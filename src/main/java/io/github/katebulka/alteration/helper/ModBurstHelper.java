package io.github.katebulka.alteration.helper;

import io.github.katebulka.alteration.ModConfig;
import io.github.katebulka.alteration.mixin.accessor.TrapDoorBlockAccessor;
import io.github.katebulka.alteration.registry.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

// https://modrinth.com/mod/burst-door-open

public class ModBurstHelper {
	private enum Type {
		DOOR,
		TRAPDOOR
	}

	private static final class Tracked {
		final ResourceKey<Level> dimension;
		final BlockPos pos;
		final Type type;
		boolean hasEntered;

		public Tracked(ResourceKey<Level> dimension, BlockPos pos, Type type, boolean hasEntered) {
			this.dimension = dimension;
			this.pos = pos.immutable();
			this.type = type;
			this.hasEntered = hasEntered;
		}
	}

	private static final Map<ServerPlayer, List<Tracked>> CACHE = new WeakHashMap<>();

	public static void tick(ServerPlayer player) {
		Level level = player.level();

		if (level.isClientSide()) return;

		if (player.isRemoved()) {
			CACHE.remove(player);
			return;
		}

		List<Tracked> tracked = CACHE.computeIfAbsent(player, p -> new ArrayList<>());
		closeDone(level, player, tracked);

		if (ModConfig.INSTANCE.burstDoor) {
			tryBurstDoor(level, player, tracked);
			tryBurstTrapdoor(level, player, tracked);
		}
	}

	private static void tryBurstDoor(Level level, ServerPlayer player, List<Tracked> tracked) {
		if (!player.isSprinting()) return;

		BlockHitResult result = getPlayerPOVHitResult(level, player);

		if (result.getType() != BlockHitResult.Type.BLOCK) return;

		BlockPos pos = result.getBlockPos();
		BlockState state = level.getBlockState(pos);

		if (!state.is(ModBlockTags.BUST_DOORS) || state.getValue(BlockStateProperties.OPEN)) return;

		Direction face = state.getValue(DoorBlock.FACING);

		double x = pos.getCenter().x() - face.getStepX() * 0.5;
		double z = pos.getCenter().z() - face.getStepZ() * 0.5;
		Vec3 doorPos = new Vec3(x, pos.getCenter().y(), z);

		double speed = horizontalSpeed(player);
		double openDistance = Mth.clamp(1.25 + speed * 2.5, 1.25, 3.5);

		if (horizontalDistance(doorPos, player.position()) > openDistance) return;

		Vec3 to = new Vec3(doorPos.x - player.getX(), 0, doorPos.z - player.getZ()).normalize();
		Vec3 move = new Vec3(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);

		if (move.lengthSqr() > 1.0E-4 && to.dot(move.normalize()) < 0.5) return;

		openDoors(level, pos, state, player, face, tracked);
	}

	private static void tryBurstTrapdoor(Level level, ServerPlayer player, List<Tracked> tracked) {
		if (player.onClimbable() || player.getDeltaMovement().y > -0.001) {
			BlockPos feetPos = player.blockPosition();
			BlockPos headPos = BlockPos.containing(player.getX(), player.getEyeY() + 0.2, player.getZ());
			BlockPos[] poses = {headPos.above(), feetPos.above(), feetPos.above(2)};

			for (BlockPos pos : poses) {
				BlockState state = level.getBlockState(pos);

				if (canOpenTrapdoor(state) && player.getEyePosition().distanceToSqr(pos.getCenter()) <= 3.0) {
					openTrapdoor(level, pos, state, player, tracked);
					return;
				}
			}
		}

		if (!player.isSprinting()) return;

		BlockHitResult hit = getPlayerPOVHitResult(level, player);

		if (hit.getType() != BlockHitResult.Type.BLOCK) return;

		BlockPos pos = hit.getBlockPos();
		BlockState state = level.getBlockState(pos);

		if (!canOpenTrapdoor(state)) return;

		double maxDist = Mth.clamp(1.35 + horizontalSpeed(player) * 2.2, 1.35, 3.25);

		if (player.position().distanceTo(pos.getCenter()) > maxDist) return;

		openTrapdoor(level, pos, state, player, tracked);
	}

	private static boolean canOpenTrapdoor(BlockState state) {
		return state.is(ModBlockTags.BUST_TRAPDOORS) && !state.getValue(BlockStateProperties.OPEN);
	}

	private static void openDoors(Level level, BlockPos pos, BlockState state, ServerPlayer player, Direction face, List<Tracked> tracked) {
		state = openDoor(level, pos, state, player, tracked, false);

		if (!ModConfig.INSTANCE.affectNearbyDoors) return;

		DoorHingeSide side = state.getValue(DoorBlock.HINGE);
		boolean rightHinge = side == DoorHingeSide.RIGHT;
		int reverse = (rightHinge ? -1 : 1) * ((face == Direction.NORTH || face == Direction.SOUTH) ? -1 : 1);

		BlockPos neighborPos = pos.offset(face.getStepZ() * reverse, face.getStepY(), face.getStepX() * reverse);
		BlockState neighbor = level.getBlockState(neighborPos);

		if (!neighbor.is(ModBlockTags.BUST_DOORS)) return;
		if (neighbor.getValue(BlockStateProperties.OPEN)) return;
		if (neighbor.getValue(DoorBlock.HINGE) != (rightHinge ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT)) return;

		openDoor(level, neighborPos, neighbor, player, tracked, true);
	}

	private static BlockState openDoor(Level level, BlockPos pos, BlockState state, @Nullable Player player, List<Tracked> tracked, boolean noSound) {
		DoorBlock door = (DoorBlock) state.getBlock();
		state = state.cycle(DoorBlock.OPEN);
		level.setBlock(pos, state, 10);

		boolean isOpen = state.getValue(DoorBlock.OPEN);

		if (!noSound) {
			level.playSound(null, pos, isOpen ? door.type().doorOpen() : door.type().doorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
		}

		level.gameEvent(player, isOpen ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);

		if (ModConfig.INSTANCE.closeDoors) {
			tracked.add(new Tracked(level.dimension(), pos, Type.DOOR, false));
		}

		return state;
	}

	// todo: i should somehow prevent sound from playing twice when doors closes...
	private static void closeDoor(Level level, BlockPos pos, BlockState state, @Nullable Player player) {
		DoorBlock door = (DoorBlock) state.getBlock();

		state = state.setValue(DoorBlock.OPEN, false);
		level.setBlock(pos, state, 10);

		level.playSound(null, pos, door.type().doorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
		level.gameEvent(player, GameEvent.BLOCK_CLOSE, pos);
	}

	private static void openTrapdoor(Level level, BlockPos pos, BlockState state, @Nullable Player player, List<Tracked> tracked) {
		TrapDoorBlock trapdoor = (TrapDoorBlock) state.getBlock();

		state = state.setValue(TrapDoorBlock.OPEN, true);
		level.setBlock(pos, state, 10);

		level.playSound(null, pos, ((TrapDoorBlockAccessor) trapdoor).alteration$getType().trapdoorOpen(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
		level.gameEvent(player, GameEvent.BLOCK_OPEN, pos);

		if (ModConfig.INSTANCE.closeDoors) {
			tracked.add(new Tracked(level.dimension(), pos, Type.TRAPDOOR, false));
		}
	}

	private static void closeTrapdoor(Level level, BlockPos pos, BlockState state, @Nullable Player player) {
		TrapDoorBlock trapdoor = (TrapDoorBlock) state.getBlock();

		state = state.setValue(TrapDoorBlock.OPEN, false);
		level.setBlock(pos, state, 10);

		level.playSound(null, pos, ((TrapDoorBlockAccessor) trapdoor).alteration$getType().trapdoorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
		level.gameEvent(player, GameEvent.BLOCK_CLOSE, pos);
	}

	private static void closeDone(Level level, ServerPlayer player, List<Tracked> tracked) {
		MinecraftServer server = level.getServer();
		Iterator<Tracked> it = tracked.iterator();

		while (it.hasNext()) {
			Tracked entry = it.next();

			if (server == null || server.getLevel(entry.dimension) == null || !entry.dimension.equals(level.dimension())) {
				it.remove();
				continue;
			}

			BlockState current = level.getBlockState(entry.pos);

			if (!current.getOptionalValue(BlockStateProperties.OPEN).orElse(false)) {
				it.remove();
				continue;
			}

			boolean colliding = entry.type == Type.DOOR ? collidesDoor(level, entry.pos, current, player) : collidesTrapdoor(entry.pos, player);

			if (colliding && !entry.hasEntered) {
				entry.hasEntered = true;
			} else if (entry.hasEntered && !colliding) {
				if (entry.type == Type.DOOR && current.getBlock() instanceof DoorBlock) {
					closeDoor(level, entry.pos, current, player);
				} else if (entry.type == Type.TRAPDOOR && current.getBlock() instanceof TrapDoorBlock) {
					closeTrapdoor(level, entry.pos, current, player);
				}

				it.remove();
			}
		}
	}

	private static boolean collidesDoor(Level level, BlockPos pos, BlockState state, Player player) {
		BlockPos p1 = state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
		BlockPos p2 = p1.above();

		AABB playerBox = player.getBoundingBox();

		return playerBox.intersects(new AABB(p1).inflate(0.05)) || playerBox.intersects(new AABB(p2).inflate(0.05));
	}

	private static boolean collidesTrapdoor(BlockPos pos, Player player) {
		AABB b = new AABB(pos).inflate(0.15);
		return player.getBoundingBox().intersects(b);
	}

	private static double horizontalSpeed(Player player) {
		Vec3 delta = player.getDeltaMovement();
		return Math.hypot(delta.x, delta.z);
	}

	private static double horizontalDistance(Vec3 pos1, Vec3 pos2) {
		return Math.hypot(pos2.x - pos1.x, pos2.z - pos1.z);
	}

	private static BlockHitResult getPlayerPOVHitResult(Level level, Player player) {
		float x = player.getXRot();
		float y = player.getYRot();

		Vec3 eyePos = player.getEyePosition();

		float f2 = Mth.cos(-y * 0.017453292F - 3.1415927F);
		float f3 = Mth.sin(-y * 0.017453292F - 3.1415927F);
		float f4 = -Mth.cos(-x * 0.017453292F);
		float f5 = Mth.sin(-x * 0.017453292F);

		Vec3 look = new Vec3(f3 * f4, f5, f2 * f4);

		double reach = player.blockInteractionRange();
		Vec3 end = eyePos.add(look.scale(reach));

		return level.clip(new ClipContext(eyePos, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
	}
}

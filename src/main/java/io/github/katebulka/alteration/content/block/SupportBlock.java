package io.github.katebulka.alteration.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SupportBlock extends RotatedPillarBlock {
	public static final BooleanProperty IS_PARENT = BooleanProperty.create("is_parent");
	private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

	public SupportBlock(Properties p_55926_) {
		super(p_55926_);

		this.registerDefaultState(
				this.stateDefinition.any()
						.setValue(AXIS, Direction.Axis.Y)
						.setValue(IS_PARENT, true)
		);
	}


	@Override
	protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		Player player = context.getPlayer();
		Level world = context.getLevel();

		if (player.isHolding(this.asItem()) || player.isSecondaryUseActive()) return false;

		if (!player.isCreative()) {
			if (player.getInventory().getFreeSlot() >= 0) {
				world.destroyBlock(context.getClickedPos(), false, player);
				player.getInventory().add(new ItemStack(this));
			} else {
				world.destroyBlock(context.getClickedPos(), true, player);
			}
		} else {
			world.destroyBlock(context.getClickedPos(), false, player);
		}

		return true;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction face = context.getClickedFace();

		if (face == null) {
			return this.defaultBlockState().setValue(IS_PARENT, true).setValue(AXIS, context.getClickedFace().getAxis());
		}

		BlockPos pos = context.getClickedPos().relative(face.getOpposite());
		BlockState adjacent = context.getLevel().getBlockState(pos);

		boolean isParent = !(adjacent.getBlock() instanceof SupportBlock);

		return this.defaultBlockState().setValue(IS_PARENT, isParent).setValue(AXIS, context.getClickedFace().getAxis());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(IS_PARENT));
	}

	private void chain(Level level, BlockPos pos) {
		if (level.isClientSide()) { return;}

		for (Direction direction : Direction.values()) {
			BlockPos dir = pos.offset(direction.getNormal());
			Block block = level.getBlockState(dir).getBlock();

			if (block instanceof SupportBlock) {
				level.scheduleTick(dir, block, 1);
			}
		}
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!state.getValue(IS_PARENT)) {
			level.destroyBlock(pos, true);
			chain(level, pos);
		}
	}

	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		super.onRemove(state, level, pos, newState, movedByPiston);

		if (state.getValue(IS_PARENT)) {
			chain(level, pos);
		}
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (context.isAbove(SHAPE, pos, false)) {
			return SHAPE;
		}

		return Shapes.empty();
	}
}

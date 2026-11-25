package io.github.katebulka.alteration.mixin.client;

import io.github.katebulka.alteration.ModConfig;
import io.github.katebulka.alteration.registry.ModBlockTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// https://modrinth.com/mod/woolproofed
@Mixin(SoundManager.class)
public abstract class SoundManagerMixin {
	@Unique
	private static BlockPos alteration$getOriginBlockPos(Level level, Vec3 soundSource) {
		BlockPos pos = BlockPos.containing(soundSource);

		if (!level.getBlockState(pos).isAir()) {
			return pos;
		}

		BlockPos below = pos.below();

		if (!level.getBlockState(below).isAir()) {
			return below;
		}

		return pos;
	}

	@Unique
	private static boolean alteration$shouldBlockSound(Level level, Vec3 soundSource, Vec3 playerPos) {
		double distance = soundSource.distanceTo(playerPos);

		if (distance < ModConfig.INSTANCE.soundProofDistance) {
			return false;
		}

		BlockPos pos = alteration$getOriginBlockPos(level, soundSource);

		if (level.getBlockState(pos).is(ModBlockTags.SOUNDBLOCKERS)) {
			return false;
		}

		Vec3 dir = playerPos.subtract(soundSource).normalize();
		BlockPos playerBlockPos = BlockPos.containing(playerPos);

		for (double d = ModConfig.INSTANCE.soundProofInterval; d < distance; d += ModConfig.INSTANCE.soundProofInterval) {
			Vec3 checkPos = soundSource.add(dir.multiply(d, d, d));
			BlockPos blockPos = BlockPos.containing(checkPos);

			if (blockPos.equals(pos) || blockPos.equals(playerBlockPos)) {
				continue;
			}

			BlockState state = level.getBlockState(blockPos);

			if (state.is(ModBlockTags.SOUNDBLOCKERS)) {
				return true;
			}
		}

		return false;
	}

	@Inject(method = "play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V", at = @At("HEAD"), cancellable = true)
	private void alteration$play(SoundInstance sound, CallbackInfo ci) {
		if (!ModConfig.INSTANCE.soundProof) {
			return;
		}

		Minecraft client = Minecraft.getInstance();

		if (client.player == null || client.level == null) {
			return;
		}

		if (sound.getX() == 0 && sound.getY() == 0 && sound.getZ() == 0) {
			return;
		}

		Vec3 soundSource = new Vec3(sound.getX(), sound.getY(), sound.getZ());
		Vec3 playerPos = client.player.position();

		if (alteration$shouldBlockSound(client.level, soundSource, playerPos)) {
			ci.cancel();
		}
	}
}

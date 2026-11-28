package io.github.katebulka.alteration.mixin;

import io.github.katebulka.alteration.ModConfig;
import io.github.katebulka.alteration.registry.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// https://modrinth.com/mod/lightning-prevention
@Mixin(ServerLevel.class)
public class ServerLevelMixin {
	@Redirect(method = "tickChunk", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerLevel;isRainingAt(Lnet/minecraft/core/BlockPos;)Z"))
	private boolean alteration$tickChunk(ServerLevel level, BlockPos pos) {
		if (!ModConfig.INSTANCE.lightningPrevention) return level.isRainingAt(pos);

		if (level.getBlockState(pos.below()).is(ModBlockTags.PREVENT_LIGHTNING_STRIKE_BASE_BLOCKS)) {
			return false;
		}

		return level.isRainingAt(pos);
	}
}

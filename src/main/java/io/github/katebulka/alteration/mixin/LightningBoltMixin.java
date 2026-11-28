package io.github.katebulka.alteration.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.katebulka.alteration.ModConfig;
import io.github.katebulka.alteration.registry.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// https://modrinth.com/mod/lightning-prevention
@Mixin(LightningBolt.class)
public class LightningBoltMixin {
	@Inject(method = "spawnFire", at = @At("HEAD"), cancellable = true)
	private void alteration$spawnFire(int extraFireTicks, CallbackInfo ci) {
		if (!ModConfig.INSTANCE.lightningPrevention) return;

		Entity self = (Entity) (Object) this;
		Level level = self.level();
		BlockPos pos = self.blockPosition();

		if (!level.getBlockState(pos.below()).is(ModBlockTags.LIGHTNING_FIRE_BASE_BLOCKS)) {
			ci.cancel();
		}
	}
}

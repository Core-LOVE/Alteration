package io.github.katebulka.alteration.mixin;

import io.github.katebulka.alteration.ModConfig;
import io.github.katebulka.alteration.registry.ModItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
		value = ItemEntity.class
)
public abstract class ItemEntityMixin {
	@Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
	private void alteration$hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (!ModConfig.INSTANCE.fireImmuneItems) return;

		ItemEntity self = (ItemEntity) (Object) this;

		if (self.getItem().is(ModItemTags.FIRELESS)) {
			if (source.is(DamageTypeTags.IS_FIRE) && !source.is(DamageTypes.LAVA)) {
				cir.setReturnValue(false);
			}
		}
	}
}


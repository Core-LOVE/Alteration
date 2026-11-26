package io.github.katebulka.alteration.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.katebulka.alteration.ModHelper;
import io.github.katebulka.alteration.registry.ModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
		value = LivingEntity.class
)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
		super(p_19870_, p_19871_);
	}

	@ModifyReturnValue(method = "getDefaultGravity", at = @At("RETURN"))
	private double alteration$getDefaultGravity(double d0) {
		return ModHelper.getGravity(this, d0);
	}

	@ModifyExpressionValue(method = "calculateFallDamage", slice = @Slice(
				from = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/ai/attributes/Attributes;SAFE_FALL_DISTANCE:Lnet/minecraft/core/Holder;"),
				to = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/ai/attributes/Attributes;FALL_DAMAGE_MULTIPLIER:Lnet/minecraft/core/Holder;")
		), at = @At(
				value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/core/Holder;)D"
		)
	)
	private double alteration$calculateFallDamage(double d0, float fall, float damage) {
		return ModHelper.getSafeFallDistance(this, d0);
	}

//	@Inject(method = "createLivingAttributes", at = @At("RETURN"))
//	private static void alteration$createLivingAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
//		Holder<Attribute> impactHolder = ModAttributes.getImpactResistanceHolder();
//
//		if (impactHolder != null) {
//			cir.getReturnValue().add(impactHolder);
//		}
//	}
}

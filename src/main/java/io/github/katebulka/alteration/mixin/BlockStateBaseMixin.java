package io.github.katebulka.alteration.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.katebulka.alteration.ModConfig;
import io.github.katebulka.alteration.registry.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.level.block.state.BlockBehaviour$BlockStateBase")
public abstract class BlockStateBaseMixin {
	@Shadow
	protected abstract BlockState asState();

	@ModifyReturnValue(method = "getLightBlock(Lnet/minecraft/entity/player/BlockGetter;Lnet/minecraft/util/math/BlockPos;)I", at = @At("RETURN"))
	private int alteration$getLightBlock(int original, BlockGetter blockGetter, BlockPos blockPos) {
		if (ModConfig.INSTANCE.darkerWater) {
			return this.asState().getFluidState().is(FluidTags.WATER) ? 3 : original;
		}

		return original;
	}

	@Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
	private void b2$itemPassThrough(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if (!ModConfig.INSTANCE.itemIgnoreBlocks) return;

		BlockBehaviour.BlockStateBase self = (BlockBehaviour.BlockStateBase) (Object) this;

		if (!self.is(ModBlockTags.ITEM_PASS_THROUGH)) return;

		Entity entity = context instanceof EntityCollisionContext ecc ? ecc.getEntity() : null;

		if (entity instanceof ItemEntity) cir.setReturnValue(Shapes.empty());
	}
}

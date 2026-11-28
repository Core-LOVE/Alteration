package io.github.katebulka.alteration.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.katebulka.alteration.ModConfig;
import io.github.katebulka.alteration.registry.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.fml.common.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
	@ModifyReturnValue(method = "getShadeBrightness(Lnet/minecraft/entity/player/BlockState;Lnet/minecraft/entity/player/BlockGetter;Lnet/minecraft/util/math/BlockPos;)F",
			at = @At("RETURN"))
	private float alteration$getShadeBrightness(float original, BlockState p_60472_, BlockGetter p_60473_, BlockPos p_60474_) {
		return (ModConfig.INSTANCE.darkerAmbientOcclusion && original == 0.2F) ? 0: original;
	}

	@Inject(method = "canBeReplaced", at = @At("RETURN"), cancellable = true)
	public void alteration$canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext, CallbackInfoReturnable cir) {
		if (!ModConfig.INSTANCE.easyBlockReplace) return;

		if (blockState.is(ModBlockTags.REPLACEABLE)) {
			Item item = blockPlaceContext.getItemInHand().getItem();

			if (item instanceof BlockItem && item == blockState.getBlock().asItem()) {
				return;
			} else {
				cir.setReturnValue(true);
			}
		}
	}
}

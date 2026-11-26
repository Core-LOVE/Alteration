package io.github.katebulka.alteration.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.katebulka.alteration.ModConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
		value = FallingBlockRenderer.class
)
public abstract class FallingBlockRendererMixin {
	@Redirect(
		method = "render(Lnet/minecraft/world/entity/item/FallingBlockEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
		at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/renderer/block/ModelBlockRenderer;tesselateBlock(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/neoforged/neoforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V"))
	private void alteration$render(ModelBlockRenderer instance, BlockAndTintGetter level, BakedModel model, BlockState state, BlockPos pos,
									PoseStack poseStack, VertexConsumer consumer, boolean check, RandomSource random, long seed, int overlay,
									ModelData modelData, RenderType renderType, FallingBlockEntity entity, float partialTick, float p_114636_,
									PoseStack outerPoseStack, MultiBufferSource bufferSource, int packedLight) {
		if (!ModConfig.INSTANCE.aoFallingBlocks) {
			instance.tesselateBlock(level, model, state, pos, poseStack, consumer, check, random, seed, overlay, modelData, renderType);
			return;
		}

		BlockPos realPos = entity.blockPosition();
		instance.tesselateWithAO(level, model, state, realPos, poseStack, consumer, false, random, seed, overlay, modelData, renderType);
	}
}

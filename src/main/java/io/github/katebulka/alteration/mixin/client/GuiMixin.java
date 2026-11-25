package io.github.katebulka.alteration.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.katebulka.alteration.ModConfig;
import io.github.katebulka.alteration.ModHelper;
import io.github.katebulka.alteration.compat.RaisedCompat;
import io.github.katebulka.alteration.registry.ModSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = Gui.class
)
public class GuiMixin {
	@Unique
	private static final ResourceLocation alteration$air_empty = ResourceLocation.withDefaultNamespace("hud/air_empty");

	@Shadow
	public int rightHeight;

	@Shadow
	@Final
	private static ResourceLocation AIR_SPRITE;

	@Shadow
	@Final
	private static ResourceLocation AIR_BURSTING_SPRITE;

	@Shadow
	@Final
	private Minecraft minecraft;

	@Unique
	private int alteration$lastBubblePopSoundPlayed;

	@Unique
	private int alteration$prevHealth = -1;

	@Shadow
	private int lastHealth;

	@Shadow
	private long healthBlinkTime;

	@Shadow
	private int tickCount;

	@Shadow
	private Player getCameraPlayer() {
		throw new AssertionError();
	}

	@Unique
	private int alteration$lastHealSoundTick = -1000;

	@Inject(method = "renderHealthLevel", at = @At("HEAD"))
	private void alteration$renderHealthLevel(GuiGraphics guiGraphics, CallbackInfo ci) {
		if (!ModConfig.INSTANCE.playHealSound) {
			return;
		}

		Player player = this.getCameraPlayer();

		if (player == null || this.minecraft.level == null) return;

		int hp = Mth.ceil(player.getHealth());

		if (alteration$prevHealth != -1 && hp > alteration$prevHealth && player.invulnerableTime > 0 && this.tickCount - alteration$lastHealSoundTick > 4) {
			alteration$playHealSound(player);
		}

		alteration$prevHealth = hp;
	}


	@Inject(method = "renderAirLevel", at = @At("HEAD"), cancellable = true)
	private void alteration$renderAirBubbles(GuiGraphics g, CallbackInfo ci) {
		if (!ModConfig.INSTANCE.renderEmptyBubbles && !ModConfig.INSTANCE.playUiBubblePop && !ModConfig.INSTANCE.playUiBubblePopOutside) {
			return;
		}

		Player player = this.getCameraPlayer();

		if (player == null) {
			return;
		}

		int i1 = g.guiWidth() / 2 + 91;
		this.minecraft.getProfiler().push("air");
		int i3 = player.getMaxAirSupply();
		int j3 = Math.min(player.getAirSupply(), i3);

		if (player.isEyeInFluid(FluidTags.WATER) || j3 < i3) {
			int j2 = g.guiHeight() - this.rightHeight;
			int l3 = Mth.ceil((double)(j3 - 2) * 10.0F / i3);
			int i4 = Mth.ceil((double)j3 * 10.0F / i3) - l3;

			RenderSystem.enableBlend();

			g.pose().pushPose();

			if (ModHelper.isModLoaded("raised")) {
				RaisedCompat.translate(g, "oxygen_bar");
			}

			for (int j4 = 0; j4 < 10; ++j4) {
				int x = i1 - j4 * 8 - 9;

				if (j4 < l3) {
					g.blitSprite(AIR_SPRITE, x, j2, 9, 9);
				} else if (j4 < l3 + i4) {
					alteration$playAirBubblePoppedSound(j4, player, j3);
					g.blitSprite(AIR_BURSTING_SPRITE, x, j2, 9, 9);
				} else {
					if (ModConfig.INSTANCE.renderEmptyBubbles) {
						g.blitSprite(alteration$air_empty, x, j2, 9, 9);
					}
				}
			}

			RenderSystem.disableBlend();

			g.pose().popPose();

			this.rightHeight += 10;
		}

		this.minecraft.getProfiler().pop();
		ci.cancel();
	}

	@Unique
	private void alteration$playAirBubblePoppedSound(int i, Player player, int j) {
		if (!ModConfig.INSTANCE.playUiBubblePop) return;

		if (!player.isEyeInFluid(FluidTags.WATER) && !ModConfig.INSTANCE.playUiBubblePopOutside) return;

		if (this.alteration$lastBubblePopSoundPlayed != i) {
			float f = 0.5F + 0.1F * Math.max(0, j - 3 + 1);
			float g = 1.0F + 0.1F * Math.max(0, j - 5 + 1);

			Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ModSoundEvents.BUBBLE_POP.get(), f, g));

			this.alteration$lastBubblePopSoundPlayed = i;
		}
	}

	@Unique
	private void alteration$playHealSound(Player player) {
		Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ModSoundEvents.HP_HEAL.get(), 1.0F, 1.0F));
		alteration$lastHealSoundTick = this.tickCount;
	}
}

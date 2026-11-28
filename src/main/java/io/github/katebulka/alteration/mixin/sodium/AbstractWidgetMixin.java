package io.github.katebulka.alteration.mixin.sodium;

import com.mojang.blaze3d.systems.RenderSystem;
import net.caffeinemc.mods.sodium.client.gui.Dimensioned;
import net.caffeinemc.mods.sodium.client.gui.widgets.AbstractWidget;
import net.caffeinemc.mods.sodium.client.util.Dim2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//@Mixin(
//		value = AbstractWidget.class
//)
public abstract class AbstractWidgetMixin implements Dimensioned {
	@Unique
	private static final WidgetSprites SPRITES = new WidgetSprites(
			ResourceLocation.withDefaultNamespace("widget/button"),
			ResourceLocation.withDefaultNamespace("widget/button_disabled"),
			ResourceLocation.withDefaultNamespace("widget/button_highlighted")
	);

	@Shadow
	protected void drawCenteredString(GuiGraphics graphics, Component text, int x, int y, int color) {}

	@Inject(method = "drawRect", at = @At("HEAD"), cancellable = true)
	private void alteration$drawRect(GuiGraphics graphics, int x1, int y1, int x2, int y2, int color, CallbackInfo ci) {
		AbstractWidget self = (AbstractWidget) (Object) this;

		Minecraft minecraft = Minecraft.getInstance();
		graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		graphics.blitSprite(SPRITES.get(self.isActive(), self.isHovered() || self.isFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
		graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

		ci.cancel();
//		graphics.fill(x1, y1, x2, y2, color);
	}
}

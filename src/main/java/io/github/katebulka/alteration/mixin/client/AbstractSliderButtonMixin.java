package io.github.katebulka.alteration.mixin.client;

import io.github.katebulka.alteration.ModHelper;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(
		value = AbstractSliderButton.class
)
public abstract class AbstractSliderButtonMixin extends AbstractWidget {
	public AbstractSliderButtonMixin(int p_93629_, int p_93630_, int p_93631_, int p_93632_, Component p_93633_) {
		super(p_93629_, p_93630_, p_93631_, p_93632_, p_93633_);
	}

	@ModifyArg(index = 3, method = "renderWidget", at = @At(value = "INVOKE",  target = "Lnet/minecraft/client/gui/components/AbstractSliderButton;renderScrollingString(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;II)V"))
	private int alteration$renderWidgetColor(int c) {
		return ModHelper.getHoverTextColor(this, c);
	}
}

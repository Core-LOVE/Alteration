package io.github.katebulka.alteration.mixin.client;

import io.github.katebulka.alteration.ModHelper;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(
		value = AbstractButton.class
)
public abstract class AbstractButtonMixin extends AbstractWidget {
	public AbstractButtonMixin(int p_93629_, int p_93630_, int p_93631_, int p_93632_, Component p_93633_) {
		super(p_93629_, p_93630_, p_93631_, p_93632_, p_93633_);
	}

	@ModifyArg(index = 3, method = "renderString", at = @At(value = "INVOKE",  target = "Lnet/minecraft/client/gui/components/AbstractButton;renderScrollingString(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;II)V"))
	private int alteration$renderStringColor(int c) {
		return ModHelper.getHoverTextColor(this, c);
	}
}

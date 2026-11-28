package io.github.katebulka.alteration.mixin.sodium;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.caffeinemc.mods.sodium.client.gui.Layout;
import net.caffeinemc.mods.sodium.client.gui.widgets.OptionListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//@Mixin(
//		value = Layout.class
//)
public abstract class LayoutMixin {
	@ModifyReturnValue(method = "entryHeight", at = @At("RETURN"))
	private static int alteration$entryHeight(int original) {
		return 20;
	}
}

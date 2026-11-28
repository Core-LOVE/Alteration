package io.github.katebulka.alteration.mixin.sodium;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.caffeinemc.mods.sodium.client.gui.ColorTheme;
import net.caffeinemc.mods.sodium.client.gui.options.control.AbstractOptionList;
import net.caffeinemc.mods.sodium.client.gui.options.control.Control;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlElement;
import net.caffeinemc.mods.sodium.client.gui.widgets.OptionListWidget;
import net.caffeinemc.mods.sodium.client.util.Dim2i;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//@Mixin(
//		value = OptionListWidget.class,
//		remap = false
//)
public abstract class OptionListWidgetMixin {
	@Inject(method = "renderFilteredOptions", at = @At(
					value = "INVOKE",
					target = "Lnet/caffeinemc/mods/sodium/client/gui/options/control/Control;createElement(Lnet/minecraft/client/gui/screens/Screen;Lnet/caffeinemc/mods/sodium/client/gui/options/control/AbstractOptionList;Lnet/caffeinemc/mods/sodium/client/util/Dim2i;Lnet/caffeinemc/mods/sodium/client/gui/ColorTheme;)Lnet/caffeinemc/mods/sodium/client/gui/options/control/ControlElement;"
			), remap = false)
	private void alteration$renderFilteredOptions(Screen screen, int x, int y, int width, CallbackInfoReturnable<Integer> cir, @Local(index = 5) LocalIntRef listHeight) {
		listHeight.set(listHeight.get() + 5);
	}

	@Inject(method = "renderAllPages", at = @At(
					value = "INVOKE",
					target = "Lnet/caffeinemc/mods/sodium/client/gui/options/control/Control;createElement(Lnet/minecraft/client/gui/screens/Screen;Lnet/caffeinemc/mods/sodium/client/gui/options/control/AbstractOptionList;Lnet/caffeinemc/mods/sodium/client/util/Dim2i;Lnet/caffeinemc/mods/sodium/client/gui/ColorTheme;)Lnet/caffeinemc/mods/sodium/client/gui/options/control/ControlElement;"
			), remap = false)
	private void alteration$renderAllPages(Screen screen, int x, int y, int width, CallbackInfoReturnable<Integer> cir, @Local(index = 5) LocalIntRef listHeight) {
		listHeight.set(listHeight.get() + 5);
	}
}

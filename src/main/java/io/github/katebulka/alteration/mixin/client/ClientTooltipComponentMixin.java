package io.github.katebulka.alteration.mixin.client;

import io.github.katebulka.alteration.tooltip.ModTooltips;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// https://github.com/crastinator/tooltips-improved/tree/main
@Mixin(ClientTooltipComponent.class)
public interface ClientTooltipComponentMixin {
	//? if !forge || >=1.21 {
	@Inject(method = "create(Lnet/minecraft/world/inventory/tooltip/TooltipComponent;)Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;",
			at = @At("HEAD"), cancellable = true)
	private static void tooltipsimproved$createPreview(TooltipComponent component, CallbackInfoReturnable<ClientTooltipComponent> cir) {
		if (component instanceof ModTooltips.Preview preview) cir.setReturnValue(preview.renderer());
	}
	//?}
}

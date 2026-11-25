package io.github.katebulka.alteration.mixin.client;

import io.github.katebulka.alteration.ModHelper;
import io.github.katebulka.alteration.tooltip.ModTooltips;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

// https://github.com/crastinator/tooltips-improved
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
//	@Inject(method = "getTooltipLines", at = @At("RETURN"), cancellable = true)
//			//? if >=1.20.5 {
//	private void tooltipsimproved$addLines(Item.TooltipContext context, Player player, TooltipFlag flag, CallbackInfoReturnable<List<Component>> cir) {
//		//?} else {
//		/*private void tooltipsimproved$addLines(Player player, TooltipFlag flag, CallbackInfoReturnable<List<Component>> cir) {
//		 *///?}
//		cir.setReturnValue(ModTooltipsExtras.addLines((ItemStack) (Object) this, cir.getReturnValue(), player, flag));
//	}

	// An item's own image, like a bundle's contents, comes first
	@Inject(method = "getTooltipImage", at = @At("RETURN"), cancellable = true)
	private void alteration$getTooltipImage(CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
		if (cir.getReturnValue().isEmpty()) cir.setReturnValue(ModTooltips.forStack((ItemStack) (Object) this, ModHelper.isShifting()));
	}
}

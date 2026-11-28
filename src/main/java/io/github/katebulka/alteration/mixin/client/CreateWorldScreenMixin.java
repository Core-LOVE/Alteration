package io.github.katebulka.alteration.mixin.client;

import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(
		value = CreateWorldScreen.class
)
public abstract class CreateWorldScreenMixin {

}

package io.github.katebulka.alteration.mixin.client;

import net.minecraft.world.entity.item.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(
		value = FallingBlockEntity.class
)
public abstract class FallingBlockEntityMixin {
}

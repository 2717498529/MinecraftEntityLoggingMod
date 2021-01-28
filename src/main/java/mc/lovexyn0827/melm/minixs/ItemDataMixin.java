package mc.lovexyn0827.melm.minixs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.ItemEntity;

@Mixin(ItemEntity.class)
public interface ItemDataMixin {
	@Accessor("health")
	public float getHealthMELM();
}

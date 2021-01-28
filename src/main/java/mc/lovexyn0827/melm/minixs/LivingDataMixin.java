package mc.lovexyn0827.melm.minixs;

import net.minecraft.entity.LivingEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingDataMixin {
	@Accessor("jumping")
	public boolean isJumpingMELM();
	
	@Accessor("movementSpeed")
	public float getMovementSpeedMELM();
}

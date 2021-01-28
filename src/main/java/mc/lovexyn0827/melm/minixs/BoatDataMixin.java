package mc.lovexyn0827.melm.minixs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.vehicle.BoatEntity;

@Mixin(BoatEntity.class)
public interface BoatDataMixin {
	@Accessor("velocityDecay")
	public float getVelocityDecayMELM();
}

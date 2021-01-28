package mc.lovexyn0827.melm.minixs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.ExperienceOrbEntity;

@Mixin(ExperienceOrbEntity.class)
public interface ExperienceOrbDataMixin {
	@Accessor("health")
	public float getHealthMELM();
}

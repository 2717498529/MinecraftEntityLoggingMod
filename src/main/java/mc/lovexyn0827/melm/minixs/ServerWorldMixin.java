package mc.lovexyn0827.melm.minixs;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.world.ServerWorld;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
	@Inject(@At="tickentity(Lnet/minecraft/server/world/server/);V")
}

package mc.lovexyn0827.melm;

import java.io.IOException;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.world.World;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;

@SuppressWarnings("deprecation")
public class MinecraftEntityLoggingMod implements ModInitializer,WorldTickCallback,ServerStartCallback {
	public static LoggingFile loggingFile;

	public void onInitialize() {
		
	}

	public void onStartServer(MinecraftServer server) {
		try {
			MinecraftEntityLoggingMod.loggingFile = new LoggingFile(server.getFile(server.getName()));
		} catch (IOException e) {
			CrashReport report = new CrashReport("Failed to create logging file beacuse of ", e);
		}
	}

	public void tick(World world) {
		for(Entity entity:((ServerWorld)world).getEntitiesByType(EntityType.TNT, (e) -> true)) {
			recordEntity(world, entity);
		}
		for(Entity entity:((ServerWorld)world).getEntitiesByType(EntityType.ARROW, (e) -> true)) {
			recordEntity(world, entity);
		}
		for(Entity entity:((ServerWorld)world).getEntitiesByType(EntityType.PLAYER, (e) -> true)) {
			recordEntity(world, entity);
		}
		for(Entity entity:((ServerWorld)world).getEntitiesByType(EntityType.ARMOR_STAND, (e) -> true)) {
			recordEntity(world, entity);
		}
		for(Entity entity:((ServerWorld)world).getEntitiesByType(EntityType.SKELETON, (e) -> true)) {
			recordEntity(world, entity);
		}
	}
	
	private void recordEntity(World world, Entity entity) {
		if(!MinecraftEntityLoggingMod.loggingFile.entityRecords.containsKey(entity)) {
			MinecraftEntityLoggingMod.loggingFile.writeEntityRecord(new EntityRecord(entity, world));
		}
		int id = MinecraftEntityLoggingMod.loggingFile.entityRecords.get(entity).id;
		MinecraftEntityLoggingMod.loggingFile.writeLogItem(new LogItem(id, world, entity));
	}
}

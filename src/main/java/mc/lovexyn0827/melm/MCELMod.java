package mc.lovexyn0827.melm;

import java.util.HashSet;

import com.google.common.collect.Sets;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class MCELMod implements ModInitializer {

	public static final HashSet<EntityType<?>> OBSERVED_TYPES = Sets.newHashSet();
	public static final String ROOT_DIR = FabricLoader.getInstance().getGameDir().toString()+"/logfiles/";
	private static int runTicks = -1;
	private static int pauseTicks = -1;
	public static boolean hasLoggingFile = false;
	
	@Override
	public void onInitialize() {
		
	}
	
	public static void tick(World world) {
		if(runTicks>0) {
			--runTicks;
		}
		if(pauseTicks>0) {
			--pauseTicks;
		}
		LogFile.tick(world);
	}

}

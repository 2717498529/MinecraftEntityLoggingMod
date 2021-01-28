package mc.lovexyn0827.melm;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mc.lovexyn0827.melm.minixs.LivingDataMixin;
import mc.lovexyn0827.melm.minixs.BoatDataMixin;
import mc.lovexyn0827.melm.minixs.ExperienceOrbDataMixin;
import mc.lovexyn0827.melm.minixs.ItemDataMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LogFile extends DataOutputStream {

	private static LogFile instance = null;
	private static final Logger LOGGER = LogManager.getLogger();
	private final Object2IntOpenHashMap<Entity> entity2IdMap = new Object2IntOpenHashMap<>();
	private int currentEntityId = 0;
	
	private LogFile() throws IOException {
		super(new BufferedOutputStream(new FileOutputStream(MCELMod.ROOT_DIR+System.currentTimeMillis()+".mel")));
	}
	
	public static void createIfNull() {
		if(instance != null) return;
		try {
			instance = new LogFile();
		} catch (IOException e) {
			LOGGER.error("Failed to create log file:"+e);
			MCELMod.hasLoggingFile = false;
		}
		MCELMod.hasLoggingFile = true;
	}
	
	public static void create() {
		instance = null;
		createIfNull();
	}
	
	/**
	 * Called when a tick begins
	 * @param world Context
	 */
	public static void tick(World world) {
		try {
			instance.writeByte(0);
			instance.writeLong(world.getTime());
		} catch (IOException e) {
			LOGGER.error("Failed to write tick header:"+e);
		}
		
	}
	/**
	 * Called when an entity is being written
	 * @param entity Entity being written
	 */
	public static void writeEntity(Entity entity) {
		if(!instance.isEntityRegistered(entity))
			try {
				instance.registerEntity(entity);
				instance.writeEntityBasicData(entity);
				if(entity instanceof LivingEntity) {
					instance.writeLivingData((LivingEntity)entity);
				}else if(entity instanceof TntEntity) {
					instance.writeTntData((TntEntity)entity);
				}else if(entity instanceof ItemEntity||entity instanceof ExperienceOrbEntity) {
					instance.writeItemData(entity);
				}else if(entity instanceof BoatEntity) {
					instance.writeBoatData((BoatEntity)entity);
				}else {
					instance.writeByte(0);
				}
			} catch (IOException e) {
				LOGGER.error("Failed to write entity data:"+e);
			}
	}

	private void writeBoatData(BoatEntity entity) throws IOException {
		writeByte(4);
		writeFloat(((BoatDataMixin)entity).getVelocityDecayMELM());
	}

	private void writeItemData(Entity entity) throws IOException {
		writeByte(3);
		int age = 0;
		float health = 0.0f;
		if(entity instanceof ItemEntity) {
			age = ((ItemEntity)entity).getAge();
			health = ((ItemDataMixin)entity).getHealthMELM();
		}else {
			age = ((ExperienceOrbEntity)entity).age;
			health = ((ExperienceOrbDataMixin)entity).getHealthMELM();
		}
		writeInt(age);
		writeFloat(health);
	}

	private void writeTntData(TntEntity entity) throws IOException {
		writeByte(2);
		writeInt(entity.getFuse());
	}

	private void writeLivingData(LivingEntity entity) throws IOException {
		writeByte(1);
		int livingFlags = ((entity.hurtTime==entity.maxHurtTime)?0:1<<4)|
				(entity.isFallFlying()?0:1<<3)|
				(((LivingDataMixin)entity).isJumpingMELM()?0:1<<2)|
				(entity.isSleeping()?0:1<<1)|
				(entity.isDead()?0:1);
		writeByte(livingFlags);
		writeFloat(entity.forwardSpeed);
		writeFloat(entity.sidewaysSpeed);
		writeFloat(entity.upwardSpeed);
		writeFloat(entity.getHealth());
		writeFloat(entity.flyingSpeed);
		writeFloat(((LivingDataMixin)entity).getMovementSpeedMELM());
	}

	private void writeEntityBasicData(Entity entity) throws IOException {
		writeByte(2);
		int id = this.entity2IdMap.getInt(entity);
		writeInt(id);
		Vec3d motion = entity.getVelocity();
		writeDouble(motion.x);
		writeDouble(motion.y);
		writeDouble(motion.z);
		Vec3d pos = entity.getPos();
		writeDouble(pos.x);
		writeDouble(pos.y);
		writeDouble(pos.z);
		writeFloat(entity.getYaw(0.0f));
		writeFloat(entity.getPitch(0.0f));
		writeFloat(entity.fallDistance);
		int vehicleId = -1;
		if(entity.hasVehicle()) {
			vehicleId = this.entity2IdMap.getOrDefault(entity, -2);
		}
		writeInt(vehicleId);
		int mutableFlags = (entity.horizontalCollision?0:1<<6)|
				(entity.verticalCollision?0:1<<5)|
				(entity.isOnGround()?0:1<<4)|
				(entity.isWet()?0:1<<3)|
				(entity.isSprinting()?0:1<<2)|
				(entity.isSneaking()?0:1<<1)|
				(entity.isSwimming()?0:1);
		writeByte(mutableFlags);
		writeFloat(entity.stepHeight);
	}

	private void registerEntity(Entity entity) throws IOException {
		writeByte(1);
		int id = this.entity2IdMap.put(entity, this.currentEntityId++);
		writeInt(id);
		writeLong(entity.getEntityWorld().getTime());
		UUID uuid = entity.getUuid();
		writeLong(uuid.getMostSignificantBits());
		writeLong(uuid.getLeastSignificantBits());
		int immutableFlags = (entity.isGlowing()?0:1<<3)|
				(entity.isInvulnerable()?0:1<<2)|
				(entity.isCollidable()?0:1<<1)|
				(entity.hasNoGravity()?0:1);
		writeByte(immutableFlags);
		String typeStr = entity.getType().getTranslationKey().replaceFirst("^.+\\u002e", "");
		writeByte(typeStr.length());
		writeChars(typeStr);
		if(entity.hasCustomName()) {
			String customName = entity.getCustomName().asString();
			customName = customName.substring(0, MathHelper.clamp(customName.length(),0,127));
			writeByte(customName.length());
			writeChars(customName);
		}else {
			writeByte(0);
		}
	}

	private boolean isEntityRegistered(Entity entity) {
		if(this.entity2IdMap.containsKey(entity)) return true;
		return false;
	}

}

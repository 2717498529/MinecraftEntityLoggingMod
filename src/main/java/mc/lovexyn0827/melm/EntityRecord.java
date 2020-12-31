package mc.lovexyn0827.melm;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityRecord {
	
	public Entity entityIn;
	private World worldIn;
	public final int id;
	private boolean wrote = false;
	
	public EntityRecord(Entity entity, World world) {
		this.entityIn = entity;
		this.worldIn = world;
		this.id = LoggingFile.currentId++;
	}
	
	public void write(DataOutputStream f) {
		if (this.wrote) return;
		try {
			f.writeByte(0xff);
			f.writeInt(this.id);
			f.writeLong(this.entityIn.getUuid().getMostSignificantBits());
			f.writeLong(this.entityIn.getUuid().getLeastSignificantBits());
			f.writeLong(this.worldIn.getTime());
			String type = this.entityIn.getType().getTranslationKey();
			f.writeByte(type.length());
			f.writeChars(type);
			String name = this.entityIn.getCustomName().asString();
			if(name.length()<=127) {
				name = name.substring(0, 64);
			}
			f.writeByte(name.length());
			f.writeChars(name);
		} catch (IOException e) {
			System.err.println("Failed to write"+this+"beacuse of"+e);
		}finally {
			this.wrote = true;
		}
	}
	
	public int hashcode() {
		return this.id;
	}
	
	public String toString() {
		return "[EntityRecord:"+id+","+this.entityIn.getUuidAsString()+"]";
	}
}

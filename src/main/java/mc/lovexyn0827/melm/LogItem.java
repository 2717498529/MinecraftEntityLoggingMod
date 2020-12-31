package mc.lovexyn0827.melm;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LogItem {

	public final int id;
	private World worldIn;
	private Entity entityIn;
	private boolean wrote = false;

	public LogItem(int id, World world, Entity entity) {
		this.id = id;
		this.entityIn = entity;
		this.worldIn = world;
	}
	
	public void write(DataOutputStream f) {
		if(this.wrote) return;
		try {
			f.writeByte(0);
			f.writeInt(this.id);
			f.writeLong(this.worldIn.getTime());
			Vec3d v = this.entityIn.getVelocity();
			f.writeDouble(v.x);
			f.writeDouble(v.y);
			f.writeDouble(v.z);
			f.writeDouble(this.entityIn.getX());
			f.writeDouble(this.entityIn.getY());
			f.writeDouble(this.entityIn.getZ());
			f.writeFloat(this.entityIn.getYaw(0));
			f.writeFloat(this.entityIn.getPitch(0));
			if(this.entityIn instanceof LivingEntity) {
				LivingEntity entity = ((LivingEntity)(this.entityIn));
				f.writeFloat(entity.getHealth());
				f.writeFloat(entity.forwardSpeed);
				f.writeFloat(entity.sidewaysSpeed);
				f.writeFloat(entity.upwardSpeed);
			}else if(this.entityIn instanceof TntEntity) {
				f.writeInt(((TntEntity)this.entityIn).getFuse());
			}
		} catch (IOException e) {
			System.err.println("Failed to write"+this+"beacuse of"+e);
		}finally{
			this.wrote = true;
		}
	}
	
	public String toString() {
		return "[LogItem:"+id+","+worldIn+"]";	
	}

}

package mc.lovexyn0827.melm;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import net.minecraft.entity.Entity;

public class LoggingFile {
	public static int currentId = 0;
	HashMap<Entity,EntityRecord> entityRecords = new HashMap<>();
	DataOutputStream f;
	
	public LoggingFile(File path) throws IOException {
		this.f = new DataOutputStream(
				new BufferedOutputStream(
						new FileOutputStream(path)
						)
				);
	}
	
	public void writeEntityRecord(EntityRecord er) {
		if(this.entityRecords.containsValue(er)) {
			this.entityRecords.put(er.entityIn, er);
			er.write(f);
		}
	}
	
	public void writeLogItem(LogItem li) {
		li.write(f);
	}
}

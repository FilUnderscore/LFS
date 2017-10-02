package lamp.filesystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lamp.filesystem.file.LFSDrive;
import lamp.filesystem.file.io.FileByteArrayOutputStream;

public class LFS 
{
	private static List<LFSDrive> DRIVES = new ArrayList<>();
	
	private static ByteArrayInputStream BAIS;
	
	public static void close() throws Exception
	{
		DRIVES.clear();
		
		if(BAIS != null)
			BAIS.close();
	}
	
	public static void read(ByteArrayInputStream bais)
	{
		DRIVES.add(LFSDrive.readDrive(bais));
		
		BAIS = bais;
	}
	
	public static byte[] write() throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		for(LFSDrive drive : DRIVES)
		{
			drive.writeDrive(baos);
		}
		
		return baos.toByteArray();
	}
	
	public static LFSDrive addDrive(LFSDrive drive)
	{
		if(!DRIVES.contains(drive))
		{
			DRIVES.add(drive);
		}
		
		return drive;
	}
	
	public static Map<LFSDrive, byte[]> save(byte[] dat) throws IOException
	{
		Map<LFSDrive, byte[]> map = new HashMap<>();
		
		for(LFSDrive drive : DRIVES)
		{
			FileByteArrayOutputStream baos = new FileByteArrayOutputStream();
			
			byte[] data = write();
			
			baos.write(dat, 0, dat.length);
			
			//byte[] wD = writeDrive(drive);
			
			//baos.write(wD, 0, 0, wD.length);
			
			baos.write(data, 0, drive.VOLUME_FILETABLE, data.length);

			map.put(drive, baos.toByteArray());
			
			baos.close();
		}
		
		return map;
	}
	
	public static byte[] writeDrive(LFSDrive drive) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		Util.writeString(baos, drive.VOLUME_NAME);
		
		Util.writeString(baos, drive.VOLUME_TYPE);
		
		Util.writeInt(baos, drive.VOLUME_SECTOR_SIZE);
		
		Util.writeInt(baos, drive.VOLUME_SECTORS_PER_TRACK);
		
		Util.writeInt(baos, drive.VOLUME_CYLINDERS);
		
		Util.writeInt(baos, drive.VOLUME_HEADS);
		
		Util.writeInt(baos, drive.VOLUME_FILETABLE);
		
		Util.writeInt(baos, drive.VOLUME_PARTITIONS);
		
		Util.writeInt(baos, drive.VOLUME_VERSION);
		
		return baos.toByteArray();
	}
	
	public static List<LFSDrive> getDrives()
	{
		return Collections.unmodifiableList(DRIVES);
	}
	
	public static LFSDrive getDefaultDrive()
	{
		return DRIVES.get(0);
	}
	
	public static String string()
	{
		try
		{
			StringBuilder builder = new StringBuilder();
			for(LFSDrive drive : getDrives())
			{
				builder.append(drive.toString());
			}
			return builder.toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
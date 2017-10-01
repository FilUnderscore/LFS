package lamp.filesystem;

import java.io.ByteArrayInputStream;

public class LFS 
{
	public String VOLUME_NAME;
	
	public String VOLUME_TYPE;
	
	public int VOLUME_SECTOR_SIZE;
	
	public int VOLUME_SECTORS_PER_TRACK;
	
	public int VOLUME_CYLINDERS;
	
	public int VOLUME_HEADS;
	
	public long VOLUME_MAXCAPACITY;

	public int VOLUME_FILETABLE;
	
	public int VOLUME_PARTITIONS;
	
	public int VOLUME_VERSION;
	
	public static LFS read(ByteArrayInputStream bais)
	{
		LFS lfs = new LFS();
		
		lfs.VOLUME_NAME = Util.readString(bais);
		
		lfs.VOLUME_TYPE = Util.readString(bais);
		
		lfs.VOLUME_SECTOR_SIZE = Util.readInt(bais);
		
		lfs.VOLUME_SECTORS_PER_TRACK = Util.readInt(bais);
		
		lfs.VOLUME_CYLINDERS = Util.readInt(bais);
		
		lfs.VOLUME_HEADS = Util.readInt(bais);
		
		//lfs.VOLUME_MAXCAPACITY = Integer.toUnsignedLong(Util.readInt(bais));
		
		lfs.VOLUME_MAXCAPACITY = Integer.toUnsignedLong(lfs.VOLUME_SECTOR_SIZE * lfs.VOLUME_SECTORS_PER_TRACK * lfs.VOLUME_CYLINDERS * lfs.VOLUME_HEADS);
		
		lfs.VOLUME_FILETABLE = Util.readInt(bais);
		
		lfs.VOLUME_PARTITIONS = Util.readInt(bais);
		
		lfs.VOLUME_VERSION = Util.readInt(bais);
		
		return lfs;
	}
	
	public String toString()
	{
		try
		{
			return Util.dump(this.getClass(), this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
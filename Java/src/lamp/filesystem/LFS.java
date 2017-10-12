package lamp.filesystem;

import java.util.ArrayList;
import java.util.List;

import lamp.filesystem.io.LFSTypeOutputStream;
import lamp.filesystem.type.LFSDrive;

public final class LFS 
{
	private static List<LFSDrive> drives;
	
	public static void initialize()
	{
		drives = new ArrayList<>();
	}
	
	public static LFSDrive loadDrive(byte[] driveData)
	{
		if(driveData == null)
			return null;
		
		LFSDrive drive = (LFSDrive) LFSType.load(LFSDrive.class, driveData);
		
		addDrive(drive);
		
		return drive;
	}
	
	public static byte[] unloadDrive(LFSDrive drive)
	{
		if(drive == null)
			return null;
		
		byte[] data = saveDrive(drive);
		
		drive.eject();
		removeDrive(drive);
		
		return data;
	}
	
	public static void addDrive(LFSDrive drive)
	{
		if(drive == null)
			return;
		
		drives.add(drive);
	}
	
	private static void removeDrive(LFSDrive drive)
	{
		if(drive == null)
			return;
		
		drives.remove(drive);
	}
	
	public static byte[] saveDrive(LFSDrive drive)
	{
		if(drive == null)
			return null;
		
		LFSTypeOutputStream out = new LFSTypeOutputStream();
		
		drive.save(out);
		
		return out.getBuffer();
	}
	
	public static byte[] saveDrive(String driveId)
	{
		return saveDrive(getDrive(driveId));
	}
	
	public static LFSDrive getDrive(String driveId)
	{
		for(LFSDrive drive : drives)
		{
			if(drive.getDriveId() == driveId)
			{
				return drive;
			}
		}
		
		return null;
	}
	
	//TODO: Get list of currently used Drive IDs, and generate unique id.
	public static String assignDriveId()
	{
		return "D";
	}
}
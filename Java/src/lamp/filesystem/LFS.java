package lamp.filesystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lamp.filesystem.exception.FileAlreadyExistsException;
import lamp.filesystem.exception.FileNotFoundException;
import lamp.filesystem.io.LFSTypeInputStream;
import lamp.filesystem.io.LFSTypeOutputStream;
import lamp.filesystem.type.LFSDrive;
import lamp.filesystem.type.LFSFile;

/**
 * The main Lamp File System (LFS) instance (static).
 * 
 * @author Filip Jerkovic
 */
public final class LFS 
{
	/*
	 * VARIABLES
	 */
	
	public static final String FILE_SEPARATOR = "/";
	
	/**
	 * List of {@link LFSDrive}(s)
	 */
	private static List<LFSDrive> drives;
	
	/**
	 * Initialize the {@link LFSDrive} list.
	 */
	public static void initialize()
	{
		drives = new ArrayList<>();
	}
	
	/**
	 * 
	 * @param driveData
	 * @return
	 */
	public static LFSDrive loadDrive(byte[] driveData)
	{
		if(driveData == null)
			return null;
		
		LFSDrive drive = (LFSDrive) LFSType.load(LFSDrive.class, new LFSTypeInputStream(driveData));
		
		addDrive(drive);
		
		return drive;
	}
	
	/**
	 * 
	 * @param drive
	 * @return
	 */
	public static byte[] unloadDrive(LFSDrive drive)
	{
		if(drive == null)
			return null;
		
		byte[] data = saveDrive(drive);
		
		drive.eject();
		removeDrive(drive);
		
		return data;
	}
	
	/**
	 * 
	 * @param drive
	 */
	public static void addDrive(LFSDrive drive)
	{
		if(drive == null)
			return;
		
		drives.add(drive);
	}
	
	/**
	 * 
	 * @param drive
	 */
	private static void removeDrive(LFSDrive drive)
	{
		if(drive == null)
			return;
		
		drives.remove(drive);
	}
	
	/**
	 * 
	 * @param drive
	 * @return
	 */
	public static byte[] saveDrive(LFSDrive drive)
	{
		if(drive == null)
			return null;
		
		LFSTypeOutputStream out = new LFSTypeOutputStream();
		
		drive.save(out);
		
		return out.getBuffer();
	}
	
	/**
	 * 
	 * @param driveId
	 * @return
	 */
	public static byte[] saveDrive(String driveId)
	{
		return saveDrive(getDrive(driveId));
	}
	
	/**
	 * 
	 * @param driveId
	 * @return
	 */
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
	/**
	 * 
	 * @return
	 */
	public static String assignDriveId()
	{
		return "D";
	}
	
	/*
	 * SEARCH FILESYSTEM METHODS
	 */
	
	/**
	 * 
	 * @return
	 */
	public static List<LFSDrive> getDrives()
	{
		return Collections.unmodifiableList(drives);
	}
	
	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public static LFSFile getFile(String filePath) throws FileNotFoundException
	{
		for(LFSDrive drive : drives)
		{
			for(LFSFile file : drive.getFiles())
			{
				if(file.getFullPath().equalsIgnoreCase(filePath))
				{
					return file;
				}
			}
		}
		
		return null;
	}
	
	/*
	 * FILESYSTEM CREATE/DELETE METHODS
	 */
	
	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public static LFSFile createFile(String filePath) throws FileAlreadyExistsException
	{
		try
		{
			if(getFile(filePath) != null)
				throw new FileAlreadyExistsException(filePath);
		}
		catch(FileNotFoundException e)
		{
			//Handle...
		}
		
		//Parse drive/directory tree..
		String fileName = "";
		
		return new LFSFile(fileName);
	}
}
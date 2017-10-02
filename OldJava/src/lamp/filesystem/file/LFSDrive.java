package lamp.filesystem.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import lamp.filesystem.Util;

public class LFSDrive 
{
	public String VOLUME_NAME;
	public String VOLUME_TYPE;
	
	public int VOLUME_SECTOR_SIZE;
	public int VOLUME_SECTORS_PER_TRACK;
	public int VOLUME_CYLINDERS;
	public int VOLUME_HEADS;
	
	public long VOLUME_MAXCAPACITY;
	
	/**
	 * File table manages file locations (hierarchy)
	 */
	public int VOLUME_FILETABLE;
	
	public int VOLUME_PARTITIONS;
	public int VOLUME_VERSION;
	
	public ByteArrayInputStream BAIS;
	
	public List<LFSDirectory> DIRECTORIES;
	
	private LFSDrive() {}
	
	public LFSDrive(String volumeName, String volumeType, int volumeSectorSize, int volumeSectorsPerTrack, int volumeCylinders, int volumeHeads, int volumeFileTable, int volumePartitions, int volumeVersion, LFSDirectory... directories)
	{
		this.VOLUME_NAME = volumeName;
		this.VOLUME_TYPE = volumeType;
		this.VOLUME_SECTOR_SIZE = volumeSectorSize;
		this.VOLUME_SECTORS_PER_TRACK = volumeSectorsPerTrack;
		this.VOLUME_CYLINDERS = volumeCylinders;
		this.VOLUME_HEADS = volumeHeads;
		
		this.calculateVolumeMaxCapacity();
		
		this.VOLUME_FILETABLE = volumeFileTable;
		this.VOLUME_PARTITIONS = volumePartitions;
		this.VOLUME_VERSION = volumeVersion;
		
		if(directories == null || directories.length <= 0)
		{
			this.DIRECTORIES = new ArrayList<>();
		}
		else
		{
			this.DIRECTORIES = Arrays.asList(directories);
		}
	}
	
	public void calculateVolumeMaxCapacity()
	{
		this.VOLUME_MAXCAPACITY = Integer.toUnsignedLong(this.VOLUME_SECTOR_SIZE * this.VOLUME_SECTORS_PER_TRACK * this.VOLUME_CYLINDERS * this.VOLUME_HEADS);
	}
	
	public static LFSDrive readDrive(ByteArrayInputStream bais)
	{
		LFSDrive lfsDrive = new LFSDrive();
		
		lfsDrive.VOLUME_NAME = Util.readString(bais);
		
		lfsDrive.VOLUME_TYPE = Util.readString(bais);
		
		lfsDrive.VOLUME_SECTOR_SIZE = Util.readInt(bais);
		
		lfsDrive.VOLUME_SECTORS_PER_TRACK = Util.readInt(bais);
		
		lfsDrive.VOLUME_CYLINDERS = Util.readInt(bais);
		
		lfsDrive.VOLUME_HEADS = Util.readInt(bais);
		
		lfsDrive.calculateVolumeMaxCapacity();
		
		lfsDrive.VOLUME_FILETABLE = Util.readInt(bais);
		
		lfsDrive.VOLUME_PARTITIONS = Util.readInt(bais);
		
		lfsDrive.VOLUME_VERSION = Util.readInt(bais);
		
		lfsDrive.BAIS = bais;
		
		lfsDrive.loadDirectories();
		
		return lfsDrive;
	}
	
	public void writeDrive(ByteArrayOutputStream baos) throws IOException
	{
		this.saveDirectories(baos);
	}
	
	public LFSDirectory addDirectory(LFSDirectory directory)
	{
		this.DIRECTORIES.add(directory);
		directory.parentDrive = this;
		
		return directory;
	}
	
	public void loadDirectories()
	{
		this.DIRECTORIES = new ArrayList<>();
		this.BAIS.reset();
		
		//Jump to FILETABLE Address in Hard Drive
		Util.skip(this.BAIS, this.VOLUME_FILETABLE);
		
		int directoriesSize = Util.readInt(this.BAIS);
		/*
		 * Scan through all directories.
		 */
		for(int directoryIndex = 0; directoryIndex < directoriesSize; directoryIndex++)
		{
			LFSDirectory lfsDirectory = LFSDirectory.readDirectory(this.BAIS);
			lfsDirectory.parentDrive = this;
			
			this.DIRECTORIES.add(lfsDirectory);
		}
		
		/*
		 * Set Parent Directory of all Directories if set.
		 */
		for(LFSDirectory dir : this.DIRECTORIES)
		{
			if(dir.parentDirectoryName != null && dir.parentDirectoryName.length() > 0)
			{
				dir.parentDirectory = this.getDirectory(dir.parentDirectoryName);
			}
		}
	}
	
	public void saveDirectories(ByteArrayOutputStream baos) throws IOException
	{
		Util.writeInt(baos, this.DIRECTORIES.size());
		
		for(LFSDirectory directory : this.DIRECTORIES)
		{
			directory.writeDirectory(baos);
		}
	}
	
	public LFSDirectory getDirectory(String name)
	{
		for(LFSDirectory dir : this.DIRECTORIES)
		{
			if(dir.directoryName.equalsIgnoreCase(name))
			{
				return dir;
			}
		}
		
		return null;
	}
	
	public String toString()
	{
		try 
		{
			return Util.dump(this.getClass(), this);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
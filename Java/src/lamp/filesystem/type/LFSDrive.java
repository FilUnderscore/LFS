package lamp.filesystem.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lamp.filesystem.LFS;
import lamp.filesystem.LFSType;
import lamp.filesystem.io.LFSTypeInputStream;
import lamp.filesystem.io.LFSTypeOutputStream;

/**
 * 
 * @author Filip Jerkovic
 */
public class LFSDrive extends LFSType
{
	/**
	 * 
	 */
	private String driveId;
	
	/**
	 * 
	 * @param name
	 */
	public LFSDrive(String name)
	{
		super(name);
		
		this.driveId = LFS.assignDriveId();
	}
	
	/**
	 * 
	 * @param driveId
	 * @param name
	 */
	public LFSDrive(String driveId, String name) 
	{
		super(name);
		
		this.driveId = driveId;
	}
	
	/*
	 * OVERRIDEN METHODS
	 */
	
	/**
	 * 
	 * @param in
	 */
	public void load(LFSTypeInputStream in)
	{
		//TODO: Jump to position 2 in Buffer and read Bootsector header for LFS.
		//		Also support GPT for booting.
	
		this.driveId = in.readString();
		
		super.load(in);
	}
	
	/**
	 * 
	 * @param out
	 */
	public void save(LFSTypeOutputStream out)
	{
		//TODO: Jump to position 2 in Buffer and write Bootsector header for LFS.
		//		Also support GPT for booting.
		
		out.writeString(this.driveId);
		
		super.save(out);
	}
	
	/*
	 * METHODS
	 */
	
	/**
	 * 
	 */
	public void eject()
	{
		//TODO: Call to LFS, or call from LFS.
	}
	
	/**
	 * 
	 * @param directory
	 * @return
	 */
	public LFSDirectory createDirectory(LFSDirectory directory)
	{
		this.addChild(directory);
		
		return directory;
	}
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	public LFSFile createFile(LFSFile file)
	{
		this.addChild(file);
		
		return file;
	}
	
	/**
	 * 
	 * @param directoryName
	 * @return
	 */
	public LFSDirectory createDirectory(String directoryName)
	{
		return createDirectory(new LFSDirectory(directoryName));
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public LFSFile createFile(String fileName)
	{
		return createFile(new LFSFile(fileName));
	}
	
	/**
	 * 
	 * @return
	 */
	public LFSDirectory[] getDirectories()
	{
		List<LFSDirectory> dirs = new ArrayList<>();
		
		for(LFSType type : this.children)
		{
			if(type instanceof LFSDirectory)
			{
				LFSDirectory dir = (LFSDirectory) type;
				
				dirs.add(dir);
				
				dirs.addAll(Arrays.asList(dir.getDirectories()));
			}
		}
		
		return dirs.toArray(new LFSDirectory[dirs.size()]);
	}
	
	/**
	 * 
	 * @return
	 */
	public LFSFile[] getFiles()
	{
		List<LFSFile> files = new ArrayList<>();
		
		for(LFSDirectory dir : this.getDirectories())
		{
			for(LFSFile file : dir.getFiles())
			{
				files.add(file);
			}
		}
		
		return files.toArray(new LFSFile[files.size()]);
	}
	
	/*
	 * RETURN METHODS
	 */
	
	/**
	 * 
	 * @return
	 */
	public String getDriveId()
	{
		return this.driveId;
	}
	
	/*
	 * OVERRIDEN METHODS
	 */
	
	public int getTypeId()
	{
		return LFSType.DRIVE;
	}
}
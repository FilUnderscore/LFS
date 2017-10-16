package lamp.filesystem.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lamp.filesystem.LFSType;

/**
 * 
 * @author Filip Jerkovic
 */
public interface LFSFileParent 
{
	/*
	 * METHODS (from lamp.filesystem.LFSType)
	 */
	
	/**
	 * 
	 * @param type
	 */
	public abstract void addChild(LFSType... type);
	
	/**
	 * 
	 * @return
	 */
	public abstract LFSType[] getChildren();
	
	/*
	 * FILE PARENT METHODS
	 */
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	public default LFSFile createFile(LFSFile file)
	{
		this.addChild(file);
		
		return file;
	}
	
	/**
	 * 
	 * @param directory
	 * @return
	 */
	public default LFSDirectory createDirectory(LFSDirectory directory)
	{
		this.addChild(directory);
		
		return directory;
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public default LFSFile createFile(String fileName)
	{
		return createFile(new LFSFile(fileName));
	}
	
	/**
	 * 
	 * @param directoryName
	 * @return
	 */
	public default LFSDirectory createDirectory(String directoryName)
	{
		return createDirectory(new LFSDirectory(directoryName));
	}
	
	/*
	 * FILE PARENT RETURN METHODS
	 */
	
	/**
	 * 
	 * @return
	 */
	public default LFSDirectory[] getDirectories()
	{
		List<LFSDirectory> dirs = new ArrayList<>();
		
		for(LFSType type : this.getChildren())
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
	public default LFSFile[] getFiles()
	{
		List<LFSFile> files = new ArrayList<>();
		
		for(LFSFile file : this.getFiles())
		{
			files.add(file);
		}
		
		for(LFSDirectory dir : this.getDirectories())
		{
			for(LFSFile file : dir.getFiles())
			{
				files.add(file);
			}
		}
		
		return files.toArray(new LFSFile[files.size()]);
	}
}
package lamp.filesystem.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lamp.filesystem.LFSType;
import lamp.filesystem.io.LFSTypeInputStream;
import lamp.filesystem.io.LFSTypeOutputStream;

/**
 * 
 * @author Filip Jerkovic
 */
public class LFSDirectory extends LFSType
{
	/**
	 * 
	 * @param name
	 */
	public LFSDirectory(String name) 
	{
		super(name);
	}
	
	/**
	 * 
	 */
	public void save(LFSTypeOutputStream out)
	{
		super.save(out);
	}
	
	/**
	 * 
	 */
	public void load(LFSTypeInputStream in)
	{
		super.load(in);
	}
	
	/*
	 * METHODS
	 */
	
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
		
		for(LFSType type : this.children)
		{
			if(type instanceof LFSFile)
			{
				files.add((LFSFile)type);
			}
		}
		
		return files.toArray(new LFSFile[files.size()]);
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
	 * @param fileName
	 * @return
	 */
	public LFSFile createFile(String fileName)
	{
		return createFile(new LFSFile(fileName));
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
}
package lamp.filesystem.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lamp.filesystem.LFS;
import lamp.filesystem.Util;

public class LFSDirectory 
{
	public LFSDrive parentDrive;
	public LFSDirectory parentDirectory;
	
	public String parentDirectoryName;
	
	public String directoryName;
	
	public int lfsFlags;
	
	public ByteArrayInputStream BAIS;
	
	public List<LFSFile> FILES;
	
	private LFSDirectory() {}
	
	public LFSDirectory(LFSDirectory parentDirectory, String dirName, LFSFile...files)
	{
		this(parentDirectory, dirName);
		
		if(files == null || files.length <= 0)
		{
			this.FILES = new ArrayList<>();
		}
		else
		{
			this.FILES = Arrays.asList(files);
		}
	}
	
	public LFSDirectory(LFSDrive parentDrive, LFSDirectory parentDirectory, String dirName)
	{
		this.parentDrive = parentDrive;
		this.parentDirectory = parentDirectory;
		this.directoryName = dirName;
		
		this.FILES = new ArrayList<>();
	}
	
	public LFSDirectory(LFSDirectory parentDirectory, String dirName)
	{
		this(LFS.getDefaultDrive(), parentDirectory, dirName);
	}
	
	public LFSDirectory(String dirName)
	{
		this(null, dirName);
	}
	
	public static LFSDirectory readDirectory(ByteArrayInputStream bais)
	{
		LFSDirectory lfsDirectory = new LFSDirectory();
		
		//Use parent directory name for now.
		lfsDirectory.parentDirectoryName = Util.readString(bais);
		
		lfsDirectory.directoryName = Util.readString(bais);
		
		lfsDirectory.lfsFlags = Util.readInt(bais);
		
		lfsDirectory.BAIS = bais;
		
		lfsDirectory.readFiles();
		
		return lfsDirectory;
	}
	
	public void addFile(LFSFile file)
	{
		this.FILES.add(file);
	}
	
	public void writeDirectory(ByteArrayOutputStream baos) throws IOException
	{
		if(this.parentDirectory != null)
			Util.writeString(baos, this.parentDirectory.directoryName);
		else
			Util.writeString(baos, "");
		
		Util.writeString(baos, this.directoryName);
		
		Util.writeInt(baos, this.lfsFlags);
		
		this.saveFiles(baos);
	}
	
	public void readFiles()
	{
		this.FILES = new ArrayList<>();
		
		int filesSize = Util.readInt(this.BAIS);
		
		for(int fileIndex = 0; fileIndex < filesSize; fileIndex++)
		{
			LFSFile lfsFile = LFSFile.readFile(this.BAIS);
			lfsFile.lfsDirectory = this;
			
			this.FILES.add(lfsFile);
		}
	}
	
	public void saveFiles(ByteArrayOutputStream baos) throws IOException
	{	
		Util.writeInt(baos, this.FILES.size());
		
		for(LFSFile lfsFile : this.FILES)
		{
			lfsFile.writeFile(baos);
		}
	}
	
	public String toString()
	{
		try
		{
			return Util.dump(this.getClass(), this, "parentDrive", "parentDirectory");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
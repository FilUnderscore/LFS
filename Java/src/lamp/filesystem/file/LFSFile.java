package lamp.filesystem.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import lamp.filesystem.Util;

public class LFSFile 
{
	public LFSDirectory lfsDirectory;
	
	public String fileName;

	public int lfsFlags;
	
	public byte[] fileData;
	
	private LFSFile() {}
	
	public LFSFile(LFSDirectory lfsDirectory, String fileName, int lfsFlags, byte[] fileData)
	{
		this.lfsDirectory = lfsDirectory;
		this.fileName = fileName;
		this.lfsFlags = lfsFlags;
		this.fileData = fileData;
	}
	
	public static LFSFile readFile(ByteArrayInputStream bais)
	{
		LFSFile lfsFile = new LFSFile();
		
		lfsFile.fileName = Util.readString(bais);
		
		lfsFile.lfsFlags = Util.readInt(bais);
		
		lfsFile.fileData = Util.readByteArray(bais);
		
		return lfsFile;
	}
	
	public void writeFile(ByteArrayOutputStream baos) throws IOException
	{
		Util.writeString(baos, this.fileName);
		
		Util.writeInt(baos, this.lfsFlags);
		
		Util.writeByteArray(baos, this.fileData, true);
	}
	
	public String toString()
	{
		try
		{
			return Util.dump(this.getClass(), this, "lfsDirectory");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
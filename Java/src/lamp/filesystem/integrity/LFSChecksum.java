package lamp.filesystem.integrity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import lamp.filesystem.io.LFSTypeInputStream;
import lamp.filesystem.io.LFSTypeOutputStream;
import lamp.util.Hash;

/**
 * Checksums for LFS (Lamp File System)
 * 
 * Will only be run if a file change is not recorded within the file system
 * log, and hasn't been verified.
 * 
 * If the checksum fails, the file has been corrupted by the file system and will be
 * removed, notifying the user.
 * 
 * @author Filip Jerkovic
 */
public class LFSChecksum 
{
	public byte[] md5;
	public byte[] sha512;

	private LFSChecksum() {}
	
	/**
	 * 
	 * @param fileName (Includes path directory)
	 * @return
	 */
	public static LFSChecksum load(String fileName) throws IOException
	{
		LFSChecksum checksum;
		
		String checksumFile = fileName + ".chksum";
		
		byte[] fileBytes = Files.readAllBytes(Paths.get(fileName));
		
		
		//Checksum has not been made for file, either because it's new or modified.
		if(!new File(checksumFile).exists())
		{
			checksum = save(fileName, fileBytes);
			
			return checksum;
		}
		else
		{
			byte[] checksumFileBytes = Files.readAllBytes(Paths.get(checksumFile));
			
			checksum = loadChecksum(checksumFileBytes);
			
			// No checksum or invalid checksum data.
			if(checksum == null)
			{
				checksum = save(fileName, fileBytes);
				
				return checksum;
			}
			else
			{
				if(!checksum.confirmChecksum(fileBytes))
				{
					//Deletes the file and it's checksum file.
					deleteFile(fileName, checksumFile);
				}
				else
				{
					return checksum;
				}
			}
		}
		
		return null;
	}
	
	private static LFSChecksum save(String fileName, byte[] fileBytes) throws IOException 
	{
		LFSChecksum checksum = new LFSChecksum();
		
		checksum.updateChecksum(fileBytes);
		
		Files.write(Paths.get(fileName + ".chksum"), checksum.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		
		return checksum;
	}

	private static void deleteFile(String fileName, String checksumFileName)
	{
		//Use LFS file delete handler and notify user..
	}
	
	/**
	 * Confirm checksum of file bytes.
	 * 
	 * @param file [e.g. file name: file.chksum] (checksum file could be hidden in directory by OS)
	 * @return
	 */
	private static LFSChecksum loadChecksum(byte[] chksumFileBytes)
	{
		LFSChecksum checksum = new LFSChecksum();
		
		try
		{
			LFSTypeInputStream in = new LFSTypeInputStream(chksumFileBytes);
			
			checksum.md5 = in.readArray();
			checksum.sha512 = in.readArray();
		}
		catch(Exception e)
		{
			//Possibly either null value supplied, file is empty, or file is invalid.
			
			e.printStackTrace();
			
			return null;
		}
		
		return checksum;
	}
	
	/**
	 * Confirm checksum of the file bytes.
	 * 
	 * @param file [e.g. file name: file.ext]
	 */
	public boolean confirmChecksum(byte[] fileBytes)
	{
		try
		{
			if(Hash.md5Hash(fileBytes) != this.md5 || Hash.sha512Hash(fileBytes) != this.sha512)
			{
				return false;
			}
			
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void updateChecksum(byte[] newFileBytes)
	{
		try
		{
			this.md5 = Hash.md5Hash(newFileBytes);
			
			this.sha512 = Hash.sha512Hash(newFileBytes);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public byte[] getBytes()
	{
		LFSTypeOutputStream out = new LFSTypeOutputStream();
		
		out.writeArray(this.md5);
		out.writeArray(this.sha512);
		
		return out.getBuffer();
	}
}
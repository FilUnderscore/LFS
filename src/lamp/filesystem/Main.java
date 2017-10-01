package lamp.filesystem;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import lamp.filesystem.file.LFSDirectory;
import lamp.filesystem.file.LFSDrive;
import lamp.filesystem.file.LFSFile;
import lamp.filesystem.file.LFSFlag;

public class Main 
{
	public static String FILE_URI = "../Lamp/bin/bootloader/boot.iso";
	public static byte[] ORIGINAL_DATA;
	
	public static void main(String[] args) throws Exception
	{
		ORIGINAL_DATA = Files.readAllBytes(Paths.get(FILE_URI));
		
		testDrive();
		
		testWrite();
		
		LFS.close();
		
		testRead();
	}
	
	public static void testDrive()
	{
		LFSDrive ldrive = LFS.addDrive(new LFSDrive("Local Drive", "LFS", 512, 32, 512, 256, 1048576, 1, 1));
		
		LFSDirectory ldir = ldrive.addDirectory(new LFSDirectory(null, "root"));
		ldir.addFile(new LFSFile(ldir, ".kbin", (LFSFlag.EXECUTE | LFSFlag.READ | LFSFlag.WRITE), new byte[6]));
		
		LFSDirectory ldir2 = ldrive.addDirectory(new LFSDirectory(ldir, "test"));
		ldir2.addFile(new LFSFile(ldir2, "file.bin", (LFSFlag.EXECUTE), new byte[] { (byte)0xFF, (byte)0xA0, (byte)0xAA, (byte)0xBB, (byte)0xCC, (byte)0xDD, (byte)0xEE, (byte)0xFF }));
	}
	
	public static void testWrite() throws Exception
	{
		Map<LFSDrive, byte[]> newDataMap = LFS.save(ORIGINAL_DATA);
		
		byte[] data = null;
		
		for(LFSDrive drive : newDataMap.keySet())
		{
			data = newDataMap.get(drive);
		}
		
		write(FILE_URI, data);
	}
	
	public static void testRead()
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(ORIGINAL_DATA);
		
		LFS.read(bais);
	
		System.out.println(LFS.string());
	}
	
	public static void write(String file, byte[] data) throws Exception
	{
		FileOutputStream fO = new FileOutputStream(file);
		
		fO.write(data, 0, data.length);
		
		fO.flush();
		fO.close();
	}
}
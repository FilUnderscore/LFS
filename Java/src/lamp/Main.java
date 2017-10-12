package lamp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import lamp.filesystem.LFS;
import lamp.filesystem.type.LFSDirectory;
import lamp.filesystem.type.LFSDrive;
import lamp.filesystem.type.LFSFile;
import lamp.util.Dump;

/**
 * Lamp File System implementation using
 * Java, for Assembly/C++.
 * 
 * @author Filip Jerkovic
 */
public class Main 
{
	public static void main(String[] args)
	{
		//TEST -- START
		LFS lfs = new LFS();
		
		//Write Test
		test1(lfs);
		
		//Read Test
		test2(lfs);
	}
	
	public static void test1(LFS lfs)
	{
		LFSDrive lfsDrive = new LFSDrive("A:", "Drive");
		
		LFSDirectory lfsDir = new LFSDirectory("testDir");
		
		lfsDrive.addChild(lfsDir);
		
		lfsDir.addChild(new LFSFile("test", 2, new byte[] { (byte)0xEB, (byte)0x00, (byte)0xAA, (byte)0xBB, (byte)0xCC, (byte)0xDD, (byte)0xEE, (byte)0xFF }));
		lfsDir.addChild(new LFSFile("file", 4, new byte[] { (byte)0xFF, (byte)0x00, (byte)0x00, (byte)0x00 }));
		
		lfs.addDrive(lfsDrive);
		
		byte[] data = lfs.saveDrive(lfsDrive.getDriveId());
		
		System.out.println("Data: " + Dump.printHex(data));
		
		try {
			Files.write(Paths.get("test.bin"), data, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void test2(LFS lfs)
	{
		lfs.loadDrive(lfs.saveDrive("A:"));
	}
}
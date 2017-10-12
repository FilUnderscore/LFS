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
	/**
	 * Program Start.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		LFS.initialize();
		
		//Write Test
		test1();
		
		//Read Test
		test2();
	}
	
	/**
	 * Write Test
	 */
	public static void test1()
	{
		LFSDrive lfsDrive = new LFSDrive("A:", "Drive");
		
		LFSDirectory lfsDir = new LFSDirectory("testDir");
		
		lfsDrive.addChild(lfsDir);
		
		lfsDir.addChild(new LFSFile("test", 2, new byte[] { (byte)0xEB, (byte)0x00, (byte)0xAA, (byte)0xBB, (byte)0xCC, (byte)0xDD, (byte)0xEE, (byte)0xFF }));
		lfsDir.addChild(new LFSFile("file", 4, new byte[] { (byte)0xFF, (byte)0x00, (byte)0x00, (byte)0x00 }));
		
		LFS.addDrive(lfsDrive);
		
		test12 = LFS.unloadDrive(lfsDrive);
		
		System.out.println("Data: " + Dump.printHex(test12));
		
		try {
			Files.write(Paths.get("test.bin"), test12, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Read/Write Test Data (Byte Array)
	 */
	private static byte[] test12;
	
	/**
	 * Read Test
	 */
	public static void test2()
	{
		LFSDrive drive = LFS.loadDrive(test12);
		
		System.out.println("");
		System.out.println("Drive Data:");
		System.out.println("ID: " + drive.getDriveId());
		System.out.println("Name: " + drive.getName());
		System.out.println("Children: " + drive.getChildren().length);
		System.out.println("Metadata: " + drive.getMetadata().toString());
		System.out.println("");
		
		test12 = LFS.unloadDrive(drive);
		
		System.out.println("Data: " + Dump.printHex(test12));
	}
}
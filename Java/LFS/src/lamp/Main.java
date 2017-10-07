package lamp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.xml.bind.DatatypeConverter;

import lamp.filesystem.LFS;
import lamp.filesystem.LFSTypeMetadata;
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
	
		test1(lfs);
	}
	
	public static void test1(LFS lfs)
	{
		LFSDrive lfsDrive = new LFSDrive("A:", "Drive", new LFSTypeMetadata());
		
		lfsDrive.addChild(new LFSFile("test", new LFSTypeMetadata(), 2, new byte[] { (byte)0xEB, (byte)0x00 }));
	
		lfs.addDrive(lfsDrive);
		
		byte[] data = lfs.saveDrive(lfsDrive.getDriveId());
		
		System.out.println("Data: " + Dump.printHex(data));
		
		try {
			Files.write(Paths.get("test.bin"), data, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
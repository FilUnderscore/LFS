package lamp.filesystem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main 
{
	public static void main(String[] args) throws IOException
	{
		LFS lfs = LFS.read(new ByteArrayInputStream(Files.readAllBytes(Paths.get("../Lamp/bin/bootloader/boot.iso"))));
	
		System.out.println(lfs.toString());
	}
}
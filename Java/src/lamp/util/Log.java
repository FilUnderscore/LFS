package lamp.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class Log 
{
	private static final File logFile = new File("log.file");
	private static BufferedWriter logWriter;
	
	static
	{	
		try 
		{
			if(!logFile.exists())
				logFile.createNewFile();
			
			logWriter = new BufferedWriter(new FileWriter(logFile));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void write(Object inputIndex)
	{
		try 
		{
			logWriter.write(inputIndex + "\n");
			logWriter.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
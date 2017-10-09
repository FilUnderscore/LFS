package lamp.filesystem.io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import lamp.io.LFSInputStream;

public class LFSTypeInputStream extends LFSInputStream
{
	public LFSTypeInputStream(byte[] buf) 
	{
		super(buf);
	}

	public static int BOOLEAN_SIZE = 1;
	public static int SHORT_SIZE = 2;
	public static int INT_SIZE = 4;
	public static int LONG_SIZE = 8;
	
	private static ByteOrder ENDIANNESS = ByteOrder.BIG_ENDIAN;
	
	public boolean readBoolean()
	{
		byte[] array = this.readArray(BOOLEAN_SIZE);
		
		byte n = ByteBuffer.wrap(array).order(ENDIANNESS).get();
		
		boolean result = (n == 0 ? false : true);
		
		return result;
	}
	
	public int readInt()
	{
		byte[] array = this.readArray(INT_SIZE);
		
		int n = ByteBuffer.wrap(array).order(ENDIANNESS).getInt();
		
		return n;
	}
	
	public long readLong()
	{
		byte[] array = this.readArray(LONG_SIZE);
		
		long n = ByteBuffer.wrap(array).order(ENDIANNESS).getLong();
		
		return n;
	}
	
	public String readString()
	{
		int arrayLength = this.readInt();
		
		byte[] array = this.readArray(arrayLength);
		
		String str = new String(array, StandardCharsets.UTF_8);
		
		return str;
	}
	
	public byte[] readArray()
	{
		int arrayLength = this.readInt();
		
		return this.readArray(arrayLength);
	}
}
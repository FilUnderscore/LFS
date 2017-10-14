package lamp.filesystem.io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import lamp.io.LFSInputStream;

import static lamp.util.ByteUtil.*;

/**
 * 
 * @author Filip Jerkovic
 */
public class LFSTypeInputStream extends LFSInputStream
{
	/**
	 * 
	 * @param buf
	 */
	public LFSTypeInputStream(byte[] buf) 
	{
		super(buf);
	}
	
	/**
	 * 
	 */
	private static ByteOrder ENDIANNESS = ByteOrder.BIG_ENDIAN;
	
	/*
	 * RETURN METHODS
	 */
	
	/**
	 * 
	 * @return
	 */
	public boolean readBoolean()
	{
		byte[] array = this.readArray(BOOLEAN_SIZE);
		
		byte n = ByteBuffer.wrap(array).order(ENDIANNESS).get();
		
		boolean result = (n == 0 ? false : true);
		
		return result;
	}
	
	/**
	 * 
	 * @return
	 */
	public int readInt()
	{
		byte[] array = this.readArray(INT_SIZE);
		
		int n = ByteBuffer.wrap(array).order(ENDIANNESS).getInt();
		
		return n;
	}
	
	/**
	 * 
	 * @return
	 */
	public long readLong()
	{
		byte[] array = this.readArray(LONG_SIZE);
		
		long n = ByteBuffer.wrap(array).order(ENDIANNESS).getLong();
		
		return n;
	}
	
	/**
	 * 
	 * @return
	 */
	public String readString()
	{
		int arrayLength = this.readInt();
		
		byte[] array = this.readArray(arrayLength);
		
		String str = new String(array, StandardCharsets.UTF_8);
		
		return str;
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] readArray()
	{
		int arrayLength = this.readInt();
		
		return this.readArray(arrayLength);
	}
}
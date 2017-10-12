package lamp.filesystem.io;

import static lamp.util.ByteUtil.BOOLEAN_SIZE;
import static lamp.util.ByteUtil.INT_SIZE;
import static lamp.util.ByteUtil.LONG_SIZE;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import lamp.io.LFSOutputStream;

/**
 * 
 * @author Filip Jerkovic
 */
public class LFSTypeOutputStream extends LFSOutputStream
{
	//TODO: Create custom ByteBuffer and ByteOrder instead of relying on Java libraries.
	
	/**
	 * 
	 */
	private static ByteOrder ENDIANNESS = ByteOrder.BIG_ENDIAN;
	
	/*
	 * METHODS
	 */
	
	/**
	 * 
	 * @param b
	 */
	public void writeBoolean(boolean b)
	{
		this.writeBoolean(b, true);
	}
	
	/**
	 * 
	 * @param b
	 * @param overwrite
	 */
	public void writeBoolean(boolean b, boolean overwrite)
	{
		byte bool = (byte) (b ? 1 : 0);
		
		byte[] array = ByteBuffer.allocate(BOOLEAN_SIZE).order(ENDIANNESS).put(bool).array();
		
		this.write(array, 0, array.length, overwrite);
	}
	
	/**
	 * 
	 * @param i
	 */
	public void writeInt(int i)
	{
		this.writeInt(i, true);
	}
	
	/**
	 * 
	 * @param i
	 * @param overwrite
	 */
	public void writeInt(int i, boolean overwrite)
	{
		byte[] array = ByteBuffer.allocate(INT_SIZE).order(ENDIANNESS).putInt(i).array();
		
		this.write(array, 0, array.length, overwrite);
	}
	
	/**
	 * 
	 * @param l
	 */
	public void writeLong(long l)
	{
		this.writeLong(l, true);
	}
	
	/**
	 * 
	 * @param l
	 * @param overwrite
	 */
	public void writeLong(long l, boolean overwrite)
	{
		byte[] array = ByteBuffer.allocate(LONG_SIZE).order(ENDIANNESS).putLong(l).array();
		
		this.write(array, 0, array.length, overwrite);
	}
	
	/**
	 * 
	 * @param str
	 */
	public void writeString(String str)
	{
		this.writeString(str, true);
	}
	
	/**
	 * 
	 * @param str
	 * @param overwrite
	 */
	public void writeString(String str, boolean overwrite)
	{
		byte[] array = str.getBytes(StandardCharsets.UTF_8);
		
		this.writeInt(array.length, overwrite);
		this.write(array, 0, array.length, overwrite);
	}
	
	/**
	 * 
	 * @param array
	 */
	public void writeArray(byte[] array)
	{
		this.writeArray(array, true);
	}
	
	/**
	 * 
	 * @param array
	 * @param overwrite
	 */
	public void writeArray(byte[] array, boolean overwrite)
	{
		this.writeInt(array.length, overwrite);
		this.write(array, 0, array.length, overwrite);
	}
}
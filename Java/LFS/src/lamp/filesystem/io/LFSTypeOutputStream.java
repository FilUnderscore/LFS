package lamp.filesystem.io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.DatatypeConverter;

import lamp.io.LFSOutputStream;

public class LFSTypeOutputStream extends LFSOutputStream
{
	public static int BOOLEAN_SIZE = 1;
	public static int SHORT_SIZE = 2;
	public static int INT_SIZE = 4;
	public static int LONG_SIZE = 8;
	
	private static ByteOrder ENDIANNESS = ByteOrder.BIG_ENDIAN;
	
	public void writeBoolean(boolean b)
	{
		this.writeBoolean(b, true);
	}
	
	public void writeBoolean(boolean b, boolean overwrite)
	{
		byte bool = (byte) (b ? 1 : 0);
		
		byte[] array = ByteBuffer.allocate(BOOLEAN_SIZE).order(ENDIANNESS).put(bool).array();
		
		this.write(array, 0, array.length, overwrite);
	}
	
	public void writeInt(int i)
	{
		this.writeInt(i, true);
	}
	
	public void writeInt(int i, boolean overwrite)
	{
		byte[] array = ByteBuffer.allocate(INT_SIZE).order(ENDIANNESS).putInt(i).array();
		
		this.write(array, 0, array.length, overwrite);
	}
	
	public void writeLong(long l)
	{
		this.writeLong(l, true);
	}
	
	public void writeLong(long l, boolean overwrite)
	{
		byte[] array = ByteBuffer.allocate(LONG_SIZE).order(ENDIANNESS).putLong(l).array();
		
		this.write(array, 0, array.length, overwrite);
	}
	
	public void writeString(String str)
	{
		this.writeString(str, true);
	}
	
	public void writeString(String str, boolean overwrite)
	{
		byte[] array = str.getBytes(StandardCharsets.UTF_8);
		
		this.writeInt(array.length, overwrite);
		this.write(array, 0, array.length, overwrite);
	}
	
	public void writeArray(byte[] array)
	{
		this.writeArray(array, true);
	}
	
	public void writeArray(byte[] array, boolean overwrite)
	{
		this.writeInt(array.length, overwrite);
		this.write(array, 0, array.length, overwrite);
	}
}
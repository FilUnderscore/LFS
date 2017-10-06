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
		byte bool = (byte) (b ? 1 : 0);
		
		byte[] array = ByteBuffer.allocate(BOOLEAN_SIZE).order(ENDIANNESS).put(bool).array();
		
		this.write(array, 0, array.length);
	}
	
	public void writeInt(int i)
	{
		byte[] array = ByteBuffer.allocate(INT_SIZE).order(ENDIANNESS).putInt(i).array();
		
		this.write(array, 0, array.length);
	}
	
	public void writeLong(long l)
	{
		byte[] array = ByteBuffer.allocate(LONG_SIZE).order(ENDIANNESS).putLong(l).array();
		
		this.write(array, 0, array.length);
	}
	
	public void writeString(String str)
	{
		byte[] array = str.getBytes(StandardCharsets.UTF_8);
		
		this.writeInt(array.length);
		this.write(array, 0, array.length);
	}
	
	public void writeArray(byte[] array)
	{
		this.writeInt(array.length);
		this.write(array, 0, array.length);
	}
}
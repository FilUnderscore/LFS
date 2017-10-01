package lamp.filesystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

public class Util 
{
	public static int BYTE_SIZE = 1;
	public static int FLOAT_SIZE = 2;
	public static int INT_SIZE = 4;
	public static int LONG_SIZE = 8;
	
	public static String readString(ByteArrayInputStream bais)
	{
		int len = readInt(bais);
		
		return new String(readByteArray(bais, len));
	}
	
	public static void writeString(ByteArrayOutputStream baos, String string) throws IOException
	{
		writeInt(baos, string.length());
		
		writeByteArray(baos, string.getBytes(), false);
	}
	
	public static int readInt(ByteArrayInputStream bais)
	{
		byte[] arr = readByteArray(bais, INT_SIZE);
		
		return ByteBuffer.wrap(arr).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
	public static void writeInt(ByteArrayOutputStream baos, int i) throws IOException
	{
		writeByteArray(baos, ByteBuffer.allocate(INT_SIZE).order(ByteOrder.LITTLE_ENDIAN).putInt(i).array(), false);
	}
	
	public static long readLong(ByteArrayInputStream bais)
	{
		byte[] arr = readByteArray(bais);
		
		return ByteBuffer.wrap(arr).order(ByteOrder.LITTLE_ENDIAN).getLong();
	}
	
	public static void writeLong(ByteArrayOutputStream baos, long l) throws IOException
	{
		writeByteArray(baos, ByteBuffer.allocate(LONG_SIZE).order(ByteOrder.LITTLE_ENDIAN).putLong(l).array(), false);
	}
	
	public static byte[] readByteArray(ByteArrayInputStream bais)
	{
		int length = bais.read();
		
		byte[] arr = new byte[length];
		
		bais.read(arr, 0, length);
		
		return arr;
	}
	
	public static byte[] readByteArray(ByteArrayInputStream bais, int length)
	{
		byte[] arr = new byte[length];
		
		bais.read(arr, 0, length);
		
		return arr;
	}
	
	public static void writeByteArray(ByteArrayOutputStream baos, byte[] arr, boolean prefix) throws IOException
	{
		if(prefix) 
			baos.write(arr.length);
		
		baos.write(arr);
	}
	
	public static void skip(ByteArrayInputStream bais, long skip)
	{
		bais.skip(skip);
	}
	
	public static int DUMP_CURRENTINDEX = 0;
	public static int DUMP_NEWLINE = 6;
	
	public static String dump(Class<?> clazz, Object instance, String...ignore) throws Exception
	{
		List<String> ignoreList = Arrays.asList(ignore);
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("{");
		
		for(int fieldIndex = 0; fieldIndex < clazz.getDeclaredFields().length; fieldIndex++)
		{
			DUMP_CURRENTINDEX++;
			
			Field field = clazz.getDeclaredFields()[fieldIndex];
		
			if(ignoreList.contains(field.getName()))
				continue;
			
			String fieldName = field.getName();
			Object fieldValue = field.get(instance);
			
			if(DUMP_CURRENTINDEX % DUMP_NEWLINE == 0)
			{
				builder.append("\r\n");
			}
			
			if(fieldValue instanceof byte[])
			{
				fieldValue = DatatypeConverter.printHexBinary((byte[])fieldValue);
			}
			
			builder.append("\"" + fieldName + "\"" + " = " + "\"" + fieldValue + "\"");
			
			if(fieldIndex < (clazz.getDeclaredFields().length - 1))
			{
				builder.append(", ");
			}
		}
		
		builder.append("}");
		
		DUMP_CURRENTINDEX = 0;
		
		return builder.toString();
	}
}
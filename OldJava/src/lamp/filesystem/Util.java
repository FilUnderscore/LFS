package lamp.filesystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
	
	public static byte[] readByteArray(ByteArrayInputStream bais)
	{
		int length = readInt(bais);
		
		System.out.println("Len:" + length);
		
		return readByteArray(bais, length);
	}
	
	public static byte[] readByteArray(ByteArrayInputStream bais, int length)
	{
		byte[] array = new byte[length];
		
		bais.read(array, 0, length);
		
		return ByteBuffer.wrap(array).order(ByteOrder.BIG_ENDIAN).array();
	}
	
	public static void writeByteArray(ByteArrayOutputStream baos, byte[] array, boolean prefixed)
	{
		array = ByteBuffer.wrap(array).order(ByteOrder.BIG_ENDIAN).array();
		
		if(prefixed)
			writeInt(baos, array.length);
		
		baos.write(array, 0, array.length);
	}
	
	public static int readInt(ByteArrayInputStream bais)
	{
		return ByteBuffer.wrap(readByteArray(bais, INT_SIZE)).order(ByteOrder.BIG_ENDIAN).getInt(0);
	}
	
	public static void writeInt(ByteArrayOutputStream baos, int i)
	{
		writeByteArray(baos, ByteBuffer.allocate(INT_SIZE).order(ByteOrder.BIG_ENDIAN).putInt(0, i).array(), false);
	}
	
	
	public static String readString(ByteArrayInputStream bais)
	{
		return new String(readByteArray(bais));
	}
	
	public static void writeString(ByteArrayOutputStream baos, String str)
	{
		writeByteArray(baos, str.getBytes(), true);
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
	
	public static class CPU
	{
		public static byte JMP_SHORT = (byte)0xEB;
	}
}
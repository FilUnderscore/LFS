package lamp.filesystem;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Util 
{
	public static String readString(ByteArrayInputStream bais)
	{
		return new String(readByteArray(bais));
	}
	
	public static int readInt(ByteArrayInputStream bais)
	{
		byte[] arr = readByteArray(bais);
		
		return ByteBuffer.wrap(arr).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
	public static byte[] readByteArray(ByteArrayInputStream bais)
	{
		int length = bais.read();
		
		byte[] arr = new byte[length];
		
		bais.read(arr, 0, length);
		
		return arr;
	}
	
	public static String dump(Class<?> clazz, Object instance) throws Exception
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("{");
		
		for(int fieldIndex = 0; fieldIndex < clazz.getDeclaredFields().length; fieldIndex++)
		{
			Field field = clazz.getDeclaredFields()[fieldIndex];
			
			String fieldName = field.getName();
			Object fieldValue = field.get(instance);
			
			builder.append("\"" + fieldName + "\"" + " = " + "\"" + fieldValue + "\"");
			
			if(fieldIndex < (clazz.getDeclaredFields().length - 1))
			{
				builder.append(", ");
			}
		}
		
		builder.append("}");
		
		return builder.toString();
	}
}
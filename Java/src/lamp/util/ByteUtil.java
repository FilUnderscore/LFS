package lamp.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Filip Jerkovic
 */
public final class ByteUtil 
{
	/**
	 * Byte size of a boolean.
	 */
	public static int BOOLEAN_SIZE = 1;
	
	/**
	 * Byte size of a short.
	 */
	public static int SHORT_SIZE = 2;
	
	/**
	 * Byte size of a float.
	 */
	public static int FLOAT_SIZE = 2;
	
	/**
	 * Byte size of an integer.
	 */
	public static int INT_SIZE = 4;
	
	/**
	 * Byte size of a long.
	 */
	public static int LONG_SIZE = 8;
	
	/**
	 * 
	 * @param input
	 * @param finalSize
	 * @return
	 */
	public static byte[] merge(List<byte[]> input, int finalSize)
	{
		byte[] output = new byte[finalSize];
		
		int outputIndex = 0;
		
		for(int inputIndex = 0; inputIndex < input.size(); inputIndex++)
		{
			byte[] byteArray = input.get(inputIndex);
			
			for(int byteIndex = 0; byteIndex < byteArray.length; byteIndex++)
			{
				output[outputIndex++] = byteArray[byteIndex];
			}
		}
		
		return output;
	}
	
	public static List<byte[]> split(byte[] input, int perByte)
	{
		System.out.println("Input: " + Dump.printHex(input));
		
		List<byte[]> array = new ArrayList<>();
		
		for(int inputIndex = 0; inputIndex < input.length; inputIndex++)
		{
			if(inputIndex % perByte == 0)
			{
				byte[] data = new byte[perByte];
				
				for(int i = 0; i < data.length; i++)
				{
					data[i] = input[inputIndex + i];
				}
				
				array.add(data);
			}
			else
			{
				if(inputIndex == input.length)
				{
					
				}
			}
		}
		
		return array;
	}
	
	/**
	 * 
	 * @param input
	 * @param per
	 * @return
	 */
	/*
	public static List<byte[]> split(byte[] input, int per)
	{
		List<byte[]> array = new ArrayList<>();
		
		System.out.println("Per: " + per);
		System.out.println("Input: " + Dump.printHex(input));
		
		for(int inputIndex = 0; inputIndex < input.length; inputIndex++)
		{
			if(per > 0 && inputIndex % per == 0)
			{
				//TODO: Experiment
				byte[] output = new byte[per];
				
				for(int i = 0; i < output.length; i++)
				{
					output[i] = input[(inputIndex / per) + i];
				}
				
				array.add(output);
			}
			else
			{
				//TODO: Experiment
				if(inputIndex == input.length)
				{
					byte[] output = new byte[per];
					
					System.arraycopy(input, (inputIndex - (inputIndex % per)), output, 0, (inputIndex % per));
				
					array.add(output);
				}
			}
		}
		
		return array;
	}
	*/
	
	/**
	 * 
	 * @param primitiveList
	 * @return
	 */
	public static byte[] listToPrimitive(List<Byte> primitiveList)
	{
		byte[] array = new byte[primitiveList.size()];
		
		for(int primListIndex = 0; primListIndex < primitiveList.size(); primListIndex++)
		{
			if(primitiveList.get(primListIndex) != null)
			{
				array[primListIndex] = primitiveList.get(primListIndex);
			}
		}
		
		return array;
	}
	
	/**
	 * 
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	public static boolean compare(byte[] arr1, byte[] arr2)
	{
		if(arr1 == null || arr2 == null)
			return false;
		
		if(arr1.length != arr2.length)
			return false;
		
		for(int arrIndex = 0; arrIndex < arr1.length; arrIndex++)
		{
			if(arr1[arrIndex] != arr2[arrIndex])
			{
				return false;
			}
		}
		
		return true;
	}
}
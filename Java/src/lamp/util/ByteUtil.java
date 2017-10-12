package lamp.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Filip Jerkovic
 */
public final class ByteUtil 
{
	public static int BOOLEAN_SIZE = 1;
	public static int SHORT_SIZE = 2;
	public static int INT_SIZE = 4;
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
	
	/**
	 * 
	 * @param input
	 * @param per
	 * @return
	 */
	public static List<byte[]> split(byte[] input, int per)
	{
		List<byte[]> array = new ArrayList<>();
		
		for(int inputIndex = 0; inputIndex < input.length; inputIndex++)
		{
			if(inputIndex % per == 0)
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
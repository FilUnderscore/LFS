package lamp.io;

import java.util.ArrayList;
import java.util.List;

import lamp.util.ByteUtil;

public class LFSOutputStream 
{
	public static final int POSITION_FREE_COUNT = 16;
	
	protected List<Byte> buffer;
	
	protected int position;
	//protected int length;
	
	public LFSOutputStream()
	{
		this(0);
	}
	
	public LFSOutputStream(int startingSize)
	{
		this.buffer = new ArrayList<Byte>(startingSize);
	}
	
	/*
	public void grow(int newLength)
	{
		if((this.position + newLength) > this.length)
		{
			this.buffer = Arrays.copyOf(buffer, newLength);
			this.length = newLength;
		}
	}
	*/
	
	public void write(byte[] data, int offset, int length)
	{
		this.write(this.position, data, offset, length);
	}
	
	public void write(int position, byte[] data, int offset, int length)
	{
		this.position = position;
		
		for(int index = offset; index < length; index++)
		{
			this.buffer.add(this.position + index, data[index]);
			this.position++;
		}
	}
	
	public int getCurrentPosition()
	{
		return this.position;
	}
	
	public byte[] getBuffer()
	{
		return ByteUtil.listToPrimitive(this.buffer);
	}
	
	public void ifCurrentPositionAvailableThenSet()
	{
		int currentPos = this.position;
		
		int count = 0;
		
		while(count < POSITION_FREE_COUNT)
		{
			currentPos++;
			
			if(this.buffer.get(currentPos) != null || this.buffer.get(currentPos) != 0)
			{
				continue;
			}
			else
			{
				count++;
			}
		}
		
		this.position = currentPos;
	}
	
	public void toPosition(int position)
	{
		this.position = position;
	}
}
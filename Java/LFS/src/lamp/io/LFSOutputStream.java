package lamp.io;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lamp.util.ByteUtil;

public class LFSOutputStream
{
	public static final int POSITION_FREE_COUNT = 16;
	
	protected List<Byte> buffer;
	
	protected int position;
	protected int length;
	
	public LFSOutputStream()
	{
		this(0);
	}
	
	public LFSOutputStream(int startingSize)
	{
		this.length = startingSize;
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
		
		int newLength = length + position;
		if(newLength > this.length)
		{
			grow(newLength);
		}
			
		for(int index = 0; index < length; index++)
		{
			this.buffer.add(this.position, data[index + offset]);
			this.position++;
		}
	}
	
	protected void grow(int newLength)
	{
		while(this.length < newLength)
		{
			this.buffer.add((byte)0x00);
			this.length++;
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
		System.out.println("pos: " + currentPos);
		
		int count = 0;
		
		while(count < POSITION_FREE_COUNT)
		{
			if(this.buffer.get(currentPos) != 0)
			{
				System.out.println("cont");
				continue;
			}
			else
			{
				count++;
			}
			
			currentPos++;
		}
		
		this.position = currentPos;
	}
	
	public void toPosition(int position)
	{
		//Grow if needed.
		if(position > this.length)
		{
			grow(position);
		}
		
		this.position = position;
	}
}
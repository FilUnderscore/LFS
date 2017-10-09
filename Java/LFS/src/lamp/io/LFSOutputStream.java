package lamp.io;

import java.util.ArrayList;
import java.util.List;

import lamp.util.ByteUtil;

public class LFSOutputStream
{
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
	
	public void write(byte[] data, int offset, int length)
	{
		this.write(data, offset, length, true);
	}
	
	public void write(int position, byte[] data, int offset, int length, boolean overwrite)
	{
		this.position = position;
		
		int newLength = length + position;
		if(newLength > this.length)
		{
			grow(newLength);
		}
		
		if(!overwrite)
		{
			boolean verified = false;
			
			int verifyPos = this.position;
			int newPos = this.position;
			int c = 0;
			
			while(c < length)
			{
				if(this.buffer.get(verifyPos++) == 0)
				{
					c++;
				}
				else
				{
					newPos++;
					c = 0;
				}
			}
			
			this.position = newPos;
		}
		
		for(int index = 0; index < length; index++)
		{
			this.buffer.set(this.position++, data[index + offset]);
		}
	}
	
	public void write(byte[] data, int offset, int length, boolean overwrite)
	{
		this.write(this.position, data, offset, length, overwrite);
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
	
	public void ifCurrentPositionAvailableThenSet(int size)
	{
		int pos = this.position - 1;
		
		int empty = 0;
		
		while(empty < size)
		{
			//Go to next position, if this position is not empty.
			if(buffer.get(pos) != 0)
			{
				toPosition(pos++);
				
				//Reset empty counter, because address has data within, 
				//and not enough space to squeeze in.
				empty = 0;
			}
			else
				empty++;
		}
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
package lamp.io;

import java.util.ArrayList;
import java.util.List;

import lamp.util.ByteUtil;

/**
 * 
 * @author Filip Jerkovic
 */
public class LFSOutputStream
{
	/**
	 * 
	 */
	protected ArrayList<Byte> buffer;
	
	/**
	 * 
	 */
	protected int position;
	
	/**
	 * 
	 */
	protected int length;
	
	/**
	 * 
	 */
	public LFSOutputStream()
	{
		this(0);
	}
	
	/**
	 * 
	 * @param startingSize
	 */
	public LFSOutputStream(int startingSize)
	{
		this.length = startingSize;
		this.buffer = new ArrayList<Byte>(startingSize);
	}
	
	/**
	 * 
	 * @param data
	 * @param offset
	 * @param length
	 */
	public void write(byte[] data, int offset, int length)
	{
		this.write(data, offset, length, true);
	}
	
	/**
	 * 
	 * @param position
	 * @param data
	 * @param offset
	 * @param length
	 * @param overwrite
	 */
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
			this.ifCurrentPositionAvailableThenSet(length);
		}
		
		for(int index = 0; index < length; index++)
		{
			this.buffer.set(this.position++, data[index + offset]);
		}
	}
	
	/**
	 * 
	 * @param data
	 * @param offset
	 * @param length
	 * @param overwrite
	 */
	public void write(byte[] data, int offset, int length, boolean overwrite)
	{
		this.write(this.position, data, offset, length, overwrite);
	}
	
	/**
	 * 
	 * @param newLength
	 */
	protected void grow(int newLength)
	{
		while(this.length < newLength)
		{
			this.buffer.add((byte)0x00);
			this.length++;
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getCurrentPosition()
	{
		return this.position;
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] getBuffer()
	{
		return ByteUtil.listToPrimitive(this.buffer);
	}
	
	/**
	 * 
	 * @param size
	 */
	public void ifCurrentPositionAvailableThenSet(int size)
	{
		int pos = this.position;
		
		int empty = 0;
		
		while(empty < size)
		{
			//Make sure the buffer has enough space, otherwise it triggers an IndexOutOfBoundsException
			if(pos >= buffer.size())
			{
				grow(pos + 1);
			}
			
			//Go to next position, if this position is not empty.
			if(buffer.get(pos) != 0)
			{
				toPosition(pos);
				
				//Reset empty counter, because address has data within, 
				//and not enough space to squeeze in.
				empty = 0;
			}
			else
			{
				empty++;
			}
			
			pos++;
		}
	}
	
	/**
	 * 
	 * @param position
	 */
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
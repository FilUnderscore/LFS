package lamp.io;

public class LFSInputStream 
{
	/**
	 * Stored buffer that is read from.
	 */
	protected byte[] buffer;
	
	/**
	 * Current position within the buffer.
	 */
	protected int position;
	
	/**
	 * Initializes a new {@link LFSInputStream} with a buffer.
	 * 
	 * @param buf Input -> Buffer
	 */
	public LFSInputStream(byte[] buf)
	{
		this.buffer = buf;
	}
	
	/**
	 * Reads a byte from the buffer at the following position.
	 * 
	 * @param position {@link int} - The position at which the buffer is read from.
	 * 
	 * @return {@link byte} - The byte from the position in the buffer.
	 */
	public byte read(int position)
	{
		this.position = position;
		
		if(this.position < this.buffer.length)
		{
			return this.buffer[this.position++];
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Reads a byte at the current position within the buffer.
	 * 
	 * @return {@link byte} - The byte from the current position within the buffer.
	 */
	public byte read()
	{
		return this.read(this.position);
	}
	
	/**
	 * Reads an array from the buffer at the current position, with a specified length.
	 * 
	 * @param length {@link int} - Specified length to read for, from buffer.
	 * 
	 * @return {@link byte[]} - Array read from buffer
	 */
	public byte[] readArray(int length)
	{
		byte[] array = new byte[length];
		
		for(int arrayIndex = 0; arrayIndex < length; arrayIndex++)
		{
			array[arrayIndex] = read();
		}
		
		return array;
	}
	
	/**
	 * Reads an array from the buffer at the specified position, with a specified length.
	 * 
	 * @param position {@link int} - Specified position to read from within the buffer.
	 * @param length {@link int} - Specified length to read for, from buffer.
	 * 
	 * @return {@link byte[]} - Array read from buffer at specified position
	 */
	public byte[] readArray(int position, int length)
	{
		this.position = position;
		
		return this.readArray(length);
	}
	
	/**
	 * Jumps to the specified position within the buffer.
	 * 
	 * @param position {@link int} - Specified position to jump to within buffer.
	 */
	public void toPosition(int position)
	{
		this.position = position;
	}
	
	/**
	 * Returns the current position within the buffer.
	 * 
	 * @return {@link int} - Current position within buffer
	 */
	public int getCurrentPosition()
	{
		return this.position;
	}
}
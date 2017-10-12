package lamp.filesystem.data;

import lamp.util.ByteUtil;

/**
 * 
 * @author Filip Jerkovic
 */
public class LFSSegment implements SegmentedData
{
	/**
	 * 
	 */
	private byte[] data;
	
	/**
	 * 
	 */
	private long address;
	
	/**
	 * 
	 * @param data
	 */
	public LFSSegment(byte[] data)
	{
		this.data = data;
	}
	
	/**
	 * 
	 * @param address
	 */
	public void setAddress(long address)
	{
		this.address = address;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getAddress()
	{
		return this.address;
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] getData()
	{
		return this.data;
	}

	/**
	 * 
	 */
	public int getSize() 
	{
		return this.data.length;
	}
	
	/**
	 * 
	 * @param segment
	 * @return
	 */
	public boolean equals(LFSSegment segment)
	{
		if(!ByteUtil.compare(this.data, segment.data))
			return false;
		
		return true;
	}
}
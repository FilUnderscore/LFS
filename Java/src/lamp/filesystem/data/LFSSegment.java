package lamp.filesystem.data;

public class LFSSegment implements SegmentedData
{
	private byte[] data;
	private long address;
	
	public LFSSegment(byte[] data)
	{
		this.data = data;
	}
	
	public void setAddress(long address)
	{
		this.address = address;
	}
	
	public long getAddress()
	{
		return this.address;
	}
	
	public byte[] getData()
	{
		return this.data;
	}

	public int getSize() 
	{
		return this.data.length;
	}
}
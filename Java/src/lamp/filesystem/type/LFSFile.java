package lamp.filesystem.type;

import lamp.filesystem.LFSType;
import lamp.filesystem.LFSTypeMetadata;
import lamp.filesystem.io.LFSTypeInputStream;
import lamp.filesystem.io.LFSTypeOutputStream;

/**
 * 
 * @author Filip Jerkovic
 */
public class LFSFile extends LFSType
{
	/**
	 * 
	 * @param name
	 */
	public LFSFile(String name)
	{
		super(name);
	}
	
	/**
	 * 
	 * @param name
	 * @param segmentSize
	 * @param fileData
	 */
	public LFSFile(String name, int segmentSize, byte[] fileData)
	{
		this(name);
		
		this.segmentSize = segmentSize;
		this.segmentedData = fileData;
	}

	/**
	 * 
	 * @param in
	 */
	public void load(LFSTypeInputStream in) 
	{
		super.load(in);
		
		this.readSegments(in);
	}

	/**
	 * 
	 * @param out
	 */
	public void save(LFSTypeOutputStream out) 
	{
		super.save(out);
		
		this.writeSegments(out);
	}	
	
	/**
	 * 
	 * @return
	 */
	public byte[] getData()
	{
		return this.segmentedData;
	}
}
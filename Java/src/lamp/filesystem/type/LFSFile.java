package lamp.filesystem.type;

import lamp.filesystem.LFSType;
import lamp.filesystem.LFSTypeMetadata;
import lamp.filesystem.io.LFSTypeInputStream;
import lamp.filesystem.io.LFSTypeOutputStream;

public class LFSFile extends LFSType
{
	public LFSFile(String name)
	{
		super(name);
	}
	
	public LFSFile(String name, int segmentSize, byte[] fileData)
	{
		this(name);
		
		this.segmentSize = segmentSize;
		this.segmentedData = fileData;
	}

	public void load(LFSTypeInputStream in) 
	{
		super.load(in);
		
		this.readSegments(in);
	}

	public void save(LFSTypeOutputStream out) 
	{
		super.save(out);
		
		this.writeSegments(out);
	}	
	
	public byte[] getData()
	{
		return this.segmentedData;
	}
}
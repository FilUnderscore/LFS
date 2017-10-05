package lamp.filesystem.type;

import lamp.filesystem.LFSType;
import lamp.filesystem.LFSTypeMetadata;
import lamp.filesystem.io.LFSTypeInputStream;
import lamp.filesystem.io.LFSTypeOutputStream;

public class LFSFile extends LFSType
{
	public LFSFile(String name, LFSTypeMetadata typeMetadata)
	{
		super(name, typeMetadata);
	}
	
	public LFSFile(String name, LFSTypeMetadata typeMetadata, int segmentSize, byte[] fileData)
	{
		this(name, typeMetadata);
		
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
		//TODO: Write Parent Address
		
		super.save(out);
		
		this.writeSegments(out);
	}	
}
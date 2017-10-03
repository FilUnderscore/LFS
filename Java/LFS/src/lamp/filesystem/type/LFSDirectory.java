package lamp.filesystem.type;

import lamp.filesystem.LFSType;
import lamp.filesystem.io.LFSTypeInputStream;
import lamp.filesystem.io.LFSTypeOutputStream;

public class LFSDirectory extends LFSType
{
	public LFSDirectory(String name, LFSTypeMetadata typeMetadata) 
	{
		super(name, typeMetadata);
	}
	
	public void save(LFSTypeOutputStream out)
	{
		//TODO: Write Parent Address
		
		super.save(out);
	}
	
	public void load(LFSTypeInputStream in)
	{
		super.load(in);
	}
}
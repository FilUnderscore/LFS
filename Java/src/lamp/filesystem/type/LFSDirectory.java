package lamp.filesystem.type;

import lamp.filesystem.LFSType;
import lamp.filesystem.LFSTypeMetadata;
import lamp.filesystem.io.LFSTypeInputStream;
import lamp.filesystem.io.LFSTypeOutputStream;

public class LFSDirectory extends LFSType
{
	public LFSDirectory(String name) 
	{
		super(name);
	}
	
	public void save(LFSTypeOutputStream out)
	{
		super.save(out);
	}
	
	public void load(LFSTypeInputStream in)
	{
		super.load(in);
	}
}
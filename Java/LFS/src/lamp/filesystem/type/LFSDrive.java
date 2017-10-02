package lamp.filesystem.type;

import lamp.filesystem.LFSType;
import lamp.filesystem.io.LFSTypeInputStream;
import lamp.filesystem.io.LFSTypeOutputStream;

public class LFSDrive extends LFSType
{
	public LFSDrive(String name, LFSTypeData typeData) 
	{
		super(name, typeData);
	}
	
	public void load(LFSTypeInputStream in)
	{
		//TODO: Jump to position 2 in Buffer and read Bootsector header for LFS.
		//		Also support GPT for booting.
		
		super.load(in);
	}
	
	public void save(LFSTypeOutputStream out)
	{
		//TODO: Jump to position 2 in Buffer and write Bootsector header for LFS.
		//		Also support GPT for booting.
		
		super.save(out);
	}
}
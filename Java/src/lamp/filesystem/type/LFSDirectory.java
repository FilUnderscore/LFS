package lamp.filesystem.type;

import lamp.filesystem.LFSType;
import lamp.filesystem.io.LFSTypeInputStream;
import lamp.filesystem.io.LFSTypeOutputStream;

/**
 * 
 * @author Filip Jerkovic
 */
public class LFSDirectory extends LFSType implements LFSFileParent
{
	/**
	 * 
	 * @param name
	 */
	public LFSDirectory(String name) 
	{
		super(name);
		
		this.setTypeId(LFSType.DIRECTORY);
	}
	
	/**
	 * 
	 */
	public void save(LFSTypeOutputStream out)
	{
		super.save(out);
	}
	
	/**
	 * 
	 */
	public void load(LFSTypeInputStream in)
	{
		super.load(in);
	}
}
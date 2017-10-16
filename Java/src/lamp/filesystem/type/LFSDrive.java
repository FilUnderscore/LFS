package lamp.filesystem.type;

import lamp.filesystem.LFS;
import lamp.filesystem.LFSType;
import lamp.filesystem.io.LFSTypeInputStream;
import lamp.filesystem.io.LFSTypeOutputStream;

/**
 * 
 * @author Filip Jerkovic
 */
public class LFSDrive extends LFSType implements LFSFileParent
{
	/**
	 * 
	 */
	private String driveId;
	
	/**
	 * 
	 * @param name
	 */
	public LFSDrive(String name)
	{
		this(LFS.assignDriveId(), name);
	}
	
	/**
	 * 
	 * @param driveId
	 * @param name
	 */
	public LFSDrive(String driveId, String name) 
	{
		super(name);
		
		this.setTypeId(LFSType.DRIVE);
		
		this.driveId = driveId;
	}
	
	/*
	 * OVERRIDEN METHODS
	 */
	
	/**
	 * 
	 * @param in
	 */
	public void load(LFSTypeInputStream in)
	{
		//TODO: Jump to position 2 in Buffer and read Bootsector header for LFS.
		//		Also support GPT for booting.
	
		this.driveId = in.readString();
		
		super.load(in);
	}
	
	/**
	 * 
	 * @param out
	 */
	public void save(LFSTypeOutputStream out)
	{
		//TODO: Jump to position 2 in Buffer and write Bootsector header for LFS.
		//		Also support GPT for booting.
		
		out.writeString(this.driveId);
		
		super.save(out);
	}
	
	/*
	 * METHODS
	 */
	
	/**
	 * 
	 */
	public void eject()
	{
		//TODO: Call to LFS, or call from LFS.
	}
	
	/*
	 * RETURN METHODS
	 */
	
	/**
	 * 
	 * @return
	 */
	public String getDriveId()
	{
		return this.driveId;
	}
}
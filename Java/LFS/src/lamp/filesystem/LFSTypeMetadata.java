package lamp.filesystem;

import lamp.filesystem.io.LFSTypeInputStream;
import lamp.filesystem.io.LFSTypeOutputStream;

/**
 * {@link LFSType} Metadata
 *
 * @author Filip Jerkovic
 */
public class LFSTypeMetadata
{
	/*
	 * VARIABLES
	 */
	
	/**
	 * Flags set for the {@link LFSType}.
	 * 
	 * {@link LFSType}
	 */
	public int flags;
	
	/**
	 * Timestamp for {@link LFSType}'s creation.
	 */
	public long created;
	
	/**
	 * Timestamp for {@link LFSType}'s last modified.
	 */
	public long lastModified;
	
	/*
	 * METHODS
	 */
	
	/**
	 * Write to {@link LFSTypeOutputStream}.
	 * 
	 * @param out The output stream - {@link LFSTypeOutputStream}.
	 */
	public void write(LFSTypeOutputStream out)
	{
		out.writeInt(this.flags);
		
		out.writeLong(this.created);
		
		out.writeLong(this.lastModified);
	}
	
	/**
	 * Restore/load data from {@link LFSTypeInputStream}.
	 * 
	 * @param in The input stream - {@link LFSTypeInputStream}.
	 */
	public void read(LFSTypeInputStream in)
	{
		this.flags = in.readInt();
		
		this.created = in.readLong();
		
		this.lastModified = in.readLong();
	}
}
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
	private int flags;
	
	/**
	 * Timestamp for {@link LFSType}'s creation.
	 */
	private long timestampCreated;
	
	/**
	 * Timestamp for {@link LFSType}'s last modified.
	 */
	private long timestampLastModified;
	
	/**
	 * CONSTRUCTORS
	 */
	
	LFSTypeMetadata()
	{
		this.flags = LFSFlag.READ | LFSFlag.WRITE | LFSFlag.EXECUTE;
		
		this.timestampCreated = System.currentTimeMillis();
		
		this.timestampLastModified = System.currentTimeMillis();
	}
	
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
		
		out.writeLong(this.timestampCreated);
		
		out.writeLong(this.timestampLastModified);
	}
	
	/**
	 * Restore/load data from {@link LFSTypeInputStream}.
	 * 
	 * @param in The input stream - {@link LFSTypeInputStream}.
	 */
	public void read(LFSTypeInputStream in)
	{
		this.flags = in.readInt();
		
		this.timestampCreated = in.readLong();
		
		this.timestampLastModified = in.readLong();
	}
}
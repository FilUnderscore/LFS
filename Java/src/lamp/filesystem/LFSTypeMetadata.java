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
	 * {@link LFSFlag}s set for the {@link LFSType}.
	 * 
	 * {@link LFSType}
	 */
	private LFSFlag[] flags;
	
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
		this.flags = new LFSFlag[] { LFSFlag.READ, LFSFlag.WRITE, LFSFlag.EXECUTE };
		
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
		out.writeInt(this.flags.length);
		for(LFSFlag flag : this.flags)
		{
			out.writeInt(flag.ordinal());
		}
		
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
		this.flags = new LFSFlag[in.readInt()];
		for(int index = 0; index < this.flags.length; index++)
		{
			this.flags[index] = LFSFlag.values()[in.readInt()];
		}
		
		this.timestampCreated = in.readLong();
		
		this.timestampLastModified = in.readLong();
	}
	
	/**
	 * 
	 */
	public void update()
	{
		this.update(this.flags);
	}
	
	/**
	 * 
	 * @param flags
	 */
	public void update(LFSFlag...flags)
	{
		this.flags = flags;
		this.timestampLastModified = System.currentTimeMillis();
	}
	
	/*
	 * RETURN METHODS
	 */
	
	public String toString()
	{
		return "{flags = " + flags.toString() + ", " + "timestampCreated = " + timestampCreated + ", timestampLastModified = " + timestampLastModified + "}";
	}
}
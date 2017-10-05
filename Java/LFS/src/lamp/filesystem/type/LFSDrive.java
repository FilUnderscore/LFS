package lamp.filesystem.type;

import lamp.filesystem.LFSType;
import lamp.filesystem.LFSTypeMetadata;
import lamp.filesystem.io.LFSTypeInputStream;
import lamp.filesystem.io.LFSTypeOutputStream;

public class LFSDrive extends LFSType
{
	private String driveId;
	
	public LFSDrive(String driveId, String name, LFSTypeMetadata typeMetadata) 
	{
		super(name, typeMetadata);
		
		this.driveId = driveId;
	}
	
	public void load(LFSTypeInputStream in)
	{
		//TODO: Jump to position 2 in Buffer and read Bootsector header for LFS.
		//		Also support GPT for booting.
	
		this.driveId = in.readString();
		
		super.load(in);
	}
	
	public void save(LFSTypeOutputStream out)
	{
		//TODO: Jump to position 2 in Buffer and write Bootsector header for LFS.
		//		Also support GPT for booting.
		
		out.writeString(this.driveId);
		
		super.save(out);
	}
	
	public String getDriveId()
	{
		return this.driveId;
	}
}
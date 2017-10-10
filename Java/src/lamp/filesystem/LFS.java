package lamp.filesystem;

import java.util.ArrayList;
import java.util.List;

import lamp.filesystem.io.LFSTypeOutputStream;
import lamp.filesystem.type.LFSDrive;

public final class LFS 
{
	private List<LFSDrive> drives;
	
	public LFS()
	{
		this.drives = new ArrayList<>();
	}
	
	public LFSDrive loadDrive(byte[] driveData)
	{
		if(driveData == null)
			return null;
		
		LFSDrive drive = (LFSDrive) LFSType.load(LFSDrive.class, driveData);
		
		this.addDrive(drive);
		
		return drive;
	}
	
	public void addDrive(LFSDrive drive)
	{
		if(drive == null)
			return;
		
		drive.setFileSystem(this);
		drives.add(drive);
	}
	
	public byte[] saveDrive(LFSDrive drive)
	{
		if(drive == null)
			return null;
		
		LFSTypeOutputStream out = new LFSTypeOutputStream();
		
		drive.save(out);
		
		return out.getBuffer();
	}
	
	public byte[] saveDrive(String driveId)
	{
		return saveDrive(getDrive(driveId));
	}
	
	public LFSDrive getDrive(String driveId)
	{
		for(LFSDrive drive : this.drives)
		{
			if(drive.getDriveId() == driveId)
			{
				return drive;
			}
		}
		
		return null;
	}
	
	//TODO: Get list of currently used Drive IDs, and generate unique id.
	public String assignDriveId()
	{
		return null;
	}
}
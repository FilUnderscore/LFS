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
		LFSDrive drive = (LFSDrive) LFSType.load(LFSDrive.class, driveData);
		
		this.addDrive(drive);
		
		return drive;
	}
	
	public void addDrive(LFSDrive drive)
	{
		drives.add(drive);
	}
	
	public byte[] saveDrive(LFSDrive drive)
	{
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
}
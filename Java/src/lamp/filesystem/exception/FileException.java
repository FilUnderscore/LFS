package lamp.filesystem.exception;

public abstract class FileException extends Exception
{
	public FileException(String reason)
	{
		super(reason);
	}
}
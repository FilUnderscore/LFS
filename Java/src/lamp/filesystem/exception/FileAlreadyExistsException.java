package lamp.filesystem.exception;

public class FileAlreadyExistsException extends FileException
{
	public FileAlreadyExistsException(String reason)
	{
		super(reason);
	}
}
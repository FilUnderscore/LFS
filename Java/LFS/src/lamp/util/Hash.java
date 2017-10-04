package lamp.util;

import java.security.MessageDigest;

public final class Hash 
{
	public static byte[] md5Hash(byte[] arr) throws Exception
	{
		return hash("MD5", arr);
	}
	
	public static byte[] sha512Hash(byte[] arr) throws Exception
	{
		return hash("SHA-512", arr);
	}
	
	public static byte[] hash(String algorithm, byte[] arr) throws Exception
	{
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
		
		return messageDigest.digest(arr);
	}
}
package lamp.util;

import java.security.MessageDigest;

/**
 * 
 * @author Filip Jerkovic
 */
public final class Hash 
{
	/**
	 * 
	 * @param arr
	 * @return
	 * @throws Exception
	 */
	public static byte[] md5Hash(byte[] arr) throws Exception
	{
		return hash("MD5", arr);
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 * @throws Exception
	 */
	public static byte[] sha512Hash(byte[] arr) throws Exception
	{
		return hash("SHA-512", arr);
	}
	
	/**
	 * 
	 * @param algorithm
	 * @param arr
	 * @return
	 * @throws Exception
	 */
	public static byte[] hash(String algorithm, byte[] arr) throws Exception
	{
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
		
		return messageDigest.digest(arr);
	}
}
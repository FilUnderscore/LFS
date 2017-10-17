package lamp.filesystem.util;

import java.util.ArrayList;
import java.util.List;

import lamp.filesystem.LFSType;

public final class LFSUtil 
{
	public static List<LFSType> reverseTypeList(List<LFSType> list)
	{
		List<LFSType> newList = new ArrayList<>();
		
		for(int arrayIndex = (list.size() - 1); arrayIndex >= 0; arrayIndex--)
		{
			newList.add(list.get(arrayIndex));
		}
		
		return newList;
	}
}
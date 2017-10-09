package lamp.filesystem.data;

import java.util.ArrayList;

/**
 * A custom {@link ArrayList} for the use with {@link SegmentedData}.
 * 
 * @author Filip Jerkovic
 * @param <T extends SegmentedData> Segment of data must be an interface of the generic type.
 */
@SuppressWarnings("serial")
public class SegmentList<T extends SegmentedData> extends ArrayList<T> 
{
	private int totalSize;
	
	public int getTotalSize()
	{
		return this.totalSize;
	}
	
	private void incrementSize(int size)
	{
		this.totalSize += size;
	}
	
	private void decrementSize(int size)
	{
		this.totalSize -= size;
	}
	
	private void resetSize()
	{
		this.totalSize = 0;
	}
	
	public boolean add(T e)
	{
		boolean add = super.add(e);
		
		incrementSize(e.getSize());
		
		return add;
	}
	
	public void add(int index, T element)
	{
		super.add(index, element);
		
		incrementSize(element.getSize());
	}
	
	public T remove(int index)
	{
		T remove = super.remove(index);
		
		decrementSize(remove.getSize());
		
		return remove;
	}
	
	public boolean remove(T e)
	{
		boolean remove = super.remove(e);
		
		decrementSize(e.getSize());
		
		return remove;
	}
	
	public void clear()
	{
		super.clear();
		
		resetSize();
	}
}
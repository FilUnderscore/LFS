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
	/*
	 * FIELDS
	 */
	
	/**
	 * 
	 */
	private int totalSize;
	
	/*
	 * METHODS
	 */
	
	/**
	 * 
	 * @param size
	 */
	private void incrementSize(int size)
	{
		this.totalSize += size;
	}
	
	/**
	 * 
	 * @param size
	 */
	private void decrementSize(int size)
	{
		this.totalSize -= size;
	}
	
	/**
	 * 
	 */
	private void resetSize()
	{
		this.totalSize = 0;
	}
	
	/**
	 * 
	 */
	public void clear()
	{
		super.clear();
		
		resetSize();
	}
	
	/**
	 * 
	 * @param index
	 * @param element
	 */
	public void add(int index, T element)
	{
		super.add(index, element);
		
		incrementSize(element.getSize());
	}
	
	/*
	 * RETURN METHODS
	 */
	
	/**
	 * 
	 * @return
	 */
	public int getTotalSize()
	{
		return this.totalSize;
	}
	
	/**
	 * 
	 * @param e
	 */
	public boolean add(T e)
	{
		boolean add = super.add(e);
		
		incrementSize(e.getSize());
		
		return add;
	}
	
	/**
	 * 
	 * @param index
	 */
	public T remove(int index)
	{
		T remove = super.remove(index);
		
		decrementSize(remove.getSize());
		
		return remove;
	}
	
	/**
	 * 
	 * @param e
	 * @return
	 */
	public boolean remove(T e)
	{
		boolean remove = super.remove(e);
		
		decrementSize(e.getSize());
		
		return remove;
	}
	
	/**
	 * 
	 * @param element
	 * @return
	 */
	public int getIndex(T element)
	{
		for(int index = 0; index < this.size(); index++)
		{
			T indexedElement = this.get(index);
			
			if(indexedElement == element)
			{
				return index;
			}
		}
		
		return 0;
	}
}
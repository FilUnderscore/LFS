package lamp.filesystem;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import lamp.filesystem.data.LFSSegment;
import lamp.filesystem.data.SegmentList;
import lamp.filesystem.integrity.LFSChecksum;
import lamp.filesystem.io.LFSTypeInputStream;
import lamp.filesystem.io.LFSTypeOutputStream;
import lamp.filesystem.type.LFSDirectory;
import lamp.filesystem.type.LFSDrive;
import lamp.filesystem.type.LFSFile;
import lamp.util.ByteUtil;

/**
 * Lamp File System Type.
 * 
 * This is used by all Drives, Directories,
 * Files etc. using LFS.
 * 
 * @author Filip Jerkovic
 */
public abstract class LFSType 
{
	/*
	 * STATIC FIELDS
	 */
	
	/**
	 * Type Identifier.
	 */
	public static final int DRIVE = 0;
	
	/**
	 * Directory Identifier.
	 */
	public static final int DIRECTORY = 1;
	
	/**
	 * File Identifier.
	 */
	public static final int FILE = 2;
	
	/*
	 * FIELDS
	 */
		
	/**
	 * Name of this type.
	 */
	protected String name;
	
	/**
	 * Metadata for this type.
	 * 
	 * See {@link LFSTypeMetadata}.
	 */
	protected LFSTypeMetadata typeMetadata;
	
	/**
	 * Parent of this type.
	 */
	protected LFSType parent;
	
	/**
	 * Instead of using names, if we use addresses,
	 * we can spot the exact parent of this file and
	 * not a random one.
	 */
	protected long parentAddress;
	
	/**
	 * Children of this type.
	 */
	protected LFSType[] children;
	
	/**
	 * Children's Addresses in storage that can be accessed
	 * by reading/skipping.
	 */
	protected long[] childrenAddresses;
	
	/**
	 * Generated when the file system in saved.
	 */
	protected long savedMemoryAddress;
	
	/**
	 * The size of segments that the data is separated into.
	 * 
	 * Must be a multiple of 2 (i.e. 2, 4, 8, 16, 32, 64, 128)
	 */
	protected int segmentSize;
	
	/**
	 * Segments, each contains a portion of data.
	 */
	protected SegmentList<LFSSegment> segments;
	
	/**
	 * An array with all the segments recombined from the Segment Addresses.
	 */
	protected byte[] segmentedData;
	
	protected LFSChecksum checksum;
	
	/*
	 * CONSTRUCTORS 
	 */
	
	/**
	 * An empty constructor for use with {@link LFSType.load(Class<?>, byte[])}
	 */
	LFSType() {}
	
	/**
	 * 
	 * 
	 * @param name
	 */
	public LFSType(String name)
	{
		this.name = name;
		
		this.typeMetadata = new LFSTypeMetadata();
	}
	
	/**
	 * 
	 * 
	 * @param parentAddress
	 * @param name
	 */
	protected LFSType(long parentAddress, String name)
	{
		this(name);
		
		this.parentAddress = parentAddress;
	}
	
	/**
	 * 
	 * @param name
	 * @param childrenAddresses
	 */
	protected LFSType(String name, long...childrenAddresses)
	{
		this(name);
		
		this.childrenAddresses = childrenAddresses;
	}
	
	/**
	 * 
	 * @param parentAddress
	 * @param name
	 * @param childrenAddresses
	 */
	protected LFSType(long parentAddress, String name, long...childrenAddresses)
	{
		this(name);
		
		this.parentAddress = parentAddress;
		this.childrenAddresses = childrenAddresses;
	}
	
	
	/*
	 * METHODS
	 */
	
	/**
	 * 
	 * @param size
	 */
	public void setSegmentSize(int size)
	{
		this.segmentSize = size;
	}
	
	/**
	 * 
	 */
	public void combineSegments()
	{
		int totalSize = 0;
		
		List<byte[]> segments = new ArrayList<>();
		
		for(LFSSegment segment : this.segments)
		{
			byte[] data = segment.getData();
			
			segments.add(data);
			
			totalSize += data.length;
		}
		
		this.segmentedData = new byte[totalSize];
		
		int currentIndex = 0;
		
		for(byte[] segment : segments)
		{
			for(int i = 0; i < segment.length; i++)
			{
				this.segmentedData[currentIndex] = segment[i];
			}
			
			currentIndex += segment.length;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public SegmentList<LFSSegment> separateSegments(byte[] data)
	{
		List<byte[]> dataList = ByteUtil.split(data, this.segmentSize);
	
		SegmentList<LFSSegment> segments = new SegmentList<>();
		
		for(byte[] dat : dataList)
		{
			segments.add(new LFSSegment(dat));
		}
		
		return segments;
	}
	
	/**
	 * 
	 * @param out
	 */
	public void writeSegments(LFSTypeOutputStream out)
	{
		this.segments = this.separateSegments(this.segmentedData);
		
		int addrPosition = out.getCurrentPosition() + LFSTypeOutputStream.INT_SIZE;
		int position = addrPosition;
		int addressSpace = LFSTypeOutputStream.LONG_SIZE * segments.size();
		int afterAddrPos = addrPosition + addressSpace;
		
		long closestSegment = 0x00;
		
		//Write segments length/size
		out.writeInt(segments.size());
		for(LFSSegment segment : segments)
		{
			//Skip to empty space - away from segment address table, to be away from conflicts with segment memory addresses
			
			out.toPosition(afterAddrPos);
			
			//Find empty address with enough space.
			out.ifCurrentPositionAvailableThenSet(segment.getSize());
			long emptySegmentAddress = (long)out.getCurrentPosition();
			
			//Write segment to empty address
			out.writeArray(segment.getData(), false);
		
			long endOfSegmentAddress = (long)out.getCurrentPosition();
			
			if(endOfSegmentAddress > closestSegment)
				closestSegment = endOfSegmentAddress;
			
			//Go back to address map
			out.toPosition(position);
			
			//Write segment address.
			out.writeLong(emptySegmentAddress);
			
			//Update position
			position = out.getCurrentPosition();
		}
		
		//WriteChildren() overrides segment data. Detect segment data and prevent override.
		
		//Temporary fix?
		out.toPosition((int)closestSegment);
	}
	
	/**
	 * 
	 * @param in
	 */
	public void readSegments(LFSTypeInputStream in)
	{
		this.segments = new SegmentList<>();
		
		List<byte[]> segments = new ArrayList<>();
		int totalArraySize = 0;
		
		int segmentsSize = in.readInt();
		
		//Current Address of InputStream
		int currentAddress = in.getCurrentPosition();
		
		for(int segmentsIndex = 0; segmentsIndex < segmentsSize; segmentsIndex++)
		{
			//Segment's Address
			long segmentAddress = in.readLong();
			//Update current address
			currentAddress = in.getCurrentPosition();
			
			//Go to Segment Address
			in.toPosition((int)segmentAddress);
			
			//Read segment
			byte[] segment = in.readArray();
			
			//Increment final size of segmented data
			totalArraySize += segment.length;
			
			//Add segment to list
			segments.add(segmentsIndex, segment);
			
			createSegment(segmentAddress, segment);
			
			//Go back to address map to next segment address index.
			in.toPosition(currentAddress);
		}
		
		this.segmentedData = ByteUtil.merge(segments, totalArraySize);
	}
	
	private void createSegment(long segmentAddress, byte[] data)
	{
		LFSSegment segment = new LFSSegment(data);
		
		segment.setAddress(segmentAddress);
		
		this.segments.add(segment);
	}
	
	/**
	 * 
	 * @param out
	 */
	public void writeParent(LFSTypeOutputStream out)
	{
		if(this.parent == null)
		{
			//Doesn't have a parent.
			out.writeBoolean(false);
			return;
		}
		else
		{
			out.writeBoolean(true);
		}
		
		this.parentAddress = this.parent.parentAddress;
		
		out.writeLong(this.parentAddress);
	}
	
	public void readParent(LFSTypeInputStream in)
	{
		if(!in.readBoolean())
			return;
		
		this.parentAddress = in.readLong();
	}
	
	/**
	 * 
	 * @param out
	 */
	public void writeChildren(LFSTypeOutputStream out)
	{
		if(this.children == null || this.children.length == 0)
		{
			out.writeBoolean(false);
			return;
		}
		else
		{
			out.writeBoolean(true);
		}
		
		this.childrenAddresses = new long[this.children.length];
		
		for(int childIndex = 0; childIndex < this.children.length; childIndex++)
		{
			this.children[childIndex].save(out);
			this.childrenAddresses[childIndex] = this.children[childIndex].savedMemoryAddress;
		}
		
		int childrenAddressesLength = this.childrenAddresses.length;
		out.writeInt(childrenAddressesLength);
		
		for(long l : this.childrenAddresses)
		{
			out.writeLong(l);
		}
	}
	
	/**
	 * 
	 * @param in
	 */
	public void loadChildren(LFSTypeInputStream in)
	{
		if(!in.readBoolean())
			return;
		
		int childrenAddressesLength = in.readInt();
		
		this.childrenAddresses = new long[childrenAddressesLength];
		this.children = new LFSType[childrenAddressesLength];
		
		for(int childrenAddressesIndex = 0; childrenAddressesIndex < childrenAddressesLength; childrenAddressesIndex++)
		{
			long childrenAddress = in.readLong();
			
			this.childrenAddresses[childrenAddressesIndex] = childrenAddress;
			
			//Store current array position
			int currentPos = in.getCurrentPosition();
			
			//Go to address and load in segment.
			in.toPosition((int)childrenAddress + 1);
			byte[] childData = in.readArray();
			
			int type = in.read((int)childrenAddress);
			
			LFSType lfsType = null;
			
			switch(type)
			{
			case DRIVE:
				lfsType = LFSType.load(LFSDrive.class, childData);
				break;
			case DIRECTORY:
				lfsType = LFSType.load(LFSDirectory.class, childData);
				break;
			case FILE:
				lfsType = LFSType.load(LFSFile.class, childData);
				break;
			default:
				throw new UnsupportedOperationException();
			}
			
			this.children[childrenAddressesIndex] = lfsType;
			
			lfsType.parent = this;
			
			//Return to position.
			in.toPosition(currentPos);
		}
	}
	
	public void addChild(LFSType... child)
	{
		if(this.children == null || this.children.length < 0)
			this.children = new LFSType[0];
		
		//Create new array.
		LFSType[] children = new LFSType[this.children.length + child.length];
		
		//Clone existing array data.
		System.arraycopy(this.children, 0, children, 0, this.children.length);
		
		//Copy added children to array.
		System.arraycopy(child, 0, children, (children.length - 1), child.length);
		
		//Set children array to current + added.
		this.children = children;
	}
	
	public void overwrite(LFSSegment segment, byte[] newData)
	{
		this.overwrite(segment.getAddress(), newData);
	}
	
	public void overwrite(long segmentAddr, byte[] newData)
	{
		LFSSegment oldSegment = this.getSegment(segmentAddr);
		int index = this.segments.getIndex(oldSegment);
		
		if(oldSegment == null)
			return;
		
		int size = oldSegment.getSize();
		byte[] newSegData = new byte[size];
		
		int copySize = newData.length;
		
		if(copySize > size)
			copySize = size;
		
		System.arraycopy(newData, 0, newSegData, 0, copySize);
		
		LFSSegment newSegment = new LFSSegment(newSegData);
		
		this.segments.set(index, newSegment);
	}
	
	public void delete()
	{
		for(LFSSegment segment : this.segments)
		{
			this.overwrite(segment, new byte[0]);
		}
	}
	
	public void update(byte[] data)
	{
		this.segments = this.separateSegments(this.segmentedData);
		List<LFSSegment> updatedSegments = this.separateSegments(data);
		
		if(this.segments == null)
			this.segments = new SegmentList<>();
		
		if(updatedSegments == null)
			return;
		
		for(int index = 0; index < updatedSegments.size(); index++)
		{
			LFSSegment segment = null;
			
			if(index < this.segments.size())
			{
				segment = this.segments.get(index);
			}
			
			LFSSegment updatedSegment = updatedSegments.get(index);
			
			if(segment != null)
			{
				if(segment.equals(updatedSegment))
				{
					continue;
				}
			}
			
			this.segments.ensureCapacity(index);
			this.segments.set(index, updatedSegment);
		}
	}
	
	/*
	 * RETURN METHODS
	 */
	
	/**
	 * 
	 * @return
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * 
	 * @return
	 */
	public LFSTypeMetadata getMetadata()
	{
		return this.typeMetadata;
	}
	
	/**
	 * 
	 * @return
	 */
	public LFSType getParent()
	{
		return this.parent;
	}
	
	/**
	 * 
	 * @return
	 */
	public LFSType[] getChildren()
	{
		return this.children;
	}
	
	/**
	 * Get the saved Memory Address of the {@link LFSType}.
	 * 
	 * @return {@link long} - Saved Memory Address
	 */
	public long getSavedMemoryAddress()
	{
		return this.savedMemoryAddress;
	}
	
	public String getFullPath()
	{
		String path = this.getPath();
		
		//Append file name
		path += this.name;
		
		return path;
	}
	
	public String getPath()
	{
		String path = "";
		
		LFSType parent = this.parent;
		
		while(parent != null)
		{
			//Append parent file name (most likely a directory).
			path += parent.name + "/";
			
			parent = parent.parent;
		}
		
		return path;
	}
	
	public LFSSegment getSegment(long segmentAddr)
	{
		if(this.segments != null)
		{
			for(LFSSegment segment : this.segments)
			{
				if(segment.getAddress() == segmentAddr)
				{
					return segment;
				}
			}
		}
		
		return null;
	}
	
	/*
	 * STATIC METHODS
	 */
	
	/**
	 * Load the {@link LFSType} from its stored data.
	 * 
	 * @param type The type of LFS storage - {@link LFSType}
	 * @param data The input data exported from the saved {@link LFSType}.
	 * 
	 * @return {@link LFSType} instance represented by the input data.
	 */
	public static <T> LFSType load(Class<?> type, byte[] data)
	{
		if(LFSType.class.isAssignableFrom(type))
		{
			try
			{
				Constructor<?> instanceConstructor = type.getConstructor(String.class);
				instanceConstructor.setAccessible(true);
				
				LFSType instance = (LFSType) instanceConstructor.newInstance(new Object[] { "" });
				
				instance.load(new LFSTypeInputStream(data));
				
				return instance;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			throw new UnsupportedOperationException("Attempted to load LFSType with non-LFSType class.");
		}
		
		return null;
	}
	
	/*
	 * OVERRIDEN METHODS
	 */
	
	/**
	 * Load the {@link LFSType} from the specified {@link LFSTypeInputStream}.
	 * 
	 * @param in The input stream - {@link LFSTypeInputStream}.
	 */
	public void load(LFSTypeInputStream in)
	{
		this.readParent(in);
		
		this.name = in.readString();
		
		this.typeMetadata = new LFSTypeMetadata();
		this.typeMetadata.read(in);
		
		this.loadChildren(in);
	}
	
	/**
	 * Save the {@link LFSType} to the specified {@link LFSTypeOutputStream}.
	 * 
	 * @param out The output stream - {@link LFSTypeOutputStream}.
	 */
	public void save(LFSTypeOutputStream out)
	{
		this.savedMemoryAddress = out.getCurrentPosition();
	
		this.writeParent(out);
		
		out.writeString(this.name);
		
		this.typeMetadata.write(out);
		
		this.writeChildren(out);
	}
}
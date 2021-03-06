package lamp.filesystem;

import java.lang.reflect.Constructor;
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
import lamp.filesystem.util.LFSUtil;
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
	 * Drive Type Identifier.
	 */
	public static final int DRIVE = 0;
	
	/**
	 * Directory Type Identifier.
	 */
	public static final int DIRECTORY = 1;
	
	/**
	 * File Type Identifier.
	 */
	public static final int FILE = 2;
	
	/*
	 * FIELDS
	 */
	
	/**
	 * Identifier of this type.
	 */
	private int typeId;
		
	/**
	 * Path Identifier.
	 */
	private String pathIdentifier;
	
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
	 * 
	 */
	protected long[] segmentAddresses;
	
	/**
	 * An array with all the segments recombined from the Segment Addresses.
	 */
	protected byte[] segmentedData;
	
	/**
	 * Checksum of the type (drive, directory, file, etc.).
	 */
	protected LFSChecksum checksum;
	
	/*
	 * CONSTRUCTORS 
	 */
	
	/**
	 * An empty constructor for use with {@link LFSType.load(Class<?>, byte[])}
	 */
	LFSType() {}
	
	/**
	 * Default constructor with name of type (drive, directory, file, etc.)
	 * 
	 * @param name Name of Type
	 */
	public LFSType(String name)
	{
		this.name = name;
		
		this.typeMetadata = new LFSTypeMetadata();
	
		this.setPathId(name);
	}
	
	/**
	 * Constructor with parent's address and name of type.
	 * 
	 * @param parentAddress Parent's address
	 * @param name Name of Type
	 */
	protected LFSType(long parentAddress, String name)
	{
		this(name);
		
		this.parentAddress = parentAddress;
	}
	
	/**
	 * Constructor with Name of Type and children's addresses.
	 * 
	 * @param name Name of Type
	 * @param childrenAddresses Children's addresses
	 */
	protected LFSType(String name, long...childrenAddresses)
	{
		this(name);
		
		this.childrenAddresses = childrenAddresses;
	}
	
	/**
	 * Constructor with parent's address, Name of Type, children's addresses.
	 * 
	 * @param parentAddress Parent's address
	 * @param name Name of Type
	 * @param childrenAddresses Children's address
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
	 * Set the size of data per segment (memory address area).
	 * 
	 * @param size Size of data per segment
	 */
	public void setSegmentSize(int size)
	{
		this.segmentSize = size;
	}
	
	/**
	 * Combines the segments into a long byte array. 
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
	 * Separates a long byte array into segments of data.
	 * 
	 * @param data Byte array to separate
	 * 
	 * @return Separated list of segments.
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
	 * Write the segments of data into the {@link LFSTypeOutputStream} (Output Stream).
	 * 
	 * @param out {@link LFSTypeOutputStream} OutputStream to output data into.
	 */
	public void writeSegments(LFSTypeOutputStream out)
	{
		this.segments = this.separateSegments(this.segmentedData);
		this.segmentAddresses = new long[this.segments.size()];
		
		long closestSegment = 0x00;
		
		//Write segment size
		out.writeInt(this.segmentSize);
		
		//Write segments length/size
		out.writeInt(segments.size());
		
		int addrPosition = out.getCurrentPosition();
		//int position = addrPosition;
		int addressSpace = ByteUtil.LONG_SIZE * segments.size();
		int afterAddrPos = addrPosition + addressSpace;
		
		out.toPosition(afterAddrPos);
		
		for(int segmentIndex = 0; segmentIndex < segments.size(); segmentIndex++)
		{
			LFSSegment segment = segments.get(segmentIndex);
			
			//Skip to empty space - away from segment address table, to be away from conflicts with segment memory addresses
			
			//out.toPosition(position);
			
			//Find empty address with enough space.
			out.ifCurrentPositionAvailableThenSet(segment.getSize());
			long emptySegmentAddress = (long)out.getCurrentPosition();
			
			this.segmentAddresses[segmentIndex] = emptySegmentAddress;
			
			//Write segment to empty address
			out.writeArray(segment.getData(), false);
		
			long endOfSegmentAddress = (long)out.getCurrentPosition();
			
			if(endOfSegmentAddress > closestSegment)
				closestSegment = endOfSegmentAddress;
			
			//Update position
			//position = out.getCurrentPosition();
		}
		
		//TODO: WriteChildren() overrides segment data. Detect segment data and prevent override.
		
		//Write Addresses.
		out.toPosition(addrPosition);

		for(int segmentAddressIndex = 0; segmentAddressIndex < this.segmentAddresses.length; segmentAddressIndex++)
		{
			out.writeLong(this.segmentAddresses[segmentAddressIndex]);
		}
		//End Write Addresses.
		
		//Temporary fix?
		out.toPosition((int)closestSegment);
	}
	
	/**
	 * Read segments from the specified InputStream ({@link LFSTypeInputStream}).
	 * 
	 * @param in InputStream ({@link LFSTypeInputStream})
	 */
	public void readSegments(LFSTypeInputStream in)
	{
		this.segments = new SegmentList<>();
		
		List<byte[]> segments = new ArrayList<>();
		int totalArraySize = 0;
		
		//Read segment size
		this.segmentSize = in.readInt();
		
		int segmentsSize = in.readInt();
		
		this.segmentAddresses = new long[segmentsSize];
		
		int addrPosition = in.getCurrentPosition();
		int addressSpace = ByteUtil.LONG_SIZE * segments.size();
		int afterAddrPos = addrPosition + addressSpace;
		
		for(int segmentAddrIndex = 0; segmentAddrIndex < segmentsSize; segmentAddrIndex++)
		{
			this.segmentAddresses[segmentAddrIndex] = in.readLong();
		}
		
		for(int segmentIndex = 0; segmentIndex < segmentsSize; segmentIndex++)
		{
			long segmentAddress = this.segmentAddresses[segmentIndex];
			
			in.toPosition((int)segmentAddress);
			
			byte[] segmentData = in.readArray();
			
			totalArraySize += segmentData.length;
			
			segments.add(segmentIndex, segmentData);
			
			createSegment(segmentAddress, segmentData);
		}
		
		in.toPosition(afterAddrPos);
		
		this.segmentedData = ByteUtil.merge(segments, totalArraySize);
		
		/*
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
		*/
	}
	
	/**
	 * Creates a segment for the specified memory address, with the provided byte array (data).
	 * 
	 * @param segmentAddress New Segment Address
	 * @param data New Byte Array (data)
	 */
	private void createSegment(long segmentAddress, byte[] data)
	{
		LFSSegment segment = new LFSSegment(data);
		
		segment.setAddress(segmentAddress);
		
		this.segments.add(segment);
	}
	
	/**
	 * Write parent to the provided OutputStream ({@link LFSTypeOutputStream}).
	 * 
	 * @param out OutputStream ({@link LFSTypeOutputStream})
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
	
	/**
	 * Read parent from the provided InputStream ({@link LFSTypeInputStream}).
	 * 
	 * @param in InputStream ({@link LFSTypeInputStream})
	 */
	public void readParent(LFSTypeInputStream in)
	{
		if(!in.readBoolean())
			return;
		
		this.parentAddress = in.readLong();
	}
	
	/**
	 * Write children to the provided OutputStream ({@link LFSTypeOutputStream}).
	 * 
	 * @param out OutputStream ({@link LFSTypeOutputStream})
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
		
		out.writeInt(this.children.length);
		
		this.childrenAddresses = new long[this.children.length];
		
		int addrLoc = out.getCurrentPosition();
		int endOfAddressLoc = addrLoc + (this.children.length * ByteUtil.LONG_SIZE);
		
		out.toPosition(endOfAddressLoc);
		
		for(int childIndex = 0; childIndex < this.children.length; childIndex++)
		{
			out.write((byte)this.children[childIndex].typeId);
			
			this.children[childIndex].save(out);
			
			System.out.println("Child Address: " + this.children[childIndex].savedMemoryAddress);
			
			this.childrenAddresses[childIndex] = this.children[childIndex].savedMemoryAddress;
		}
		
		out.toPosition(addrLoc);
		for(long l : this.childrenAddresses)
		{
			System.out.println("Child Addresse: " + l);
			
			out.writeLong(l);
		}
	}
	
	/**
	 * Load children from the provided InputStream ({@link LFSTypeInputStream}).
	 * 
	 * @param in InputStream ({@link LFSTypeInputStream})
	 */
	public void loadChildren(LFSTypeInputStream in)
	{
		if(!in.readBoolean())
			return;
		
		int childrenLength = in.readInt();
		
		this.children = new LFSType[childrenLength];
		this.childrenAddresses = new long[childrenLength];
		
		for(int childAddrIndex = 0; childAddrIndex < childrenLength; childAddrIndex++)
		{
			this.childrenAddresses[childAddrIndex] = in.readLong();
		}
		
		for(int childIndex = 0; childIndex < childrenLength; childIndex++)
		{
			long childAddress = this.childrenAddresses[childIndex];
		
			System.out.println("Address: " + childAddress);
			
			in.toPosition((int)childAddress);
			
			int type = in.read((int)childAddress - 1);
			
			System.out.println("Type: " + type);
			
			Class<?> childLfsTypeClass = null;
			
			switch(type)
			{
			case DRIVE:
				childLfsTypeClass = LFSDrive.class;
				break;
			case DIRECTORY:
				childLfsTypeClass = LFSDirectory.class;
				break;
			case FILE:
				childLfsTypeClass = LFSFile.class;
				break;
			default:
				throw new UnsupportedOperationException();
			}

			LFSType childLfsType = LFSType.load(childLfsTypeClass, in);
		
			this.children[childIndex] = childLfsType;
			
			childLfsType.parent = this;
		}
		
		/*
		for(int childIndex = 0; childIndex < childrenLength; childIndex++)
		{
			long childAddress = in.readLong();
			System.out.println(childAddress);
			
			this.childrenAddresses[childIndex] = childAddress;
			
			//Store current array position
			int currentPos = in.getCurrentPosition();
			
			//Go to address and load in segment.
			in.toPosition((int)childAddress);
			byte[] childData = in.readArray();
			
			System.out.println("Data: " + Dump.printHex(childData));
			
			int type = (int)in.read((int)childAddress);
			
			System.out.println("Type: " + type);
			
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
			
			this.children[childIndex] = lfsType;
			
			lfsType.parent = this;
			
			//Return to position.
			in.toPosition(currentPos);
		}
		*/
	}
	
	/**
	 * Add specific child(ren) to this type.
	 * 
	 * @param child Child(ren) types to add.
	 */
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
	
	/**
	 * Remove specific child(ren) from this type.
	 * 
	 * @param child Child(ren) types to remove.
	 */
	public void removeChild(LFSType... child)
	{
		if(this.children == null || this.children.length < 0)
			this.children = new LFSType[0];
		
		if(child.length > this.children.length)
			return;
		
		LFSType[] children = new LFSType[this.children.length - child.length];
		int childIndex = 0;
		
		int chIndex = 0;
		
		for(int index = 0; index < this.children.length; index++)
		{
			if(child.length > chIndex)
			{
				break;
			}
			
			if(child[chIndex++] == null)
			{
				break;
			}
			
			LFSType c = this.children[index];
			
			if(!c.equals(child[chIndex]))
			{
				children[childIndex] = c;
			}
		}
		
		this.children = children;
	}
	
	/**
	 * Overwrite a segment of data, with new data.
	 * 
	 * @param segment Segment ({@link LFSSegment})
	 * @param newData Updated byte array (data)
	 */
	public void overwrite(LFSSegment segment, byte[] newData)
	{
		this.overwrite(segment.getAddress(), newData);
	}
	
	/**
	 * Overwrite a segment of data, at a specific address, with new data.
	 * 
	 * @param segmentAddr Segment address
	 * @param newData Updated byte array (data)
	 */
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
	
	/**
	 * Delete all the data associated with the type, by overwriting all the segments with null bytes.
	 */
	public void delete()
	{
		if(this.segments == null)
		{
			return;
		}
		
		for(LFSSegment segment : this.segments)
		{
			this.overwrite(segment, new byte[this.segments.size() * this.segmentSize]);
		}
	}
	
	/**
	 * Update the segments of data of the type.
	 * 
	 * @param data Updated byte array (data)
	 */
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
	
	protected void setTypeId(int typeId)
	{
		this.typeId = typeId;
	}
	
	protected void setPathId(String pathId)
	{
		this.pathIdentifier = pathId;
	}
	
	/*
	 * RETURN METHODS
	 */
	
	/**
	 * Get the name of the type.
	 * 
	 * @return Type name
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Get the metadata of this type.
	 * 
	 * @return Type metadata
	 */
	public LFSTypeMetadata getMetadata()
	{
		return this.typeMetadata;
	}
	
	/**
	 * Get the parent type of this type.
	 * 
	 * @return Parent type
	 */
	public LFSType getParent()
	{
		return this.parent;
	}
	
	/**
	 * Get the children type(s) of this type.
	 * 
	 * @return Children type(s)
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
	
	/**
	 * Get the root parent (first parent) type in the hierarchy.
	 * 
	 * @return Root parent ({@link LFSType}).
	 */
	public LFSType getRootParent()
	{
		if(this.parent == null)
			return this;
		
		LFSType parent = this.parent;
		
		while(parent.parent != null)
		{
			parent = parent.parent;
		}
		
		return parent;
	}
	
	/**
	 * Get the parent path(s) of the type (drive, directory, file, etc.) with the name of the type.
	 * 
	 * @return Parent path(s) with the name of the type.
	 */
	public String getFullPath()
	{
		String path = this.getPath();
		
		//Append file name
		path += this.name;
		
		return path;
	}
	
	/**
	 * Get the parent path(s) of the type (drive, directory, file, etc.) without the name of the type.
	 * 
	 * @return Parent path(s)
	 */
	public String getPath()
	{
		String path = "";
		
		LFSType parent = this.parent;
		List<LFSType> parentList = new ArrayList<>();
		
		while(parent != null)
		{
			parentList.add(parent);
			
			parent = parent.parent;
		}
		
		for(LFSType p : LFSUtil.reverseTypeList(parentList))
		{
			//Append parent file name (most likely a directory).
			path += p.pathIdentifier + LFS.FILE_SEPARATOR;
		}
		
		return path;
	}
	
	/**
	 * Get a segment of this type at the specified segment address.
	 * 
	 * @param segmentAddr Segment Address
	 * 
	 * @return Segment of Data ({@link LFSSegment}).
	 */
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
	public static <T> LFSType load(Class<?> type, LFSTypeInputStream in)
	{
		if(LFSType.class.isAssignableFrom(type))
		{
			try
			{
				Constructor<?> instanceConstructor = type.getConstructor(String.class);
				instanceConstructor.setAccessible(true);
				
				LFSType instance = (LFSType) instanceConstructor.newInstance(new Object[] { "" });
				
				instance.load(in);
				
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
		this.setPathId(this.name);
		
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
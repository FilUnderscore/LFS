/*
 * lfstypeinputstream.cpp
 *
 *  Created on: 4/10/2017
 *      Author: filip
 */

#include "lfstypeinputstream.h"
#include "../../util/bytebuf.h"

bool lfstypeinputstream::readBoolean()
{
	void* array = this->readArray(BOOLEAN_SIZE);

	bytebuf bbuf = bytebuf::convert(array);
	bbuf.swap(endianness);

	char n = bbuf.get();

	bool result = (n == 0 ? false : true);

	return result;
}

int lfstypeinputstream::readInt()
{
	void* array = this->readArray(INT_SIZE);

	bytebuf bbuf = bytebuf::convert(array);
	bbuf.swap(endianness);

	int n = bbuf.getInt();

	return n;
}

long lfstypeinputstream::readLong()
{
	void* array = this->readArray(LONG_SIZE);

	bytebuf bbuf = bytebuf::convert(array);
	bbuf.swap(endianness);

	long n = bbuf.getLong();

	return n;
}

string lfstypeinputstream::readString()
{
	int arrayLength = this->readInt();

	char* array = (char*)this->readArray(arrayLength);

	string str(array);

	return str;
}

void* lfstypeinputstream::readArray()
{
	int arrayLength = this->readInt();

	return this->readArray(arrayLength);
}

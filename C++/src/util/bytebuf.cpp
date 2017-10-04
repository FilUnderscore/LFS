/*
 * bytebuf.cpp
 *
 *  Created on: 4/10/2017
 *      Author: Filip Jerkovic
 */

#include "bytebuf.h"
#include <cstdlib>
#include "endian.h"

bytebuf::bytebuf(void* buffer)
{
	this->buffer = vector<char>((char*)buffer, (char*)buffer + sizeof(buffer));
	this->determineEndianness();
}

bytebuf::bytebuf(int size)
{
	this->buffer = vector<char>(size);
	this->determineEndianness();
}

bytebuf bytebuf::convert(void* buffer)
{
	return new bytebuf(buffer);
}

bytebuf bytebuf::create(int size)
{
	return new bytebuf(size);
}

void bytebuf::determineEndianness()
{
	// https://stackoverflow.com/a/4240014
	short int number = 0x1;
	char *numPtr = (char*)&number;
    if(numPtr[0] == 1)
    {
    	this->endianness = endian::LITTLE;
    }
    else
    {
    	this->endianness = endian::BIG;
    }
}

void bytebuf::putInt(int i)
{
	this->putInt(this->buffer.size(), i);
}

void bytebuf::putInt(int index, int i)
{
	this->buffer.at(index) = (i >> 24) & 0xFF;
	this->buffer.at(index + 1) = (i >> 16) & 0xFF;
	this->buffer.at(index + 2) = (i >> 8) & 0xFF;
	this->buffer.at(index + 3) = i & 0xFF;
}

void bytebuf::putLong(int index, long l)
{
	this->buffer.at(index) = (l >> 56) & 0xFFFFFFFF;
	this->buffer.at(index + 1) = (l >> 48) & 0xFFFFFFFF;
	this->buffer.at(index + 2) = (l >> 40) & 0xFFFFFFFF;
	this->buffer.at(index + 3) = (l >> 32) & 0xFFFFFFFF;
	this->buffer.at(index + 4) = (l >> 24) & 0xFFFFFFFF;
	this->buffer.at(index + 5) = (l >> 16) & 0xFFFFFFFF;
	this->buffer.at(index + 6) = (l >> 8) & 0xFFFFFFFF;
	this->buffer.at(index + 7) = l & 0xFFFFFFFF;
}

void bytebuf::putLong(long l)
{
	this->putLong(this->buffer.size(), l);
}

long bytebuf::getLong(int index)
{
	return (this->buffer.at(index) << 56) | (this->buffer.at(index + 1) << 48) | (this->buffer.at(index + 2) << 40) | (this->buffer.at(index + 3) << 32) | (this->buffer.at(index + 4) << 24) | (this->buffer.at(index + 5) << 16) | (this->buffer.at(index + 6) << 8) | (this->buffer.at(index + 7));
}

long bytebuf::getLong()
{
	return getLong(0);
}

int bytebuf::getInt()
{
	return getInt(0);
}

char bytebuf::get()
{
	return this->get(0);
}

char bytebuf::get(int index)
{
	return this->buffer.at(index);
}

int bytebuf::getInt(int index)
{
	return (this->buffer.at(index) << 24) | (this->buffer.at(index + 1) << 16) | (this->buffer.at(index + 2) << 8) | (this->buffer.at(index + 3));
}

void bytebuf::swap(int endianness)
{
	if(endianness == this->endianness)
		return;

	char* buffer = this->buffer.data();

	//Allocate temporary buffer
	char* tempBuffer = (char*)malloc(sizeof(buffer));

	//Reverse values in buffer to new buffer.
	for(int bufferLen = 0; bufferLen < sizeof(buffer); bufferLen++)
	{
		//Reverse array.
		tempBuffer[bufferLen] = buffer[sizeof(buffer) - bufferLen];
	}

	//Replace old values in buffer with new.
	for(int bufferLen = 0; bufferLen < sizeof(tempBuffer); bufferLen++)
	{
		buffer[bufferLen] = tempBuffer[bufferLen];
	}

	this->buffer = vector<char>(buffer, buffer + sizeof(buffer));

	//Update endian.
	if(this->endianness == endian::BIG)
		this->endianness = endian::LITTLE;
	else
		this->endianness = endian::BIG;
}

int bytebuf::getEndianness()
{
	return this->endianness;
}

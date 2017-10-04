/*
 * lfstypeinputstream.cpp
 *
 *  Created on: 4/10/2017
 *      Author: filip
 */

#include "lfsinputstream.h"
#include <cstdlib>

lfsinputstream::lfsinputstream(void* buffer)
{
	this->buffer = buffer;
}

char lfsinputstream::read(int position)
{
	this->position = position;

	char* buffer = (char*)this->buffer;

	if(this->position < sizeof(buffer))
	{
		return buffer[this->position];
	}
	else
	{
		return 0;
	}
}

char lfsinputstream::read()
{
	return this->read(this->position);
}

void* lfsinputstream::readArray(int length)
{
	char* array = (char*)malloc(length);

	for(int arrayIndex = 0; arrayIndex < length; arrayIndex++)
	{
		array[arrayIndex] = read();
	}

	return (void*)array;
}

void* lfsinputstream::readArray(int position, int length)
{
	this->position = position;

	return this->readArray(length);
}

void lfsinputstream::toPosition(int position)
{
	this->position = position;
}

int lfsinputstream::getCurrentPosition()
{
	return this->position;
}

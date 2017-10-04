/*
 * lfsoutputstream.cpp
 *
 *  Created on: 4/10/2017
 *      Author: Filip Jerkovic
 */

#include "lfsoutputstream.h"

lfsoutputstream::lfsoutputstream() : lfsoutputstream(0)
{
}

lfsoutputstream::lfsoutputstream(int startingSize)
{
	this->buffer = vector<char>(startingSize);
}

void lfsoutputstream::write(void* data, int offset, int length)
{
	this->write(this->position, data, offset, length);
}

void lfsoutputstream::write(int position, void* data, int offset, int length)
{
	this->position = position;

	char* dat = (char*) data;

	for(int index = offset; index < length; index++)
	{
		this->buffer.at(this->position) = dat[index];
		this->position++;
	}
}

int lfsoutputstream::getCurrentPosition()
{
	return this->position;
}

void* lfsoutputstream::getBuffer()
{
	return (void*)this->buffer.data();
}

void lfsoutputstream::ifCurrentPositionAvailableThenSet()
{
	int currentPos = this->position;

	int count = 0;

	while(count < POSITION_FREE_COUNT)
	{
		currentPos++;

		if(this->buffer.at(currentPos) != 0)
		{
			continue;
		}
		else
		{
			count++;
		}
	}

	this->position = currentPos;
}

void lfsoutputstream::toPosition(int position)
{
	this->position = position;
}

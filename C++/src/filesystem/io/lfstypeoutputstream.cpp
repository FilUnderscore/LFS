/*
 * lfstypeoutputstream.cpp
 *
 *  Created on: 4/10/2017
 *      Author: filip
 */

#include "lfstypeoutputstream.h"

void lfstypeoutputstream::writeBoolean(bool b)
{

}

void lfstypeoutputstream::writeInt(int i)
{

}

void lfstypeoutputstream::writeLong(long l)
{

}

void lfstypeoutputstream::writeString(string str)
{

}

void lfstypeoutputstream::writeArray(void* array)
{
	char* arr = (char*)array;

	this->writeInt(sizeof(arr));
	this->write(array, 0, sizeof(arr));
}

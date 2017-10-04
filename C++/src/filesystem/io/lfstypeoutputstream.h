/*
 * lfstypeoutputstream.h
 *
 *  Created on: 4/10/2017
 *      Author: Filip Jerkovic
 */

#include "../../io/lfsoutputstream.h"
#include <string>

using namespace std;

#ifndef FILESYSTEM_IO_LFSTYPEOUTPUTSTREAM_H_
#define FILESYSTEM_IO_LFSTYPEOUTPUTSTREAM_H_

class lfstypeoutputstream : public lfsoutputstream
{
	public:
		static const int BOOLEAN_SIZE = 1;
		static const int SHORT_SIZE = 2;
		static const int INT_SIZE = 4;
		static const int LONG_SIZE = 8;

		void writeBoolean(bool b);

		void writeInt(int i);

		void writeLong(long l);

		void writeString(string str);

		void writeArray(void* array);
};

#endif /* FILESYSTEM_IO_LFSTYPEOUTPUTSTREAM_H_ */

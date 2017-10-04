/*
 * lfstypeinputstream.h
 *
 *  Created on: 4/10/2017
 *      Author: filip
 */

#ifndef FILESYSTEM_IO_LFSTYPEINPUTSTREAM_H_
#define FILESYSTEM_IO_LFSTYPEINPUTSTREAM_H_

#include "../../io/lfsinputstream.h"
#include <string>

#include "../../util/endian.h"

using namespace std;

class lfstypeinputstream : public lfsinputstream
{
	public:
		static const int BOOLEAN_SIZE = 1;
		static const int SHORT_SIZE = 2;
		static const int INT_SIZE = 4;
		static const int LONG_SIZE = 8;

		bool readBoolean();

		int readInt();

		long readLong();

		string readString();

		void* readArray();
	private:
		static const int endianness = endian::BIG;
};

#endif /* FILESYSTEM_IO_LFSTYPEINPUTSTREAM_H_ */

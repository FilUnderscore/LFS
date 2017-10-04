/*
 * bytebuf.h
 *
 *  Created on: 4/10/2017
 *      Author: Filip Jerkovic
 */

#ifndef UTIL_BYTEBUF_H_
#define UTIL_BYTEBUF_H_

#include <vector>

using namespace std;

class bytebuf
{
	public:
		static bytebuf convert(void* buffer);

		static bytebuf create(int size);

		bytebuf(void* buffer);

		bytebuf(int size);

		~bytebuf();

		void swap(int endianness);

		void* getBuffer();

		int getEndianness();

		void putInt(int i);

		void putInt(int index, int i);

		int getInt();

		int getInt(int index);

		long getLong();

		long getLong(int index);

		void putLong(long l);

		void putLong(int index, long l);

		char get();

		char get(int index);

	protected:
		vector<char> buffer;

		int endianness;

		void determineEndianness();
};

#endif /* UTIL_BYTEBUF_H_ */

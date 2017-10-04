/*
 * lfsoutputstream.h
 *
 *  Created on: 4/10/2017
 *      Author: filip
 */

#ifndef IO_LFSOUTPUTSTREAM_H_
#define IO_LFSOUTPUTSTREAM_H_

#include <vector>

using namespace std;

class lfsoutputstream
{
	public:
		static const int POSITION_FREE_COUNT = 16;

		lfsoutputstream();

		lfsoutputstream(int startingSize);

		void write(void* data, int offset, int length);

		void write(int position, void* data, int offset, int length);

		int getCurrentPosition();

		void* getBuffer();

		/**
		 * Need a better name...
		 */
		void ifCurrentPositionAvailableThenSet();

		void toPosition(int position);

	protected:
		vector<char> buffer;

		int position;
};

#endif /* IO_LFSOUTPUTSTREAM_H_ */

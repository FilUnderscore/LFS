/*
 * lfsinputstream.h
 *
 *  Created on: 4/10/2017
 *      Author: filip
 */

#ifndef IO_LFSINPUTSTREAM_H_
#define IO_LFSINPUTSTREAM_H_

class lfsinputstream
{
	protected:
		void* buffer;

		int position;

	public:
		lfsinputstream(void* buffer);
		~lfsinputstream();

		char read(int position);

		char read();

		void* readArray(int length);

		void* readArray(int position, int length);

		void toPosition(int position);

		int getCurrentPosition();
};

#endif /* IO_LFSINPUTSTREAM_H_ */

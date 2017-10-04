/*
 * lfstypemetadata.h
 *
 *  Created on: 4/10/2017
 *      Author: filip
 */

#ifndef FILESYSTEM_LFSTYPEMETADATA_H_
#define FILESYSTEM_LFSTYPEMETADATA_H_

class lfstypemetadata
{
	public:
		int flags;

		long created;
		long lastModified;

		void write();

		void read();
};

#endif /* FILESYSTEM_LFSTYPEMETADATA_H_ */

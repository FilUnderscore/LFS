/*
 * lfstype.h
 *
 *  Created on: 4/10/2017
 *      Author: filip
 */

#ifndef FILESYSTEM_LFSTYPE_H_
#define FILESYSTEM_LFSTYPE_H_

#include <string>
#include <map>
#include <vector>

#include <lfstypemetadata.h>

#include <lfstypeinputstream.h>
#include <lfstypeoutputstream.h>

using namespace std;

class lfstype
{
	public:
		static const int DRIVE = 0;
		static const int DIRECTORY = 1;
		static const int FILE = 2;

		lfstype(string name, lfstypemetadata typeMetadata);

		~lfstype();

		void setSegmentSize(int segmentSize);

		void combineSegments();

		vector<void*> separateSegments();

		void writeSegments(lfstypeoutputstream out);

		void readSegments(lfstypeinputstream in);

		void writeParent(lfstypeoutputstream out);

		void readParent(lfstypeinputstream in);

		void writeChildren(lfstypeoutputstream out);

		void loadChildren(lfstypeinputstream in);

		string getName();

		lfstypemetadata getMetadata();

		lfstype getParent();

		lfstype* getChildren();

		long getSavedMemoryAddress();

		template<class type>
		static type load(type type, void* data);

		void load(lfstypeinputstream in);

		void save(lfstypeoutputstream out);

	protected:
		string name;

		lfstypemetadata typeMetadata;

		lfstype parent;

		long parentAddress;

		lfstype* children;

		long* childrenAddresses;

		long savedMemoryAddress;

		int segmentSize;

		map<long, void*> segmentAddresses;

		void* segmentedData;

		lfstype(long parentAddress, string name, lfstypemetadata typeMetadata);

		lfstype(string name, lfstypemetadata typeMetadata, long* childrenAddresses);

		lfstype(long parentAddress, string name, lfstypemetadata, long* childrenAddresses);

	private:
		lfstype();
		~lfstype();
};

#endif /* FILESYSTEM_LFSTYPE_H_ */

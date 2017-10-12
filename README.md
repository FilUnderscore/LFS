# Lamp File System

## What is it?

The Lamp File System is an efficient filesystem that minimizes the risk of data loss without reason (e.g. corruption) by using different checksum techniques (such as SHA-1, MD5), and notifying users if a file failed verification.

## How do the checksums work?

The checksums are updated accordingly, when a file is updated. It stores a hidden file of a few bytes with the different checksums of the file.

If most of the checksums don't match, then the file is invalid and possible data loss has occurred or the update hasn't been verified.

## How you can contribute?

Feel free to test out many of the functions implemented in the code, if you find bugs - report them. If you fix any that you encounter, create a pull request. If you have any new feature requests or ideas, feel free to let us know, or help implement it! Help this project become bigger and have more dedicated people working on it.

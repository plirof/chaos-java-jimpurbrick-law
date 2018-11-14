// Law, a multi-player strategy game written as a Java applet based 
// on the classic ZX Spectrum game Chaos by Julian Gallop.
// Copyright (C) 1997-2002 Jim Purbrick
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package law;

/**
*	Bit class
*
*	Project: Chaos
*	Author:	 James Purbrick (jcp@cs.nott.ac.uk)
*	Last Revision:	1/7/97
*
*	Notes:
*	Provides methods to extract regions of bits from integers.
*	Treats integers as an array of bits: indexed from 0 to 
*	length - 1 as shown below.
*
*	Bit 0			       Bit 31
*	|                              |
*	10011001101011110010010011001110
*	|______|                |______|
*	 Byte 0                  Byte 3
*
*/

class Bit
{
	private static final int INTLENGTH  = 32;
	private static final int BYTELENGTH = 8;
	private static final int MAXINDEX   = 31;
	private static final int MININDEX   = 0;
	
	// Returns the bit at a given offset.
	public static boolean getBit(int data, int offset)
	{
		return getRegion(data,offset,offset) == 1;
	}
	
	// Returns the byte at given offset.
	public static int getByte(int data, int offset)
	{
		switch(offset)
		{
			case 0:
				return (data & 0xff000000) / 16777216;
			case 1:
				return (data & 0x00ff0000) / 65536;
			case 2:
				return (data & 0x0000ff00) / 256;
			case 3:
				return data & 0x000000ff;
		}
		return 0;
		
		// return getRegion(data,offset * BYTELENGTH, offset * BYTELENGTH + 7);
	}
	
	// Returns an integer representing the bits between start
	// and end inclusive.
	public static int getRegion(int data, int start, int end)
	{
		//int mask = 0; // An integer with 1's in region.
		
		// Check indecies are in bounds.
		if( start < MININDEX || end > MAXINDEX || start > end)
			throw new ArrayIndexOutOfBoundsException();
		
		/*	
		for(int counter = 0; counter < MAXINDEX - start; counter++)
		{
			if(counter < end - start) mask++;
			mask = mask << 1;
		}
		
		
		return data & mask;
		*/
		
		// Calculate value of region through division & remainder.
		return (int) ((data / Math.pow(2,INTLENGTH - end)) % Math.pow(2,end - start + 1));
	}
	
}	

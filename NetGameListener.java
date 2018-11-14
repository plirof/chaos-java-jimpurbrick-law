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

//-----------------------------------------------------------------------
//	NetGame
//
//	Project: Chaos
//	Author:	 James Purbrick (jcp@cs.nott.ac.uk)
//	Last Revision:	1/7/97
//
//	Notes:
//	An interface that defines all the callback routines used by NetGame.
//-----------------------------------------------------------------------

package law;

interface NetGameListener
{
	public void handleCommand(String command);
	
	public void startGame(int randomSeed);
	
	public void addPlayer(int Number,String name,boolean local);
	
	public void removePlayer(int Number);
	
	public void nextTurn();
}

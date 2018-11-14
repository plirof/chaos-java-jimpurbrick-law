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

//-------------------------------------------------------------------
//	Square
//
//	Project: Chaos
//	Author:	 James Purbrick (jcp@cs.nott.ac.uk)
//	Last Revision:	18/6/97
//
//	Notes:
//	A class that represents a square on the chaos gameboard.
//	It contains the creature and the stack of corpses on the 
//	square.
//-------------------------------------------------------------------

package law;

// Import java class libraries.
import java.util.*;
import java.awt.*;

/**
 *	A class that represents a square on the chaos gameboard.
 *	It contains the creature and the stack of corpses on the 
 *	square.
 */

public class Square implements Data
{
	// Define member variables.
	private int x,y; 		// The co-ordinates of this square.
	private Creature occupant; // The living occupier of the square.
	private Vector corpses; // The remaining corpses.
	
	// Constructor.
	public Square(int x, int y)
	{
		this.x = x;
		this.y = y;
		corpses = new Vector();
	}
	
	public synchronized void pushCorpse(Creature c)
	{
		corpses.addElement(c);
	}
	
	public synchronized void popCorpse()
	{
		try
		{
			corpses.removeElement(corpses.lastElement());
		}
		catch(NoSuchElementException e)
		{
		}
	}
	
	public Creature topCorpse()
	{
		try
		{
			return (Creature) corpses.firstElement();
		}
		catch(NoSuchElementException e)
		{
			return null;
		}
	}
	
	public int getx()
	{
		return x;
	}
	
	public int gety()
	{
		return y;
	}
		
	public synchronized void setOccupant(Creature c)
	{
		occupant = c;
	}
	
	public Creature getOccupant()
	{
		return occupant;
	}
	
	// Used to paint the square when it's been covered.
	public synchronized void paint(Graphics g)
	{
		if(occupant != null) 
		{ 
			// Paint the occupant's current frame.
			occupant.paint(g);
		}	
		else if(corpses.size() > 0) 
		{
			// Paint the corpse on the top of the pile.
			((Creature) corpses.firstElement()).corpsePaint(g);
		}
		else 
		{			
			g.setColor(Color.black);
			g.fillRect(x * SQUAREWIDTH, y * SQUAREHEIGHT,SQUAREWIDTH,SQUAREHEIGHT);
		}
	}
	
	// If there is a live occupant, update their frame and paint them.
	// Used when the square needs to be animated.
	public synchronized void updatePaint(Graphics g)
	{
		if(occupant != null) 
		{
			occupant.updateFrame();
			occupant.paint(g);
		}
	}
	
	public boolean adjacent(Square pos)
  {
      if (range(pos) != 1)
          return false;
      else
          return true;
  }
  
  public int range(Square pos)
  {
      return Math.max(Math.abs(x - pos.getx()), Math.abs(y - pos.gety()));
  }

  public int range(int i, int j)
  {
      return Math.max(Math.abs(getx() - i), Math.abs(gety() - j));
  }
  	
} // End class Square.

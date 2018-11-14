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

import java.awt.*;
import java.util.*;

//-------------------------------------------------------------------
//	GameBoard
//
//	Project: Chaos
//	Author:	 James Purbrick (jcp@cs.nott.ac.uk)
//	Last Revision:	30/6/97
//
//	Notes:

/**
 *  A class that contains a 15x10 array of squares that represents 
 *  a game board in chaos. It supports various searching operations
 *  such as returning the creatures adjacent to a square, finding
 *  a line of sight etc.
 */

public class GameBoard implements Data
{
	// Define member variables.
	private Square squares[][] = new Square[NOSQUARESWIDE][NOSQUARESHIGH];
	
	//------------------------------------------------------------
	//	Constructor.
	//------------------------------------------------------------
	
	GameBoard()
	{
		// Create squares.
		for(int x=0; x < NOSQUARESWIDE; x++)
		{
			for(int y=0; y < NOSQUARESHIGH; y++) squares[x][y] = new Square(x,y);
		}	
	}
	
	//------------------------------------------------------------
	//	Access methods.
	//------------------------------------------------------------
	
	public Square getSquare(int x, int y)
	{
		return squares[x][y];
	}
	
	public Square getSquareFromPixel(int i, int j)
  {
		// Return the calculated square.
    return squares[i / SQUAREWIDTH][j / SQUAREHEIGHT];
  }
	
	//------------------------------------------------------------
	//	Search methods.
	//------------------------------------------------------------

  public boolean enemyAdjacent(Creature c)
  {
  	return getAdjacentEnemies(c).hasMoreElements();
  }

 	public Enumeration getAdjacentEnemies(Creature creature)
  {
  	
  	int x = creature.getSquare().getx();
  	int y = creature.getSquare().gety();

  	Vector creatures = new Vector(); 
  	Creature currentCreature;
 
  	for(int dx = -1; dx <= 1; dx++)
  	{
  		if( dx + x < 0 || dx + x >= NOSQUARESWIDE ) continue;
  		for(int dy = -1; dy <= 1; dy++)
  		{
  			if( dy + y < 0 || dy + y >= NOSQUARESHIGH ) continue;
  			if(dx == 0 && dy == 0) continue; // Don't want to include s.
  			currentCreature = squares[x + dx][y + dy].getOccupant();
  			if(	currentCreature != null && 
				  	currentCreature.getSummoner() != creature.getSummoner())
  			{
  				creatures.addElement(currentCreature);
  			}
  		}
  	}
  	  	
  	// Return enumeration.
  	return creatures.elements();
  }

  public Enumeration getAdjacentCreatures(Square s)
  {
  	
  	int x = s.getx();
  	int y = s.gety();

  	Vector creatures = new Vector(); 
  	Creature currentCreature;
 
  	for(int dx = -1; dx <= 1; dx++)
  	{
  		if( dx + x < 0 || dx + x >= NOSQUARESWIDE ) continue;
  		for(int dy = -1; dy <= 1; dy++)
  		{
  			if( dy + y < 0 || dy + y >= NOSQUARESHIGH ) continue;
  			if(dx == 0 && dy == 0) continue; // Don't want to include s.
  			currentCreature = squares[x + dx][y + dy].getOccupant();
  			if(currentCreature != null)
  			{
  				creatures.addElement(currentCreature);
  			}
  		}
  	}
  	  	
  	// Return enumeration.
  	return creatures.elements();
  }
   
  // This method should be replaced with the bresenham's version.
  public boolean LOS(Square pos1, Square pos2)
    {
    		int x1 = pos1.getx();
    		int x2 = pos2.getx();
    		int y1 = pos1.gety();
    		int y2 = pos2.gety();
    		
    		
        if (pos1 == pos2) return true;
        int i = x2 - x1;
        int j = y2 - y1;
        float f = (float)j / i;
        byte b1 = 1;
        byte b2 = 1;
        if (i < 0) b1 = -1;
        if (j < 0) b2 = -1;
        int k = 0;
        System.out.println("Origin: " + x1 + "," + y1);
        System.out.println("Target: " + x2 + "," + y2);
        for (; k * b1 + x1 != x2; k++)
        {
        		if(k == 0) continue; // So we don't test start square.
            System.out.println("X loop: Checking square: " + (k * b1 + x1) + "," + (int)((float)(k * b1) * f + y1));
            if( squares[k * b1 + x1][(int)((float)(k * b1) * f + y1)].getOccupant() != null ) return false;
        }
        for (k = 0; k * b2 + y1 != y2; k++)
        {
        		if(k == 0) continue; // So we don't test start square.
            System.out.println("Y loop: Checking square: " + (int)((float)(k * b2) / f + x1) + "," + (k * b2 + y1));
            if(squares[(int)((float)(k * b2) / f + x1)][k * b2 + y1].getOccupant() != null) return false;
        }
        System.out.println("LOS verified");
        return true;
    }
    
   /*
  // LOS - Uses Bresenham's line algorithm.  
  public static boolean LOS(Square pos1, Square pos2)
  {
  	int x1 = pos1.getx();
  	int y1 = pos1.gety();
  	int x2 = pos2.getx();
  	int y2 = pos2.gety();
  	
  	int dx = x2 - x1;
  	int dy = y2 - y1;
  	
  	int de1,de2,e,x,y,inc;
  	
  	System.out.println("LOS: Checking LOS from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")");
  	
  	if(Math.abs(dx) > Math.abs(dy))
  	{
  		// Calculate delta errors.
  		de1 = dy << 1; 				// Applied to error when y is not incremented.
  		de2 = de1 - (dx << 2);// Applied to error when y is incremented.
  		
  		// Set initial values.
  		e = de1 - dx; 				// Initial error.
  		y = y1;								// Initially y is at y1.
  		inc = y1 > y2? 1 : -1;// The direction to step y so it moves towards y2.
  		
  		if(dx > 0)
  		{	
  			System.out.println("LOS: Stepping positively along x axis");

  			for(x = x1; x < x2; x++)
  			{
  				System.out.println("LOS: Checking square: (" + x + "," + y + ") error: " + e);
  				  				
  				// The first square may be legally occupied, but must be included in loop
  				// to calculate correct line, so check for special case.
  				if(x != x1 && y != y1 && squares[x][y].getOccupant() != null) return false;
  				if(e >= 0)
  				{
  					y += inc;
  					e += de2;
  				}
  				else e += de1;
  			}
  		}
  		else
  		{
  			System.out.println("LOS: Stepping negatively along x axis");
  			
  			for(x = x1; x > x2; x--)
  			{
  				System.out.println("LOS: Checking square: (" + x + "," + y + ") error: " + e);
  				
  				// The first square may be legally occupied, but must be included in loop
  				// to calculate correct line, so check for special case.
  				if(x != x1 && y != y1 && squares[x][y].getOccupant() != null) return false;
  				if(e <= 0)
  				{
  					y += inc;
  					e += de2;
  				}
  				else e += de1;
  			}
  		}
  	}
  	else
  	{
  		de1 = dx << 1;
  		de2 = de1 - (dy << 2);
  		e = de1 - dy;
  		x = x1;
  		inc = x1 > x2? 1 : -1;
  		
  		if(dy > 0)
  		{
  			System.out.println("LOS: Stepping positively along y axis");
  			
  			for(y = y1; y < y2; y++)
  			{
  				System.out.println("LOS: Checking square: (" + x + "," + y + ") error: " + e);
  				  				
  				// The first square may be legally occupied, but must be included in loop
  				// to calculate correct line, so check for special case.
  				if(x != x1 && y != y1 && squares[x][y].getOccupant() != null) return false;
  				if(e >= 0)
  				{
  					x += inc;
  					e += de2;
  				}
  				else e += de1;
  			}
  		}
  		else
  		{
  			System.out.println("LOS: Stepping negatively along y axis");
  			
  			for(y = y1; y > y2; y--)
  			{
  				System.out.println("LOS: Checking square: (" + x + "," + y + ") error: " + e);
  				
  				// The first square may be legally occupied, but must be included in loop
  				// to calculate correct line, so check for special case.
  				if(x != x1 && y != y1 && squares[x][y].getOccupant() != null) return false;
  				if(e <= 0)
  				{
  					x += inc;
  					e += de2;
  				}
  				else e += de1;
  			}
  		}
  	}	
  	
  	System.out.println("LOS: Verified.");
  	return true;
  }
  */
  
  
  public Enumeration getCorpses(Wizard w)
  {
  	Creature currentCreature;
  	Vector allCreatures = new Vector();
  	
  	// First pass, find out how many creatures there are.
  	for(int y=0; y < NOSQUARESHIGH; y++)
		{
			for(int x=0; x < NOSQUARESWIDE; x++) 
			{
				currentCreature = squares[x][y].topCorpse();
				if(currentCreature != null && currentCreature.getSummoner() == w)
					allCreatures.addElement(currentCreature);
			}
		}	
		
		return allCreatures.elements();
	}
	
  public Enumeration getCreatures(Wizard w)
  {
  	Creature currentCreature;
  	Vector allCreatures = new Vector();
  	
  	// First pass, find out how many creatures there are.
  	for(int y=0; y < NOSQUARESHIGH; y++)
		{
			for(int x=0; x < NOSQUARESWIDE; x++) 
			{
				currentCreature = squares[x][y].getOccupant();
				if(currentCreature != null && currentCreature.getSummoner() == w)
					allCreatures.addElement(currentCreature);
			}
		}	
		
		return allCreatures.elements();
	}
  
  public Enumeration getCreatures()
  {
  	Creature currentCreature;
  	Vector allCreatures = new Vector();
  	
  	// First pass, find out how many creatures there are.
  	for(int y=0; y < NOSQUARESHIGH; y++)
		{
			for(int x=0; x < NOSQUARESWIDE; x++) 
			{
				currentCreature = squares[x][y].getOccupant();
				if(currentCreature != null)
					allCreatures.addElement(currentCreature);
			}
		}	
		
		return allCreatures.elements();
	}
	
  //-------------------------------------------------------------
  //	Graphic methods.
  //-------------------------------------------------------------
  
  public void updateAndPaint(Graphics g)
  {
  	for(int y=0; y < NOSQUARESHIGH; y++)
		{
			for(int x=0; x < NOSQUARESWIDE; x++) 
			{
				squares[x][y].updatePaint(g);
			}
		}	
	}

} // End class Gameboard.

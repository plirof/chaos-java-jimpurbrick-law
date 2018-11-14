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

/* Decompiled by Mocha from Arrow.class */
/* Originally compiled from Arrow.java */

package law;

import java.awt.Color;
import java.awt.Graphics;
import java.io.PrintStream;
import java.util.Random;

class Fireball extends Arrow implements GFX
{
		private static Random randy;// For flickering effects.
		private int size; 					// Diameter of main circle.
		
		private double tipOffsetx; 	// Offset's from the tip
		private double tipOffsety; 	// for the various triangle
		
    private int[] t1x; 	// X co-ordinates for triangle 1.
    private int[] t1y; 	// Y co-ordinates for triangle 1.
    private int[] t2x; 	// X co-ordinates for triangle 2.
    private int[] t2y; 	// Y co-ordinates for triangle 2.
    private int[] t3x; 	// X co-ordinates for triangle 3.
    private int[] t3y; 	// Y co-ordinates for triangle 3.    

		static
		{
			randy = new Random();
		}
		
    Fireball(Square pos1, Square pos2)
    {
    	super(pos1, pos2);
    	
    	setLength(0.9);
    	   
    	size = (int) (0.4 * SQUAREWIDTH);
    	
      // Initialise triangle co-ordinates.
      t1x = new int[3];
      t1y = new int[3];
      t2x = new int[3];
      t2y = new int[3];
      t3x = new int[3];
      t3y = new int[3];
    
    	// Calculate the offsets.
    	tipOffsety = 3 * Math.cos(angle) * ysign;
    	tipOffsetx = 3 * Math.sin(angle) * -xsign;
    	
    	System.out.println( "Tip Offset (" + tipOffsetx + "," + tipOffsety + ")" );
    	
    }

    public void update()
    {
        super.update();        
    }
    
   
    public synchronized void die(Graphics g)
    {    		
    		// Cover over old Fireball.
    		g.setColor(Color.black);
        g.fillPolygon(t1x,t1y,3);
        g.fillPolygon(t2x,t2y,3);
        g.fillPolygon(t3x,t3y,3);
        g.fillOval((int)currentx - size/2, (int)currenty - size/2, size, size);
    		// game.getGameBoard().getSquareFromPixel((int) oldx, (int) oldy).paint(g);
    		
    		System.out.println("Fireball: die: Waking up threads.");
    		notifyAll(); 	
    }


    public void paint(Graphics g)
    {
    		// Cover over old Fireball.
    		g.setColor(Color.black);
        g.fillPolygon(t1x,t1y,3);
        g.fillPolygon(t2x,t2y,3);
        g.fillPolygon(t3x,t3y,3);
        g.fillOval((int)currentx - size/2, (int)currenty - size/2, size, size);
    		// game.getGameBoard().getSquareFromPixel((int) oldx, (int) oldy).paint(g);        
               
        // Calculate  new points for triangles.
        t1x[0] = (int) currentx + randy.nextInt() % 4;
        t1y[0] = (int) currenty + randy.nextInt() % 4;
        t1x[1] = (int) (tipx + tipOffsetx);
        t1y[1] = (int) (tipy + tipOffsety);
        t1x[2] = (int) (tipx - tipOffsetx);
        t1y[2] = (int) (tipy - tipOffsety);
        
        t2x[0] = (int) (currentx + tipOffsetx) + randy.nextInt() % 4;
        t2y[0] = (int) (currenty + tipOffsety) + randy.nextInt() % 4;
        t2x[1] = (int) (tipx + 2 * tipOffsetx);
        t2y[1] = (int) (tipy + 2 * tipOffsety);
        t2x[2] = (int) tipx;
        t2y[2] = (int) tipy;
        
        t3x[0] = (int) (currentx - tipOffsetx) + randy.nextInt() % 4;
        t3y[0] = (int) (currenty - tipOffsety) + randy.nextInt() % 4;
        t3x[1] = (int) tipx;
        t3y[1] = (int) tipy;
        t3x[2] = (int) (tipx - 2 * tipOffsetx);
        t3y[2] = (int) (tipy - 2 * tipOffsety);      
        
    		// Calculate a random base colour for this frame.
    		Color c;
    		int r = Math.abs( randy.nextInt() % 120 );
    		
    		// Draw the tail with a dark version of the colour.
        g.setColor(new Color(150,r,0));
        g.fillPolygon(t1x,t1y,3);
        g.fillPolygon(t2x,t2y,3);
        g.fillPolygon(t3x,t3y,3);
        
        // Draw the head with a lighter version.
        g.setColor(new Color(220,r,0));
        g.fillOval((int)tipx - size/2, (int)tipy - size/2, size, size);
        
        // Draw the core with a bright version.
        g.setColor(new Color(255,r+50,0));
        g.fillOval((int) (tipx - size/3), (int)tipy - size/3, (int) (size * 0.66), (int) (size * 0.66));

    }
}

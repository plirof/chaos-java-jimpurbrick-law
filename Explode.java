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

/* Decompiled by Mocha from Explode.class */
/* Originally compiled from Explode.java */

package law;

import java.awt.Color;
import java.awt.Graphics;
import java.io.PrintStream;

class Explode implements GFX, Data
{
    private Square pos;
    private Color c1, c2, currentColour;
    private double size;
    private double size_mod;
    private final double MAX_SIZE = 0.8;
    private int width;
    private int height;

    Explode(Square pos, Color colour1, Color colour2)
    {
        size = 0.2;
        size_mod = 0.2;
        this.pos = pos;
        size = 0.0;
        c1 = colour1;
        c2 = colour2;
        currentColour = c2;
    }
    
    Explode(Square pos)
    {
    	this(pos,Color.white,Color.gray);
    }

    public void update()
    {
    		// Calculate new size.
        if (size >= 0.8) size_mod = -size_mod;
        size += size_mod;
        width = (int)(size * SQUAREWIDTH);
        height = (int)(size * SQUAREHEIGHT);
        
        // Swap current colour.
        currentColour = currentColour == c1? c2 : c1;
    }

    public boolean finished()
    {
        if (size >= 0.0)
            return false;
        else
            return true;
    }

    public synchronized void die(Graphics g)
    {
        System.out.println("Explode: die: Waking up threads.");
        notifyAll();
    }

    public synchronized void waitTillDead()
    {
        try
        {
            System.out.println("Explode: waitTillDead: Sleeping.");
	    wait();
	    System.out.println("Explode: waitTillDead: Awake.");
        }
        catch (InterruptedException e)
        {
        }
        return;
    }

    public void paint(Graphics g)
    {
    		// Cover over old explosion.
    		pos.paint(g);
    		
    		if(currentColour == null) return;
    		
    		// Draw explosion with current colour.
    		g.setColor(currentColour);
        g.fillOval(pos.getx() * SQUAREWIDTH + (SQUAREWIDTH - width) / 2, pos.gety() * SQUAREHEIGHT + (SQUAREHEIGHT - height) / 2, width, height);
    }
}

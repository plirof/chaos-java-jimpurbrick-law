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

/* Decompiled by Mocha from MultiGFX.class */
/* Originally compiled from MultiGFX.java */

package law;

import java.awt.Graphics;
import java.io.PrintStream;

class MultiGFX implements GFX
{
    GFX effects[];

    public MultiGFX(GFX agFX[])
    {
        effects = agFX;
    }

    public boolean finished()
    {
        for (int i = 0; i < effects.length; i++)
            if(! effects[i].finished() ) return false; 
        return true;
    }

    public void update()
    {
        for (int i = 0; i < effects.length; i++)
        {
        	effects[i].update();
        }
    }

    public void paint(Graphics g)
    {
        for (int i = 0; i < effects.length; i++)
        	effects[i].paint(g);
    }

    public synchronized void die(Graphics g)
    {
        for (int i = 0; i < effects.length; i++)
       		effects[i].die(g);
        
        System.out.println("MultiGFX: die: Waking up threads.");
        notifyAll();
    }

    public synchronized void waitTillDead()
    {
        try
        {
            System.out.println("MultiGFX: waitTillDead: Sleeping.");
	    wait();
	    System.out.println("MultiGFX: waitTillDead: Awake.");
        }
        catch (InterruptedException e)
        {
        }
        return;
    }
}

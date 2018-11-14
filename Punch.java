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

/* Decompiled by Mocha from Punch.class */
/* Originally compiled from Punch.java */

package law;

import java.awt.*;
import java.io.PrintStream;

class Punch implements GFX, Data
{
    private static int SIZE_INC;
    private static int PAUSE_FRAMES;
    private int type;
    private Point pos;
    private Square square;
    private final int SIZE = 3;

    Punch(Square pos)
    {
    		square = pos;
        this.pos = new Point(0, 0);
        type = 1;
        this.pos.x = (int)(((double)pos.getx() + 0.5) * SQUAREWIDTH);
        this.pos.y = (int)(((double)pos.gety() + 0.5) * SQUAREHEIGHT);
    }

    Punch(int i, int j, int k)
    {
        pos = new Point(0, 0);
        type = i;
        pos.x = j;
        pos.y = k;
    }

    public void update()
    {
        type += SIZE_INC;
    }

    public boolean finished()
    {
        if (type <= 8)
            return false;
        else
            return true;
    }

    public synchronized void die(Graphics g)
    {
        // System.out.println("Waking up waiting threads...");
        notify();
    }

    public synchronized void waitTillDead()
    {
        try
        {
            wait();
        }
        catch (InterruptedException e)
        {
        }
        return;
    }

    public void paint(Graphics g)
    {
    		// Cover over old explosion.
    		square.paint(g);

        g.setColor(Color.red);
        if (type == 1)
        {
            paint_t1(g);
            return;
        }
        if (type == 2)
        {
            paint_t2(g);
            return;
        }
        if (type == 3)
        {
            paint_t3(g);
            return;
        }
        if (type == 4)
        {
            paint_t4(g);
            return;
        }
        if (type == 5)
        {
            paint_t5(g);
            return;
        }
        if (type == 6)
        {
            paint_t6(g);
            return;
        }
        if (type == 7)
        {
            paint_t7(g);
            return;
        }
        if (type == 8)
            paint_t8(g);
            
        // Reset the colour to black.
        g.setColor(Color.black);
    }

    private void paint_t1(Graphics g)
    {
        g.fillOval(pos.x, pos.y, 3, 3);
    }

    private void paint_t2(Graphics g)
    {
        g.fillOval(pos.x - 2, pos.y, 3, 3);
        g.fillOval(pos.x - 2, pos.y - 2, 3, 3);
        g.fillOval(pos.x - 2, pos.y + 2, 3, 3);
        g.fillOval(pos.x, pos.y - 2, 3, 3);
        g.fillOval(pos.x, pos.y + 4, 3, 3);
        g.fillOval(pos.x + 2, pos.y - 2, 3, 3);
        g.fillOval(pos.x + 2, pos.y, 3, 3);
        g.fillOval(pos.x + 2, pos.y + 2, 3, 3);
    }

    private void paint_t3(Graphics g)
    {
        g.fillOval(pos.x - 2, pos.y - 4, 3, 3);
        g.fillOval(pos.x - 2, pos.y, 3, 3);
        g.fillOval(pos.x - 2, pos.y + 2, 3, 3);
        g.fillOval(pos.x, pos.y - 2, 3, 3);
        g.fillOval(pos.x, pos.y + 6, 3, 3);
        g.fillOval(pos.x + 2, pos.y + 4, 3, 3);
        g.fillOval(pos.x + 2, pos.y, 3, 3);
        g.fillOval(pos.x + 2, pos.y - 2, 3, 3);
    }

    private void paint_t4(Graphics g)
    {
        g.fillOval(pos.x - 6, pos.y - 6, 3, 3);
        g.fillOval(pos.x - 4, pos.y, 3, 3);
        g.fillOval(pos.x - 6, pos.y + 6, 3, 3);
        g.fillOval(pos.x, pos.y - 4, 3, 3);
        g.fillOval(pos.x, pos.y + 8, 3, 3);
        g.fillOval(pos.x + 4, pos.y + 6, 3, 3);
        g.fillOval(pos.x + 4, pos.y, 3, 3);
        g.fillOval(pos.x + 4, pos.y - 4, 3, 3);
    }

    private void paint_t5(Graphics g)
    {
        g.fillOval(pos.x - 8, pos.y - 8, 3, 3);
        g.fillOval(pos.x - 6, pos.y, 3, 3);
        g.fillOval(pos.x - 8, pos.y + 8, 3, 3);
        g.fillOval(pos.x, pos.y - 6, 3, 3);
        g.fillOval(pos.x, pos.y + 10, 3, 3);
        g.fillOval(pos.x + 6, pos.y + 8, 3, 3);
        g.fillOval(pos.x + 6, pos.y, 3, 3);
        g.fillOval(pos.x + 6, pos.y - 6, 3, 3);
    }

    private void paint_t6(Graphics g)
    {
        g.fillOval(pos.x - 10, pos.y - 10, 3, 3);
        g.fillOval(pos.x - 8, pos.y, 3, 3);
        g.fillOval(pos.x - 10, pos.y + 10, 3, 3);
        g.fillOval(pos.x, pos.y - 8, 3, 3);
        g.fillOval(pos.x, pos.y + 12, 3, 3);
        g.fillOval(pos.x + 8, pos.y + 10, 3, 3);
        g.fillOval(pos.x + 8, pos.y, 3, 3);
        g.fillOval(pos.x + 8, pos.y - 8, 3, 3);
    }

    private void paint_t7(Graphics g)
    {
        g.fillOval(pos.x - 12, pos.y - 12, 3, 3);
        g.fillOval(pos.x - 10, pos.y, 3, 3);
        g.fillOval(pos.x - 12, pos.y + 12, 3, 3);
        g.fillOval(pos.x, pos.y - 10, 3, 3);
        g.fillOval(pos.x, pos.y + 14, 3, 3);
        g.fillOval(pos.x + 10, pos.y + 12, 3, 3);
        g.fillOval(pos.x + 10, pos.y, 3, 3);
        g.fillOval(pos.x + 10, pos.y - 10, 3, 3);
    }

    private void paint_t8(Graphics g)
    {
        g.fillOval(pos.x - 14, pos.y - 14, 3, 3);
        g.fillOval(pos.x - 12, pos.y, 3, 3);
        g.fillOval(pos.x - 14, pos.y + 14, 3, 3);
        g.fillOval(pos.x, pos.y - 12, 3, 3);
        g.fillOval(pos.x, pos.y + 16, 3, 3);
        g.fillOval(pos.x + 12, pos.y + 14, 3, 3);
        g.fillOval(pos.x + 12, pos.y, 3, 3);
        g.fillOval(pos.x + 12, pos.y - 12, 3, 3);
    }

    static 
    {
        SIZE_INC = 1;
        PAUSE_FRAMES = 10;
    }
}

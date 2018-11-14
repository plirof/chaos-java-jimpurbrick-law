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

import java.awt.Color;
import java.awt.Graphics;
import java.io.PrintStream;

/**
  * A graphic effect that displays an arrow shooting toward a target.
  */
class Arrow implements GFX, Data
{
		private Color colour;
    private int startx;
    private int starty;
    private int endx;
    private int endy;
    protected double currentx,oldx;
    protected double currenty,oldy;
    private double gradient;
    protected double angle;
    protected double tipx;
    protected double tipy;
    private int arrowLength;
    protected int xsign;
    protected int ysign;
    private static final double squareFraction = 0.5;

    Arrow(Square pos1, Square pos2, Color c)
    {
    		colour = c;
        xsign = 1;
        ysign = 1;
        startx = (int)(((double)pos1.getx() + 0.5) * SQUAREWIDTH);
        starty = (int)(((double)pos1.gety() + 0.5) * SQUAREHEIGHT);
        endx = (int)(((double)pos2.getx() + 0.5) * SQUAREWIDTH);
        endy = (int)(((double)pos2.gety() + 0.5) * SQUAREHEIGHT);
        currentx = tipx = startx;
        currenty = tipy = starty;
        if (endy - starty < 0)
            ysign = -1;
        if (endx - startx < 0)
            xsign = -1;
        if (pos1.getx() != pos2.getx())
        {
            gradient = (double)(endy - starty) / (endx - startx);
            angle = Math.abs(Math.atan(gradient));
        }
        else
            angle = 1.5708;
        arrowLength = (int)(0.5 * SQUAREWIDTH);
    }
    
    Arrow(Square pos1, Square pos2)
    {
				this(pos1,pos2,Color.gray);
		}
		
    Arrow(int i1, int j1, int k, int i2, int j2)
    {
        xsign = 1;
        ysign = 1;
        startx = i1;
        starty = -j1;
        endx = k;
        endy = -i2;
        currentx = tipx = startx;
        currenty = tipy = starty;
        if (endy - starty < 0)
            ysign = -1;
        if (endx - startx < 0)
            xsign = -1;
        if (i1 != k)
        {
            gradient = (double)(endy - starty) / (endx - startx);
            angle = Math.abs(Math.atan(gradient));
            System.out.println("The x positions are different!");
        }
        else
            angle = 1.5708;
        arrowLength = j2;
        System.out.println("Gradient:" + gradient + " Angle:" + angle);
    }

    public boolean finished()
    {
        if ( (tipx * xsign > endx * xsign) ||
        		 (tipy * ysign > endy * ysign) )
            return true;
        else
            return false;
    }

    public void update()
    {
    		oldx = currentx;
        oldy = currenty;
        currentx = tipx;
        currenty = tipy;
        tipx = currentx + Math.cos(angle) * arrowLength * xsign;
        tipy = currenty + Math.sin(angle) * arrowLength * ysign;
    }

    public synchronized void die(Graphics g)
    {
    		g.setColor(Color.black);
    		g.drawLine((int)oldx, (int)oldy, (int)currentx, (int)currenty);
    		
        System.out.println("Arrow: die: Waking up threads.");
        notifyAll();
    }

    public synchronized void waitTillDead()
    {
        try
        {
            System.out.println("Arrow: waitTillDead: Sleeping.");
	    wait();
	    System.out.println("Arrow: waitTillDead: Awake.");
        }
        catch (InterruptedException e)
        {
        }
        return;
    }

    public void paint(Graphics g)
    {
    		g.setColor(Color.black); 
        g.drawLine((int)oldx, (int)oldy, (int)currentx, (int)currenty);
        g.setColor(colour);
        g.drawLine((int)currentx, (int)currenty, (int)tipx, (int)tipy);            
    }
    
    public void setLength( double f )
    {
    	arrowLength = (int) (f * SQUAREWIDTH);
    }
    
    public void setLength( int i )
    {
    	arrowLength = i;
    }
}

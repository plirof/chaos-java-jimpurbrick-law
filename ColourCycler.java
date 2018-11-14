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
import java.awt.image.*;
import java.io.PrintStream;
import java.util.Random;

class ColourCycler implements GFX, Data
{
    private int framesLeft;
    private Color colours[];
    private Color background;
		private Square pos;
    private int width;
    private int height;
    private static Random randy;
    private static final int noFrames = 15;
    private BattleFieldAnimator battleField;
    private Image filtered;
    private int pixels[];

    public ColourCycler(Creature creature, BattleFieldAnimator battleFieldAnimator)
    {
        pos = creature.getSquare();
        width = 40;
        height = 40;
        framesLeft = 14;
        battleField = battleFieldAnimator;
        background = Color.black;
        Image original = creature.getFrame(1);
        colours = new Color[15];
        for (int i = 0; i < 15; i++)
            colours[framesLeft - i] = new Color(Math.abs(randy.nextInt() % 255), Math.abs(randy.nextInt() % 255), Math.abs(randy.nextInt() % 255));            
        SilhoetteImageFilter silhoetteImageFilter = new SilhoetteImageFilter(Color.black);
        FilteredImageSource filteredImageSource = new FilteredImageSource(original.getSource(), silhoetteImageFilter);     
        filtered = battleField.createImage(filteredImageSource);
        
        // Make the filtered image synchronously - otherwise the first few 
        // frames will be drawn just as squares while the image is made.
        MediaTracker m = new MediaTracker(battleFieldAnimator);
        m.addImage(filtered,0);
        try {m.waitForAll();}
        catch(InterruptedException e) {;}
    }

    public ColourCycler(Creature creature, BattleFieldAnimator battleFieldAnimator, Color acolor[])
    {
        this(creature, battleFieldAnimator);
        colours = acolor;
        framesLeft = acolor.length - 1;
    }

    public void update()
    {
        framesLeft--;
    }

    public boolean finished()
    {
        return framesLeft < 0;
    }

    public synchronized void die(Graphics g)
    {
    		pos.paint(g);
        System.out.println("ColourCycler: die: Waking up threads.");
        notifyAll();
    }

    public synchronized void waitTillDead()
    {
    		if(! finished())
    		{
	        try
	        {
	            System.out.println("ColourCycler: waitTillDead: Sleeping.");
	            wait();
	            System.out.println("ColourCycler: waitTillDead: Awake.");
	        }
	        catch (InterruptedException e)
	        {
	        }
	        }
    }

    public void paint(Graphics g)
    {
    		int x,y;
    		x = pos.getx() * SQUAREWIDTH;
        y = pos.gety() * SQUAREHEIGHT;
        g.setColor(colours[framesLeft]);
        g.fillRect(x, y, width, height);
        g.drawImage(filtered, x, y, null);
        
        //// 1.0 Fix ////
        g.setColor(Color.black);
        g.drawLine(x,y,x,y);
        //// End 1.0 Fix ////
    }

    static 
    {
        randy = new Random();
    }
}

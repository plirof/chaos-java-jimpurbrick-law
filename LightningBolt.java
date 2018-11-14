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

/* Decompiled by Mocha from LightningBolt.class */
/* Originally compiled from LightningBolt.java */

package law;

import java.awt.Color;
import java.awt.Graphics;
import java.io.PrintStream;
import java.util.Random;

class LightningBolt implements GFX, Data
{
		private static final int NOSTREAKS = 3;
		private static final Color colour = new Color(128,128,255);
    private static final double DEGRAD = 0.0174533;
    private static final int radius = 20;
    private static double sine[];
    private static double cosine[];
    private static Random randy;
    private double startx;
    private double starty;
    private double endx;
    private double endy;
    private int framesLeft;
    private int[] angles;
    private int oangle;

    LightningBolt(Square pos1, Square pos2)
    {
    		int angle;
        startx = ((double)pos1.getx() + 0.5) * SQUAREWIDTH;
        starty = ((double)pos1.gety() + 0.5) * SQUAREHEIGHT;
        endx = ((double)pos2.getx() + 0.5) * SQUAREWIDTH;
        endy = ((double)pos2.gety() + 0.5) * SQUAREHEIGHT;
        framesLeft = 1;
        angles = new int[NOSTREAKS];
        
        // The following code finds the angle of the path based on the coords.
				if(endx != startx)
					angle = (int) (Math.atan( (float) (endy - starty) / (endx - startx) ) / DEGRAD);
				else
				{
					if(endy < starty) angle = 90;
					else
						angle = -90;
				}

				// Use the acute angle to find angle between 0 and 360 degrees

				if(endy >= starty)
				{
					if(endx > startx)
						angle = 360 - angle;
					else
						angle = 270 - (angle + 90);
				}
				else
				{
					if(endx >= startx)
						angle = Math.abs(angle);
					else
						angle = 180 - angle;
				}
				
				oangle = angle;
    }

    public void update()
    {
        framesLeft--;
    }

    public boolean finished()
    {
        if (framesLeft >= 0)
            return false;
        else
            return true;
    }

    public synchronized void die(Graphics g)
    {
    		// Remove last frame of lightning.
    		cover(g);
    		
        System.out.println("LightningBolt: die: Waking up threads.");
        notifyAll();
    }

    public synchronized void waitTillDead()
    {
        try
        {
            System.out.println("LightningBolt: waitTillDead: Sleeping.");
	    wait();
	    System.out.println("LightningBolt: waitTillDead: Awake.");
        }
        catch (InterruptedException e)
        {
        }
        return;
    }
    
    public void paint( Graphics g)
		{
			double spointx, spointy;
			int angle;
			int loop, inner;
			int rand_ang;
			double draw_x, draw_y;

			int distance;

			// Cover over old lightning.
			cover(g);
			
			spointx = startx;
			spointy = starty;

			// Draw the new lightning bolt.
			
			// loop through each streak of lightning
			for(inner = 0; inner < NOSTREAKS; inner++)
			{
				angle = oangle;
				
				// colour = 76 + ( Math.abs(randy.nextInt()) % 15);

				// create a random angle for zig-zagging across the path

				rand_ang = ( Math.abs(randy.nextInt()) % 90) - 45;

				g.setColor(new Color(128 + 2 * rand_ang, 128 + 2 * rand_ang, 255));				
				
				angles[inner] = rand_ang;

				// set up the angle of the first line segment
				// (this cannot be done inside the inner loop because
				//  the current angle is the angle of the path; in the
				//  inner loop, the angle is always different from the path angle)

				angle += rand_ang;

				if(angle < 0) angle += 360;


				// update the end coordinates of the first line segment

				draw_x = startx + (cosine[angle] / 2);
				draw_y = starty - (sine[angle] / 2);

				g.drawLine((int) startx, (int) starty, (int) draw_x, (int) draw_y);


				// update the coordinates for the next line segment

				startx = draw_x;
				starty = draw_y;


				// work out how may intermediate lines required
				// use Pyth. Theorem to work out distance of path

				distance = 	(int) (Math.pow(Math.abs(endx - spointx), 2));
				distance += (int) (Math.pow(Math.abs(endy - spointy), 2));
				distance = 	(int) (Math.sqrt(distance));


				// now increase the distance to match the combined length
				// of every line segment in the path

				distance /= Math.cos( DEGRAD * rand_ang );


				// now divide that distance by the line segment length

				distance /= radius;


				for(loop = 0; loop < distance; loop++)
				{

					// change the direction of the next line segment, to be drawn,
					// so that it crosses back over the path

					rand_ang = -rand_ang;


					// add this direction on to the current angle

					angle += (rand_ang * 2);

	
					// check the angle hasn't gone below zero degrees

					if( angle < 0 ) angle += 360;
					else if( angle > 360 ) angle -= 360;


					// get the end coordinates of the current line segment

					draw_x = startx + cosine[angle];
					draw_y = starty - sine[angle];

					g.drawLine((int) startx, (int) starty, (int) draw_x, (int) draw_y);


					// update the start coordinates for the next line segment

					startx = draw_x;
					starty = draw_y;
				}


				// finally draw the final line segment from the last end coordinates
				// to the end of the path

				startx = spointx;
				starty = spointy;

				g.drawLine((int) draw_x, (int) draw_y, (int) endx, (int) endy);

			} // end of inner loop

		}
		
		
		private void cover(Graphics g)
		{
			double spointx, spointy;
			int angle;
			int loop, inner;
			int rand_ang;
			double draw_x, draw_y;

			int distance;

			// uint colour;

			// Cover over old lightning.
			spointx = startx;
			spointy = starty;

			g.setColor(Color.black);
			
			// loop through each streak of lightning
			for(inner = 0; inner < NOSTREAKS; inner++)
			{

				angle = oangle;
				// colour = 76 + ( Math.abs(randy.nextInt()) % 15);

				// create a random angle for zig-zagging across the path

				rand_ang = angles[inner];
				
				// set up the angle of the first line segment
				// (this cannot be done inside the inner loop because
				//  the current angle is the angle of the path; in the
				//  inner loop, the angle is always different from the path angle)

				angle += rand_ang;

				if(angle < 0) angle += 360;


				// update the end coordinates of the first line segment

				draw_x = startx + (cosine[angle] / 2);
				draw_y = starty - (sine[angle] / 2);

				g.drawLine((int) startx, (int) starty, (int) draw_x, (int) draw_y);


				// update the coordinates for the next line segment

				startx = draw_x;
				starty = draw_y;


				// work out how may intermediate lines required
				// use Pyth. Theorem to work out distance of path

				distance = 	(int) (Math.pow(Math.abs(endx - spointx), 2));
				distance += (int) (Math.pow(Math.abs(endy - spointy), 2));
				distance = 	(int) (Math.sqrt(distance));


				// now increase the distance to match the combined length
				// of every line segment in the path

				distance /= Math.cos( DEGRAD * rand_ang );


				// now divide that distance by the line segment length

				distance /= radius;


				for(loop = 0; loop < distance; loop++)
				{

					// change the direction of the next line segment, to be drawn,
					// so that it crosses back over the path

					rand_ang = -rand_ang;


					// add this direction on to the current angle

					angle += (rand_ang * 2);

	
					// check the angle hasn't gone below zero degrees

					if( angle < 0 ) angle += 360;
					else if( angle > 360 ) angle -= 360;


					// get the end coordinates of the current line segment

					draw_x = startx + cosine[angle];
					draw_y = starty - sine[angle];

					g.drawLine((int) startx, (int) starty, (int) draw_x, (int) draw_y);


					// update the start coordinates for the next line segment

					startx = draw_x;
					starty = draw_y;
				}


				// finally draw the final line segment from the last end coordinates
				// to the end of the path

				startx = spointx;
				starty = spointy;
				angle = oangle;

				g.drawLine((int) draw_x, (int) draw_y, (int) endx, (int) endy);

			} // end of inner loop

			// colour = 76 + (rand() % 15);
			
		} // End method cover.
		
		
		static 
    {
        sine = new double[720];
        cosine = new double[720];
        for (int i = 0; i < 720; i++)
        {
            sine[i] = radius * Math.sin(0.0174533 * i);
            cosine[i] = radius * Math.cos(0.0174533 * i);
        }
        randy = new Random();
    }
}

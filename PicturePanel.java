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

/* Decompiled by Mocha from PicturePanel.class */
/* Originally compiled from PicturePanel.java */

package law;

import java.awt.*;

class PicturePanel extends Canvas
{
    private int width;
    private int height;
    private Image image;

		PicturePanel(Image image, Color color, int i, int j)
		{
			this(image,i,j);
			setBackground(color);
		}
		
    PicturePanel(Image image, int i, int j)
    {
    		MediaTracker m = new MediaTracker(this);
    		m.addImage(image,0);
    		try {m.waitForAll();}
    		catch(Exception e) {;}
        width = i;
        height = j;
        this.image = image;
    }

    PicturePanel(Image image)
    {
        this.image = image;
        MediaTracker m = new MediaTracker(this);
    		m.addImage(image,0);
    		try {m.waitForAll();}
    		catch(Exception e) {;}
        width = image.getWidth(this);
        height = image.getHeight(this);
    }

    public void paint(Graphics g)
    {
        g.drawImage(image, (size().width - image.getWidth(this)) / 2, (size().height - image.getHeight(this)) / 2, this);
    }

    public Dimension preferredSize()
    {
        return minimumSize();
    }
    
    public Dimension minimumSize()
    {
    	return new Dimension(width, height);        
    }

}

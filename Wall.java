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

/* Decompiled by Mocha from Wall.class */
/* Originally compiled from Wall.java */

package law;

import java.awt.Component;
import java.awt.Graphics;

class Wall extends AutoCreature
{
    public Wall(Square pos, Wizard wizard)
    {
    		super(wizard.getGame());
        summoner = wizard;
        
        square = pos;
        square.setOccupant(this);
        
        defense = 8;
        name = "Magic Wall";
        frames = game.getAutoCreatureImages(6);
    }

    public void update()
    {
    }

		/*
    public void paint(Graphics g, Component component, int i)
    {
        g.drawImage(frames[1], square.getx() * SQUAREWIDTH, square.gety() * SQUAREHEIGHT, SQUAREWIDTH, SQUAREHEIGHT, component);
    }
    */
}

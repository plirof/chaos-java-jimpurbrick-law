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

/* Decompiled by Mocha from GooeyBlob.class */
/* Originally compiled from GooeyBlob.java */

package law;

import java.awt.Component;
import java.awt.Graphics;
import java.io.PrintStream;

class GooeyBlob extends AutoCreature
{
    private Creature engulfed;

    GooeyBlob(Square pos, Wizard wizard)
    {
    		super(wizard.getGame());
        summoner = wizard;
        engulfed = pos.getOccupant();
        name = "Gooey Blob";        
        square = pos;
        square.setOccupant(this);
        int index=0;
        align = 0;
        factor = 0;
        defense = 1;
        magRes = 4;
        undead = false;
        flyer = false;
        mount = false;
        frames = game.getAutoCreatureImages(0);
    }

    public void update()
    {
    		// Randomly find an adjacent square for the new blob.
        int newx = square.getx() + (game.getDice().d(3) - 1);
        int newy = square.gety() + (game.getDice().d(3) - 1);
        
        // Make sure that we're not creating a creature off the board.
        if (newx < 0 || newx >= NOSQUARESWIDE || newy < 0 || newy >= NOSQUARESHIGH) return;

				// Create a blob on the new square.
        Square pos = game.getGameBoard().getSquare(newx, newy);
        Creature creature = pos.getOccupant();
        if(creature != null)
        {        	 
        	// Wizards are killed if engulfed.
        	if(creature instanceof Wizard)
        	{
        		creature.die();
        	}
        	// If a blob is already on the square, don't make another.
        	if(creature instanceof GooeyBlob)
        	{
            return;
        	}
        }
        GooeyBlob newBlob = new GooeyBlob(pos, summoner);
    }

    public void die()
    {
        super.die();
        if(engulfed != null) 
        {
        	engulfed.resetMove();
        	engulfed.setSquare(square);        	
        }
    }

    public boolean engulfing()
    {
        if (engulfed == null)
            return false;
        else
            return true;
    }
}

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

/* Decompiled by Mocha from MagicFire.class */
/* Originally compiled from MagicFire.java */

package law;

import java.awt.Component;
import java.awt.Graphics;

class MagicFire extends AutoCreature
{
    MagicFire(Square pos, Wizard wizard)
    {
    		super(wizard.getGame());
        summoner = wizard;
        name = "Magic Fire";
        
        square = pos;
        square.setOccupant(this);
        
        int index = 1;
        align = 0;
        factor = 0;
        combat = 2;
        defense = 1;
        magRes = 4;
        undead = false;
        flyer = false;
        mount = false;
        frames = game.getAutoCreatureImages(1);
    }

    public void update()
   	{
        // Randomly find an adjacent square for the new magic fire.
        int newx = square.getx() + (game.getDice().d(3) - 1);
        int newy = square.gety() + (game.getDice().d(3) - 1);
        
        // Make sure that we're not creating a creature off the board.
        if (newx < 0 || newx >= NOSQUARESWIDE || newy < 0 || newy >= NOSQUARESHIGH) return;

				// Either attack the creature on the new square or create a new fire.
        Square pos = game.getGameBoard().getSquare(newx, newy);
        Creature creature = pos.getOccupant();
        if (creature != null)
        {
            whack(creature);
        }
        else         
        {
        		MagicFire magicFire = new MagicFire(pos, summoner);
        }
    }

		/*
    public void paint(Graphics g, Component component, int i)
    {
        g.drawImage(frames[i], pos.x * Pos.squareWidth, -pos.y * Pos.squareHeight, component);
    }
    */
}

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

/* Decompiled by Mocha from Walker.class */
/* Originally compiled from Walker.java */

package law;

/**
* A sub-class of Creature that implements walking. This is a slightly broken
* class heirarchy - both flight and walking should be defined in Creature,
* and Mount should be a sub-class of Creature, rather than an Interface.
*/
public class Walker extends Creature implements Data
{
    Walker(String string, Square pos, boolean flag, Chaos game)
    {
        super(string, pos, flag, game);
    }

    public synchronized boolean move(Square pos)
    {
        Creature creature = pos.getOccupant();
        
        if (square.adjacent(pos))
        {
        		// Always allow an attack.
            if (creature != null)
            {
            		// If creature is an enemy, attack it.
                if(creature.summoner != summoner || creature instanceof GooeyBlob || creature instanceof MagicFire)
                {
                		moved = true;
                    if (whack(creature) && pos.getOccupant() == null)
                    {
                       setSquare(pos);
                       return true;
                    }
                    return false;
                }
                // Tried to move to a friendly, occupied square.
                else return false;
            }
            // If not attacking, but engaged, do not allow the move.
            else if (engaged())
            {
                game.getFrame().getMessageArea().setText("Engaged to Enemy");
                return false;
            }
            else
            {
            	setSquare(pos);
            	movesLeft--;
            	return true;
            }
        }
        // Square was not adjacent.
        game.getFrame().getMessageArea().setText("Must move 1 square at a time.");
        return false;
    }
}

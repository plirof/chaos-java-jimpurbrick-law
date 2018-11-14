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

/* Decompiled by Mocha from Flyer.class */
/* Originally compiled from Flyer.java */

package law;

/**
* A sub-class of Creature that implements flight. This is a slightly broken
* class heirarchy - both flight and walking should be defined in Creature,
* and Mount should be a sub-class of Creature, rather than an Interface.
*/
public class Flyer extends Creature implements Data
{
    Flyer(String string, Square pos, boolean flag, Chaos game)
    {
        super(string, pos, flag, game);
    }

    public synchronized boolean move(Square pos)
    {
        Creature creature = pos.getOccupant();
        
        		if (engaged())
            {
            		// If not attacking anything, do not allow the move.
                if (creature == null || creature.summoner == summoner || ! square.adjacent(pos))
                {
                  return false;
                }
                    
                // Otherwise allow the attack.
                moved = true;
                if (whack(creature) && pos.getOccupant() == null)
                {
                	Pause pause = new Pause(8);
                  queEffect(pause);
                  pause.waitTillDead();
                  setSquare(pos);
                	return true;   
                }
                else return false;
            }
            else if (square.range(pos) <= move)
            {
            		// If square empty and in range, move there.
                if(creature == null)
                {
                    setSquare(pos);
                    movesLeft = 0;
                    return true;
                }
                // If enemy there, swoop attack it.
                else if(creature.summoner != summoner || creature instanceof GooeyBlob || creature instanceof MagicFire)
                {
                		whack(creature);
                		moved = true;
                    return false;
                }
                // Tried to move onto a friendly occupied square, 
                // return false.
                else return false;
            }
            game.getFrame().getMessageArea().setText("Square out of range");
            return false;
        }
}

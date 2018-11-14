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

/* Decompiled by Mocha from ShadowWood.class */
/* Originally compiled from ShadowWood.java */

package law;

class ShadowWood extends AutoCreature
{
    ShadowWood(Square pos, Wizard wizard)
    {
    		super(wizard.getGame());
        summoner = wizard;
        
        square = pos;
        square.setOccupant(this);
        
        name = "Shadow Wood";
        combat = 1;
        defense = 3;
        range = ranged = move = man = 0;
        magRes = 4;
        frames = game.getAutoCreatureImages(4);
    }

    public void update()
    {
    }

    public Creature select(Wizard wizard)
    {
        if (!moved && summoner == wizard && game.getGameBoard().enemyAdjacent(this))
            return this;
        else
            return null;
    }

    public boolean setLocation(Square pos)
    {
        Creature creature = pos.getOccupant();
        if ((creature == null || ! square.adjacent(pos) || creature.summoner == summoner) && !(creature instanceof GooeyBlob) && !(creature instanceof MagicFire))
            return false;
        whack(creature);
        moved = true;
        return false;
    }
}

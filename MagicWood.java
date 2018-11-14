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

/* Decompiled by Mocha from MagicWood.class */
/* Originally compiled from MagicWood.java */

package law;

import java.awt.Component;
import java.awt.Graphics;

class MagicWood extends AutoCreature implements Mount
{
    private Creature rider;

    MagicWood(Square pos, Wizard wizard)
    {
    		super(wizard.getGame());
        summoner = wizard;
        name = "Magic Wood";
        
        square = pos;
        square.setOccupant(this);
        
        align = 0;
        factor = 0;
        defense = 1;
        magRes = 4;
        undead = false;
        flyer = false;
        mount = true;
        frames = game.getAutoCreatureImages(5);
    }

    public void update()
    {
        if (game.getDice().d(3) == 0 && rider == summoner)
        {
            summoner.newSpell();
            game.getFrame().getMessageArea().setText("New spell for " + summoner.getName());
            die();
        }
    }

    public boolean mount(Wizard wizard)
    {
        rider = wizard;
        wizard.setSquare(square); // This will make rider the occupant.
        square.setOccupant(this); // So switch it back to mount.
        wizard.setMoved(true);
        return true;
    }

    public void die()
    {
    	super.die();
    	
      if(rider != null) 
      {
      	square.setOccupant(rider);
      }      
    }
    
    
		// Needed to reset the rider's move.    
    public void resetMove()
    {
        super.resetMove();
        if(rider != null)
        {
        	rider.resetMove();
        }
    }

    public boolean isMounted()
    {
        if (rider == null)
            return false;
        else
            return true;
    }
    
    // Always a nasty one...
    public void handleDialog(String choice)
    {
        if(choice.equals("Yes"))
        {
            game.sendSelect(summoner.getSquare(), Chaos.SELECT_RIDER);
            rider = null;
            return;
        }
        if(choice.equals("No"))
        game.sendSelect(summoner.getSquare(), Chaos.SELECT_MOUNT);
        return;
    }
    
    public boolean selectForMove(Wizard w)
    {
        if (square == summoner.getSquare()) rider = summoner;   
        if(rider != null && rider.selectForMove(w))
    		{
   				YesNoDialog d = new YesNoDialog("Move Wizard?", this, game.getFrame());
   			}
        return false;
    }
    
    public Creature getRider()
    {
    	return rider;
    }
}

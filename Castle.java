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

import java.awt.Component;
import java.awt.Graphics;

class Castle extends AutoCreature implements Mount
{
    private Creature rider;
    private boolean asked;

    Castle(Square pos, Wizard wizard, int i)
    {
    		super(wizard.getGame());
        summoner = wizard;
        if (i == 2)
            name = "Dark Citadel";
        else
            name = "Magic Castle";
        square = pos;
        pos.setOccupant(this);
        align = 0;
        factor = 0;
        defense = 10;
        magRes = 4;
        undead = false;
        flyer = false;
        mount = true;
        frames = game.getAutoCreatureImages(i);
    }

    public void update()
    {
        if (game.getDice().d(8) == 0)
            die();
    }
    
    public void die()
    {
    	super.die();
    	
      if(rider != null) 
      {
      	square.setOccupant(rider);
      }      
    }

    public boolean selectForMove(Wizard w)
    {   
    		// If there is no rider, treat mount as a normal creature.
    		if(rider == null || asked)
    		{
    			return super.selectForMove(w);
    		}
    		// If either can move, give the player a choice.
    		else if(rider.selectForMove(w) && super.selectForMove(w))
    		{
   				YesNoDialog d = new YesNoDialog("Dismount Wizard?", this, game.getFrame());
   				asked = true;
   			}
   			// Return false for now - selection will be made on dialog choice.
   			return false;
    }
    
    public boolean mount(Wizard wizard)
    {
        if (moved)
            return false;
        rider = wizard;
        wizard.setSquare(square);
        wizard.setMoved(true);
        return true;
    }

		// Needed to reset the rider's move.    
    public void resetMove()
    {
        super.resetMove();
        asked = false;
        if(rider != null)
        {
        	rider.resetMove();
        }
    }

		// Always a nasty one...
    public void handleDialog(String choice)
    {
        if(choice.equals("Yes"))
        {
            game.sendSelect(summoner.getSquare(), Chaos.SELECT_RIDER);
            rider = null;
            setMoved(true);
            return;
        }
        if(choice.equals("No"))
        game.sendSelect(summoner.getSquare(), Chaos.SELECT_MOUNT);
        return;
    }

    public boolean isMounted()
    {
        if (rider == null)
            return false;
        else
            return true;
    }
    
    public Creature getRider()
    {
    	return rider;
    }
}

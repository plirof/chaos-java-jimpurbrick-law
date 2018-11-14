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

/* Decompiled by Mocha from WalkingMount.class */
/* Originally compiled from WalkingMount.java */

package law;

/**
* A sub-class of Creature that implements flight and mount. This is a slightly broken
* class heirarchy - both flight and walking should be defined in Creature,
* and Mount should be a sub-class of Creature, rather than an Interface.
*/
class WalkingMount extends Walker implements Data, Mount
{
		private boolean asked; 	// So we only ask about dismounting
														// once per turn.
    private Wizard rider;

    WalkingMount(String string, Square pos, boolean flag, Chaos game)
    {
        super(string, pos, flag, game);
    }
    	
    public void setSquare(Square pos)
    {
    		// Move mount to new square.
    		super.setSquare(pos);
    		
    		// Now bring rider if there is one.
        if (rider != null) rider.setSquare(pos);
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
    
    public boolean move(Square pos)
    {
        boolean moved = super.move(pos);
        if(rider != null && moved)
        {
            rider.setSquare(square);
        }
        return moved;
    }

		public void die()
    {
    	dead = true;

      if(rider != null) 
      {
      	square.setOccupant(rider);
      }
      else if(square.getOccupant() == this)
      {
      	square.setOccupant(null);
      }
      square.pushCorpse(this);
      square.paint(game.getBattleField().getBufferGraphics());
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
        if (rider == null || square != summoner.getSquare())
            return false;
        else
            return true;
    }
    
    public Creature getRider()
    {
    	return rider;
    }
}

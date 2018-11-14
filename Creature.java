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

import java.awt.*;
import java.util.*;

/**
  * Represents a creature in the game that can be attacked, cast at and
  * potentially moved and ridden. Note that objects like Castles and Gooey 
  * Blobs are also Creatures, but have special behaviours implemented as
  * subclasses of the AutoCreature class.
  */

public abstract class Creature implements Data, CreatureStats, InfoPainter
{

		//----------------------------------------------
		//	Define member variables.
		//----------------------------------------------
		
		// Frame map and counters allow animation to loop.
		private static final byte frameMap[] = {0,1,2,1};
		private byte frameCounter;
		private byte currentFrame;
		
    // protected static BattleFieldAnimator battleField;
    
    protected Image frames[];
    protected Wizard summoner;
    protected boolean dead;
    private boolean engaged;
    protected int movesLeft;
    protected boolean moved;
    protected boolean shot;
    protected String name;
    protected int align;
    protected int factor;
    protected int combat;
    protected int ranged;
    protected int range;
    protected int defense;
    protected int move;
    protected int man;
    protected int magRes;
    protected Square square;
    protected boolean undead;
    protected boolean flyer;
    protected boolean mount;
    protected Chaos game;
    private boolean illusion;
    private boolean selected; // Used to paint the coloured border.
    
		//--------------------------------------------
		//	Constructors.
		//--------------------------------------------

    Creature(Chaos c)
    {
    		game = c;
    }

    /**
      * Creates a creature of a certain type and positions them on
      * the game board.
      *
      * @param type type of the creature - "Bat", "Rat" etc.
      * @param pos initial position to create the creature.
      * @param illusion true if the creature is illusionary.
      * @param c the game which allows access to the gameboard etc.
      */
    Creature(String type, Square pos, boolean illusion, Chaos c)
    {
        name = type;
        game = c;
        
        this.illusion = illusion;
        int i = search(type);
        align = CreatureStats.aligns[i];
        factor = CreatureStats.factors[i];
        combat = CreatureStats.combats[i];
        ranged = CreatureStats.rangeds[i];
        range = CreatureStats.ranges[i];
        defense = CreatureStats.defenses[i];
        move = CreatureStats.moves[i];
        man = CreatureStats.mans[i];
        magRes = CreatureStats.magRess[i];
        undead = CreatureStats.undeads[i];
        flyer = CreatureStats.flyers[i];
        mount = CreatureStats.mounts[i];
        frames = game.getCreatureImages(i);
        resetMove();
        
        // Set up square on gameboard, don't automatically set
        // creature as occupant to allow fade in. setSquare is
        // called after fade to set occupant.
        square = pos;
    }

		//---------------------------------------
		//	Access Methods.
		//---------------------------------------
		
		/**
		* Used to signal to the creature that it should paint
		* a coloured border. Different to select method which 
		* asks if a creature can be selected for movement.
		*/
		public void setSelected(boolean b)
		{
			selected = b;
		}
		
		public void setUndead(boolean b)
		{
			undead = b;
		}
		
		public void setDead(boolean b)
		{
			dead = b;
		}
			
		public void setLeader(Wizard wizard)
    {
        summoner = wizard;
    }
    
    public void setRange(int i)
    {
        range = i;
    }

    public int getRanged()
    {
        return ranged;
    }

    public void setRanged(int i)
    {
        ranged = i;
    }

    public void setFlyer(boolean flag)
    {
        flyer = flag;
    }

    public void setMovement(int i)
    {
        move = i;
    }

    /**
      * Returns true if the creature has moved this turn.
      */
    public boolean getMoved()
    {
        return moved;
    }

    /**
      * Called once the creature has finished moving, so that it cannot
      * be selected for movement again.
      */
    public void setMoved(boolean flag)
    {
        moved = flag;
    }

    /**
      * Returns type of creature, "Bat, "Rat" etc.
      */
    public String getName()
    {
        return name;
    }

    public Wizard getSummoner()
    {
        return summoner;
    }

    /**
     * Returns manoeverability of creature.
     */
    public int getMan()
    {
        return man;
    }

    public boolean isIllusion()
    {
        return illusion;
    }

    public Image getFrame(int i)
    {
        return frames[i];
    }

    /**
      * Returns magic resistence of creature.
      */
    public int getMagRes()
    {
        return magRes;
    }

    public int getDefense()
    {
        return defense;
    }

    /**
      * Called at the start of the turn to reset the creatures movement,
      * shooting and engaged status.
      */
    public void resetMove()
    {
        movesLeft = move;
        moved = false;
        shot = false;
        engaged = false;
    }

    /**
      * Requests that the creature move to a given square.
      *
      * @param pos the desired target square.
      * @return false if the move was invalid (out of range, not adjacent etc.)
      *               or if the creature tried, but failed to move there. For
      *               example, they attacked an occupant in the square, but
      *               did not kill the occupant.
      */
    public abstract boolean move(Square pos);
      
    /**
      * Returns true if the creature can be selected for movement.
      *
      * @param currentWizard the wizard whos turn it is.
      */
    public boolean selectForMove(Wizard currentWizard)
    {
       	return currentWizard == summoner && ! moved && (movesLeft > 0 || game.getGameBoard().enemyAdjacent(this)) && ! dead;
    }
    
    /**
      * Returns true if it's the creatures go, they have ranged ability
      * and they haven't shot this turn
      */
    public boolean selectForRanged(Wizard currentWizard)
    {
    		return currentWizard == summoner && ranged > 0 && ! shot;
    }
     
    /**
      * Returns the square the creature is currently occupying.
      */
    public Square getSquare() { return square; }
    
    /**
      * Set the creature's position. This is called by move and bypasses
      * tests about movement and engagement, so could be used for 
      * teleportation etc.
      *
      * @param s the target square to move the creature to
      *
      */
    public void setSquare(Square s)
    {
    	
    	 	// Leave current square.
	    	if(square.getOccupant() == this) 
	    	{
	    		square.setOccupant(null);
	    		square.paint(game.getBattleField().getBufferGraphics());
	    	}
	                
	      // Move to new sqaure.
	      square = s;
	      
	      if(square.getOccupant() == null)
	      {
	      	square.setOccupant(this);
	      	square.paint(game.getBattleField().getBufferGraphics());
	      }
    }

    /**
      * Returns true if the creature is engaged in combat and so cannot be
      * moved.
      */
    public boolean engaged()
    {
    		Creature currentCreature;
    		
    		// If engaged flag is set, already tested once this turn, so return true.
        if (engaged)	return true;
        
        // Otherwise find maximum manouver value of all adjacent creatures.
        int i = 0;
        for (Enumeration e = game.getGameBoard().getAdjacentEnemies(this); e.hasMoreElements();)
        {
        		currentCreature = (Creature) e.nextElement();
            if ( currentCreature.getMan() > i)
                i = currentCreature.getMan();
        }
        
        // If maximum manouver was 0, then not engaged.
        if (i == 0)
            return false;
            
        // Otherwise dice for it.
        if (game.getDice().d(6) + game.getDice().d(6) + i <= game.getDice().d(6) + game.getDice().d(6) + man)
            return false;
            
        // Failed manouver test, set engaged flag and return true.
        engaged = true;
        return true;
    }

    /**
      * Called when a creature looses combat. Undead and illusions disappear,
      * real creatures become a corpse on the square.
      */
    public void die()
    {
    	// Undead don't die, they are destroyed.
    	if(undead || illusion) destroy();
    	else
    	{
        dead = true;
        if(square.getOccupant() == this) square.setOccupant(null);
        square.pushCorpse(this);
        square.paint(game.getBattleField().getBufferGraphics());
      }
    }

    /**
      * Undead and illusions are destroyed when they lose combat. Normal
      * creatures could be destroyed by spells.
      */
    public void destroy()
    {
    		// Remove the creature from game board.
        if(square.getOccupant() == this) square.setOccupant(null);
        
        // Repaint square so it gets cleared.
        square.paint(game.getBattleField().getBufferGraphics());
    }

    /**
      * Carries out an attack by setting up the graphic effects,
      * performing the attack test, and then killing the target if
      * they lose.
      *
      * @return true if the target is now dead/destroyed
      */
    public boolean whack(Creature creature)
    {
     		// Set up effects.
        Punch punch = new Punch(creature.getSquare());
        queEffect(punch);
        punch.waitTillDead();
        
        // Now carry out attack.
        if (attack(creature, combat))
        {        
            creature.die();          
            return true;
        }
        else return false;
    }

    /**
      * Performs a normal attack on a target creature. If the target is
      * undead, the attack will fail unless the attacker is undead.
      *
      * @param creature the target creature.
      * @param i the strength of the attack (attackers strength etc.)
      * @return true if the target lost.
      */
    public boolean attack(Creature creature, int i)
    {
        if (!creature.undead || undead)
            return magicAttack(creature, i);
        game.getFrame().getMessageArea().setText("Cannot harm undead");
        return false;
    }

    /**
      * Performs a magical attack on a target creature. This attack can
      * affect any creature.
      *
      * @param creature the target creature.
      * @param i the strength of the attack (spell strength etc.)
      * @return true if the target lost.
      */
    public boolean magicAttack(Creature creature, int i)
    {
        if (game.getDice().d(6) + creature.defense >= game.getDice().d(8) + i)
            return false;
        else
            return true;
    }

      /**
      * Causes this creature to attack a target square if they have ranged
      * combat ability, line of sight and have not shot this turn.
      *
      * @param pos the target square.
      * @return true if the target is now dead/destroyed.
      */	
    public boolean rangedAttack(Square pos)
    {
        Creature creature = pos.getOccupant();
        if (ranged > 0 && ! shot)
        {
            if (square.range(pos) > range)
            {
                game.getFrame().getMessageArea().setText("Out of range");
                return true;
            }
            if ( ! game.getGameBoard().LOS(square, pos))
            {
                game.getFrame().getMessageArea().setText("No line of sight");
                return true;
            }
            if(name.endsWith("Dragon")) 
            {
            	queEffect(new Fireball(square,pos));
            }
            else queEffect(new Arrow(square, pos));
            Explode explode = new Explode(pos);
            queEffect(explode);
            explode.waitTillDead();
	    shot = true;
            if (creature != null && attack(creature, ranged))
	    {
	      creature.die();
	      return true;
	    }
        }
        return false;
    }

		/**
		  * Paint the current frame and selection square if selected.
		  *
		  * @param g the graphics context to paint to.
		  */
		public void paint(Graphics g)
		{
			// The current frame may be null if this is an autoCreature
			// or their was an error loading the creature's images.
			if(frames[currentFrame] == null) return;
			
			// Draw the current image.
			g.drawImage(frames[currentFrame], square.getx() * SQUAREWIDTH, square.gety() * SQUAREHEIGHT, null);
			
			// Draw a border if the creature is selected.
			if(selected)
			{
				g.setColor(summoner.getColour());
				g.drawRect(square.getx() * SQUAREWIDTH, square.gety() * SQUAREHEIGHT, 39, 39);
				g.setColor(Color.black);
			}
		}
		
		/**
		 * Paints an image of the dead creature.
		 * 
		 * @param g the graphics context to paint to.
		 */
		public void corpsePaint(Graphics g)
		{
			g.drawImage(frames[3], square.getx() * SQUAREWIDTH, square.gety() * SQUAREHEIGHT, null);
		}
		
		/**
		  * Moves to the next frame.
		  */
		public void updateFrame()
		{
			frameCounter = (byte) (++frameCounter % 4);
			currentFrame = frameMap[frameCounter];
		}
		
    /**
      * Paints just the selection border around the creature.
      *
      * @param g the graphics context to paint to.
      */
    public void selectPaint(Graphics g)
    {
        g.setColor(summoner.getColour());
        g.drawRect(square.getx() * SQUAREWIDTH, square.gety() * SQUAREHEIGHT, SQUAREWIDTH, SQUAREHEIGHT);
        g.setColor(Color.black); 
    }
    
    /**
      * Erases the selection border around a creature.
      *
      * @param g the graphics context to paint to.
      */
    public void unSelectPaint(Graphics g)
    {
    		g.drawRect(square.getx() * SQUAREWIDTH, square.gety() * SQUAREHEIGHT, SQUAREWIDTH, SQUAREHEIGHT);
    }

    /**
      * Paints information about the creature to the screen.
      *
      * @param g the graphics context to paint to.
      */
    public void infoPaint(Graphics g)
    {     
        FontMetrics fontMetrics = g.getFontMetrics(infoFont);
        g.setFont(infoFont);
        g.setColor(Color.yellow);
        g.drawString(name, 300 - fontMetrics.stringWidth(name) / 2, 50);
        g.drawString("Combat " + combat, 5, 180);
        g.drawString("Ranged Combat " + ranged, 5, 210);
        g.drawString("Range " + range, 5, 240);
        g.drawString("Defense " + defense, 5, 270);
        g.drawString("Movement " + move, 5, 150);
        String string = "( " + Data.alignString[align + 1] + " " + factor + " )";
        g.drawString(string, 300 - fontMetrics.stringWidth(string) / 2, 80);
        g.drawString("Maneouvre " + man, 5, 300);
        g.drawString("Magic Resistance " + magRes, 5, 330);

    }

    /**
      * Find a creature index from a string.
      */
    private int search(String string)
    {
        for (int i = 0; i < CreatureStats.names.length; i++)
            if (CreatureStats.names[i].equals(string))
                return i;
        return -1;
    }

    /**
      * Add a graphic effect to the battlefield queue.
      */
    public void queEffect(GFX gFX)
    {
        game.getBattleField().queEffect(gFX);
    }
}

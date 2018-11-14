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

/* Decompiled by Mocha from Wizard.class */
/* Originally compiled from Wizard.java */

package law;

import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;

public class Wizard extends Creature
{
		private boolean local; // True if the wizard is local.
    private Color colour;
    private Vector spellBook;
    private Spell selectedSpell;
    private boolean magicAttack;
    private int combatMod;
    private int defenseMod;
    private int playerNumber; // Used to identify the wizard over the network.
    
    public Wizard(String string, Color color, Square pos, Chaos chaos, boolean b, int n)
    {
    		super(chaos);  
    		local = b;
    		playerNumber = n;
        name = string;
        align = 0;
        factor = 0;
        combat = game.getDice().d(3) + 1;
        ranged = 0;
        range = 0;
        defense = game.getDice().d(3) + 1;
        move = 1;
        man = game.getDice().d(3) + 1;
        magRes = 3;
        undead = false;
        flyer = false;
        mount = false;
        magicAttack = false;
        colour = color;
        frames = new Image[4];
        setAnimation(0, 0, 0);
        square = pos;
        square.setOccupant(this);
        summoner = this;
        spellBook = new Vector(8 + game.getDice().d(6), 1);
        spellBook.addElement(new DisbelieveSpell(this));
        for (int i = 1; i < spellBook.capacity(); i++)
            spellBook.addElement(Spell.getSpell(this));
    }

		public boolean getLocal()
		{
			return local;
		}
		
		public int getPlayerNumber()
		{
			return playerNumber;
		}
		
    public Spell getSelectedSpell()
    {
        return selectedSpell;
    }
    
    public int getSelectedSpellIndex()
    {
    		if(selectedSpell == null) return -1;
   			else return spellBook.indexOf(selectedSpell);
   	}

    public void setSelectedSpell(Spell spell)
    {
        selectedSpell = spell;
    }
    
    public Spell setSelectedSpell(int index)
    {
    		if(index < 0 || index > spellBook.size()) selectedSpell = null;
    		else selectedSpell = (Spell) spellBook.elementAt(index);
    		return selectedSpell;
    }

    public Chaos getGame()
    {
        return game;
    }

    public void setMagicAttack(boolean flag)
    {
        magicAttack = flag;
    }

    public void setAnimation(int i, int j, int k)
    {
        frames[0] = game.getWizardImage(i);
        frames[1] = game.getWizardImage(j);
        frames[2] = game.getWizardImage(k);
    }

    public void adjustAttack(int i)
    {
        combat -= combatMod;
        combat += i;
        combatMod = i;
    }

    public void adjustDefense(int i)
    {
        defense -= defenseMod;
        defense += i;
        defenseMod = i;
    }

    public void makeFlyer()
    {
        flyer = true;
    }

		//	Used to allow wizard to decide whether it can move to pos.
		// 	setSquare should be used if the target is known to be OK.
		// 	Returns true if wizard has moved to new square.
    public synchronized boolean move(Square pos)
    {
        Creature creature = pos.getOccupant();
              
        // Flyer.
        if (flyer)
        {
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
                    moved = true;
                    return true;
                }
                // If enemy there, swoop attack it.
                else if(creature.summoner != summoner || creature instanceof GooeyBlob || creature instanceof MagicFire)
                {
                		whack(creature);
                		moved = true;
                    return false;
                }
                // Creature is a friend, if it's a mount, mount it.
                else if(creature instanceof Mount)
                {
                	((Mount)creature).mount(this);
                	moved = true;
                	return true;
                }
                // Tried to move onto a friendly occupied square, 
                // return false.
                else return false;
            }
            game.getFrame().getMessageArea().setText("Square out of range");
            return false;
        }
        
        // Walker.
        else if (square.adjacent(pos))
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
                // If creature is a friendly mount, mount it.
                else if(creature instanceof Mount)
                {
                		moved = true;
                		((Mount)creature).mount(this);
                    return true;
                }
                // Otherwise do not allow the move.
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

		public void paint(Graphics g)
		{
			// 	Fill a rectangle the size of the image, not the square
			//	with the wizard's colour.
			g.setColor(colour);
      g.fillRect(square.getx() * SQUAREWIDTH, square.gety() * SQUAREHEIGHT,40,40);
      
      //// 1.0 Fix ////
      //	Draw a black rectangle over the top of the wizard.
      //	Kludge fix for transparency problems with Java 1.0     
      g.setColor(Color.black);
     	g.drawLine(square.getx() * SQUAREWIDTH, square.gety() * SQUAREHEIGHT, square.getx() * SQUAREWIDTH, square.gety() * SQUAREHEIGHT);            
      //// End 1.0 Fix ////
      
      super.paint(g);
      
    }

    public void die()
    {
    		Square currentSquare;
    		
    	  // Destroy all creatures.
    		for(Enumeration e = game.getGameBoard().getCreatures(this); e.hasMoreElements();)
    		{
    			currentSquare = ((Creature) e.nextElement()).getSquare();
    			currentSquare.setOccupant(null);
    			currentSquare.paint(game.getBattleField().getBufferGraphics());
    		}
    		
    		for(Enumeration e = game.getGameBoard().getCorpses(this); e.hasMoreElements(); e = game.getGameBoard().getCorpses(this))
    		{
   				while(e.hasMoreElements())
    			{
    				currentSquare = ((Creature) e.nextElement()).getSquare();
    				currentSquare.popCorpse();
    				currentSquare.paint(game.getBattleField().getBufferGraphics());
    			}
    		}
   		
    		// Remove wizard from list of wizards.
        game.killWizard(this);
       
       	destroy();
       	
       	/*
    		square.setOccupant(null);
    		square.paint(game.getBattleField().getBufferGraphics());        
    		*/
    }

    public boolean whack(Creature creature)
    {
     		// Set up effects.
        Punch punch = new Punch(creature.getSquare());
        queEffect(punch);
        punch.waitTillDead();

				// Now carry out attack.
				if(magicAttack)
				{        

					if(magicAttack(creature, combat))
					{
						creature.die();
						return true;
					}
					else return false;
				}
				else
				{
        	if(attack(creature, combat))
        	{        
            creature.die();          
            return true;
        	}
        	else return false;
        }
    }

    public boolean cast(Square pos)
    {
        if (selectedSpell == null || !selectedSpell.cast(pos))
            return false;
        selectedSpell = null;
        return true;
    }

    public Vector getSpells()
    {
        return spellBook;
    }

    public void newSpell()
    {
        spellBook.addElement(Spell.getSpell(this));
    }

    public void removeSelectedSpell()
    {
    		if(selectedSpell != null && ! (selectedSpell instanceof DisbelieveSpell))
    		{
        	spellBook.removeElement(selectedSpell);
        	selectedSpell = null;
        }
    }

		public Color getColour() { return colour; }
}

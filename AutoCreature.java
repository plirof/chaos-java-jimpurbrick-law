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

/**
  * An abstract class that defines the update method called once per turn
  * that allows AutioCreatures to spread/dissapear or behave in whatever
  * special ways they need to.
  */
public abstract class AutoCreature extends Creature
{
		public AutoCreature(Chaos game)
		{
			super(game);
		}
		
    public abstract void update();
    public boolean move(Square pos) {return false;}
    public boolean selectForMove(Wizard wizard) {return false;}
    public void die() {destroy();}
    
    /*
    public void corpsePaint(Graphics g) 
    {
    	g.setColor(Color.black); 
    	g.fillRect(square.getx() * SQUAREWIDTH,square.gety() * SQUAREHEIGHT, 40, 40);
    }
    */
}

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

package law.ai; // Define this class as part of the law.ai package.

import law.*; // Gain access to all the public Law classes.

/**
 * Class representing an AI controlled wizard in law.
 */

public class AIWizard extends Wizard
{
  
  /**
    * Constructor. Called by Chaos when an AI player is asked for.
    * Calls parent constructor, then initialises.
    */
  public AIWizard(String string, java.awt.Color color, Square pos, 
		  Chaos chaos, boolean b, int n)
  {
    // Call parent constructor.
    super(string, color, pos, chaos, b, n);

    // Extra initialisation.
  }

  /**
   * Called by Chaos when it is the AIWizard's turn to cast.
   */
  public void doCasting()
  {
    System.out.println("AIWizard.doCasting");
  }

  /**
   * Called by Chaos when it is the AIWizard's turn to move..
   */
  public void doMovement()
  {
    System.out.println("AIWizard.doMovement");
  }

  // Implementation (private variables and methods).
}

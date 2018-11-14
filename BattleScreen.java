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
  * Class that lays out the main battlefield screen.
  */
class BattleScreen extends Panel implements Data
{
    BattleFieldAnimator field;
    Panel buttonBar;
    Button done;
    Button yes;
    Button no;
    Button cancel;

    BattleScreen(Container parent, GameBoard g)
    {
    		parent.add("battle screen",this);
        setLayout(new BorderLayout());
        buttonBar = new Panel();
        done = new Button("Done");
        yes = new Button("Yes");
        no = new Button("No");
        cancel = new Button("Cancel");
        buttonBar.add(cancel);
        buttonBar.add(done);
        add("South", buttonBar);
        field = new BattleFieldAnimator(this,g);
        add("Center", field);
    }

    public BattleFieldAnimator getField()
    {
        return field;
    }
}

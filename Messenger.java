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

class Messenger extends Panel
{
    private static TextComponent ta;
    private static String message;

    static void writeMessage(String string)
    {
        message = string;
        ta.setText(message);
    }

    static void clearMessage()
    {
        message = "";    
        ta.setText(message);
    }

    static void addToMessage(String string)
    {
        ta.setText(message + string);
    }

    static void initMessenger(TextComponent a)
    {
        ta = a;
        ta.setBackground(Color.black);
        ta.setForeground(Color.yellow);
    }

    public Messenger()
    {
    }
}

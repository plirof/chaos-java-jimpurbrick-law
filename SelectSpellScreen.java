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

/* Decompiled by Mocha from SelectSpellScreen.class */
/* Originally compiled from SelectSpellScreen.java */

package law;

import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;

class SelectSpellScreen extends Panel implements DialogListener, Data
{
    Chaos game;
    Wizard wiz;
    Enumeration wizards;
    Panel sidePanel;
    InfoScreen infoPanel;
    Panel buttonBar;
    CheckboxGroup spellList;
    Vector spells;
    Vector list;
    Button done;
    Button showSpells;

    SelectSpellScreen(Chaos chaos)
    {
        game = chaos;
        setLayout(new BorderLayout());
        sidePanel = new Panel();
        buttonBar = new Panel();
        infoPanel = new InfoScreen();
        spellList = new CheckboxGroup();
        done = new Button("Done");
        showSpells = new Button("Show spells");      
        buttonBar.add(showSpells);
        buttonBar.add(done);
        add("South", buttonBar);
        add("East", sidePanel);
        add("Center", infoPanel);
    }

    private void setWizard(Wizard wizard)
    {
        wiz = wizard;
        infoPanel.removeAll();
       	sidePanel.hide();
        sidePanel.removeAll(); 
        wiz.setSelectedSpell(null);
        game.getFrame().getMessageArea().setText(wiz.getName() + " spell select");       
        spells = wiz.getSpells();
        sidePanel.setLayout(new GridLayout(spells.size() + 1, 1));
        list = new Vector(spells.size() + 1);        
        ColourCheckbox c = new ColourCheckbox("none",spellList,true);
        sidePanel.add(c);
        for(Enumeration e = spells.elements(); e.hasMoreElements();)
        {
        		ColourCheckbox c1 = new ColourCheckbox(((Spell) e.nextElement()).getName(),spellList,false);
         		list.addElement(c1.getCheckbox());
            sidePanel.add(c1);
        }
        sidePanel.validate();
    }
    
    public void setWizards(Enumeration e)
    {
    		wizards = e;
    		if(wizards != null && wizards.hasMoreElements())
        {
         	setWizard((Wizard) wizards.nextElement());
        }
        else game.endSpellSelect();
    }

    public boolean action(Event event, Object object)
    {
        if (event.target instanceof Checkbox)
        {
        		int index = list.indexOf(spellList.getCurrent());
            if (index < 0)
            {
                infoPanel.removeAll();
                wiz.setSelectedSpell(null);
            }
            else
            {
            		Spell spell = (Spell) spells.elementAt(index);
                wiz.setSelectedSpell(spell);
                infoPanel.setSelected(spell);
            }
            infoPanel.repaint();
        }
        if (event.target == done)
        {   
            if(wiz.getSelectedSpell() instanceof SummonSpell)
            {
               YesNoDialog d = new YesNoDialog("Illusion?",this,game.getFrame());
               return true;
            }
            if(wizards != null && wizards.hasMoreElements())
            {
            	setWizard((Wizard) wizards.nextElement());
            }
            else game.endSpellSelect();
            return true;
        }
        if (event.target == showSpells)
        {
        		sidePanel.show();
        		validate();
            return true;
        }
        return false;
    }
    
    public void handleDialog(String choice)
    {
    	if(choice.equals("Yes"))
    	{
    		((SummonSpell)wiz.getSelectedSpell()).setIllusion(true);
    	}
    	if(wizards != null && wizards.hasMoreElements())
      {
      	setWizard((Wizard) wizards.nextElement());
     	}
      else game.endSpellSelect();
    }
}

class ColourCheckbox extends Panel implements Data
{
	ColourLabel label;
	Checkbox box;
	
	public ColourCheckbox(String s,CheckboxGroup g, boolean b)
	{
		setLayout(new FlowLayout(FlowLayout.LEFT));
		label = new ColourLabel(s);
		box = new Checkbox();
		box.setCheckboxGroup(g);
		box.setState(b);
		box.setBackground(BACKGROUND);
		box.setForeground(FOREGROUND);
		add(box);
		add(label);
	}
	
	public Checkbox getCheckbox()
	{
		return box;
	}
	
}

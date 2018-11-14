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

//-----------------------------------------------------------------------
//	PlayerEntryScreen 
//
//	Project: Law
//	Author:	 James Purbrick (jcp@cs.nott.ac.uk)
//	Last Revision:	18/6/02
//
//	Notes:

package law;

import java.awt.*;
import java.net.*;
import java.util.Vector;
import java.io.IOException;

// Import law AI package.
import law.ai.*;

class PlayerEntryScreen extends Panel implements Data, NetGameListener
{
		private InetAddress server;
		private Panel buttonBar;
		private Button start;
		private Button menu;
		private LawFrame frame;
    
    //// 1.0 Fix ////
    private WizardPicturePanel pictures[];
    //// End 1.0 Fix ////
    
    private Image wizardPic;
    private TextField names[];
    private Checkbox selected[];
    private Panel mainPanel;
    private GridBagLayout gridbag;
    private NetGame netGame;
    private Panel menuScreen;
    private int nPlayers;
    private boolean ai; // True if pad with AI players.
    
    PlayerEntryScreen(InetAddress address, int port, LawFrame lf, Panel p, boolean b) throws IOException
    {
    		nPlayers = 0;
    		frame = lf;
		ai = (frame.getLoader().getProperty("ai") != null);
    		server = address;
    		menuScreen = p;
        wizardPic = frame.getLoader().getImage("wizard.gif");        
        gridbag = new GridBagLayout();
        setLayout(new BorderLayout());
        mainPanel = new Panel();
        mainPanel.setLayout(gridbag);
        
       	buttonBar = new Panel();
    		start = new Button("Start");
		// Allow start at any time, so 8 AI games can be played.
    		start.enable(ai); 
    		buttonBar.add(start);      
       
        if(menu != null)
	{
    	  menu = new Button("Menu");
    	  buttonBar.add(menu);
        }

        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.anchor = 10;
        gridBagConstraints1.fill = 2;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.fill = 0;
        gridBagConstraints2.insets = new Insets(5, 5, 5, 20);
        gridBagConstraints2.gridheight = 2;
        gridBagConstraints2.weightx = 0.0;
        gridBagConstraints2.weighty = 0.0;
        
        //// 1.0 Fix ////
        pictures = new WizardPicturePanel[MAXPLAYERS];
        //// End Fix ////
        
        selected = new Checkbox[MAXPLAYERS];
        names = new TextField[MAXPLAYERS];
        for (int i = 0; i < MAXPLAYERS; i++)
        {
        		gridBagConstraints1.gridx = i / 4 * 3;
        		gridBagConstraints1.gridy = i * 2 % 8 + 1;
        		selected[i] = new Checkbox();
        		selected[i].setBackground(BACKGROUND);
            selected[i].setForeground(FOREGROUND);
            gridbag.setConstraints(selected[i], gridBagConstraints1);
            mainPanel.add(selected[i]);
            
            gridBagConstraints1.gridx = i / 4 * 3 + 1;
            gridBagConstraints1.gridy = i * 2 % 8 + 1;
            names[i] = new TextField(15);
            names[i].setBackground(BACKGROUND);
            names[i].setForeground(FOREGROUND);
            gridbag.setConstraints(names[i], gridBagConstraints1);
            mainPanel.add(names[i]);
            
            //// 1.0 Fix ////
            pictures[i] = new WizardPicturePanel(wizardPic, colours[i], 40, 40);
            //// End Fix ////
            
            gridBagConstraints2.gridx = i / 4 * 3 + 2;
            gridBagConstraints2.gridy = i * 2 % 8;            
            gridbag.setConstraints(pictures[i], gridBagConstraints2);
            mainPanel.add(pictures[i]);
        }
        
        add("Center", mainPanel);
        add("South", buttonBar);
    		validate();   

				if(server == null)
				{
        	netGame = new NetGame(this);
        }
        else
        { 
     			netGame = new NetGame(this,server,port,frame);
     			frame.addThread(netGame.getThreadable());
     			netGame.getChatWindow().setBackground(BACKGROUND);
     			netGame.getChatWindow().setForeground(FOREGROUND);
     		}
    }
    
    public boolean action(Event event, Object object)
    {
    	int counter;
    	
      for(counter = 0; counter < MAXPLAYERS; counter++)
      {
      	if(event.target == selected[counter])
       	{
       		if(names[counter].getText().length() > 0)
       		{
	      		if(selected[counter].getState()) 
	       		{
							netGame.sendAddPlayer(names[counter].getText(),counter);
	       		}
	       		else
	       		{
	       			netGame.sendRemovePlayer(counter, names[counter].getText());
	       		}
	       	}
	       	else selected[counter].setState(false);
       		return true;
       	}
      }
      if(event.target == start)
    	{
    		netGame.sendStartGame();
     		return true;
    	}
    	if(event.target == menu)
    	{
    		frame.stopThreads();
    		frame.remove(this);
    		frame.add("Center", menuScreen);
    	}	
      return false;
   	}
   	
   	public Dimension preferredSize()
  	{
      return minimumSize();
  	}
    
  	public Dimension minimumSize()
  	{
  		return screenSize;        
  	}
   	
		//-------------------------------------------------------------------------
		//	NetGameListener Methods.
		//-------------------------------------------------------------------------
    		
    public void handleCommand(String command) {}

		public void startGame(int randomSeed)
		{
		  // There are always 8 players, as we pad human players
		  // with AIs.
		  if(ai) nPlayers = 8;

			frame.remove(this);
								
			// Make the Chaos object - how exciting!
			Chaos game = new Chaos(netGame, frame);
			
			// Start positions of wizards.
			Square NW = game.getGameBoard().getSquare(0,0);
	    Square N  = game.getGameBoard().getSquare(7,0);
	    Square NE = game.getGameBoard().getSquare(14,0);
	    Square E  = game.getGameBoard().getSquare(14,5);
	    Square SE = game.getGameBoard().getSquare(14,9);
			Square S  = game.getGameBoard().getSquare(7,9);
			Square SW = game.getGameBoard().getSquare(0,9);
			Square W  = game.getGameBoard().getSquare(0,5);
			Square positions[][] = { new Square[0], { N }, { W, E }, { W, NE, SE }, { NW, NE, SW, SE }, { NW, N, NE, SW, SE }, { NW, N, NE, SW, S, SE }, { NW, N, NE, W, SW, S, SE }, { NW, N, NE, W, E, SW, S, SE } };
			
			game.getDice().setSeed(randomSeed);
			int counter, currentPlayer = 0;
			
			System.out.println("Starting " + nPlayers + " player game.");
			
			// Add wizards for each name to game.		
			for(counter = 0; counter < MAXPLAYERS; counter++)
			{			
				if(names[counter].getText().length() > 0)
				{
					game.addWizard(new Wizard(names[counter].getText(), colours[counter], positions[nPlayers][currentPlayer], game, selected[counter].isEnabled(), counter));
					currentPlayer++;
				}
				else if(ai && netGame.isBoss()) // Pad with AIs.
				{
				  game.addWizard(new AIWizard("AIWizard", colours[counter], positions[nPlayers][currentPlayer], game, true, counter));
				  currentPlayer++;
				}
			}
			
			netGame.setListener(game);
			game.startGame(randomSeed);
		}

		public void addPlayer(int i,String s,boolean local)
		{
			nPlayers++;
    	names[i].setText(s);
    	names[i].enable(false);
    	repaint();
      selected[i].setState(true);
      selected[i].enable(local);
      // If AI, allow start at any time, so 8 AI games can be played.
	// NOTE: Using boss status as enable mechanism is broken.
	// Boss status change method in NetGameListener should be
	// called by NetGame when boss status changes.
      start.enable(ai || (/*netGame.isBoss() && */ nPlayers >= 2));
    }

		public void removePlayer(int i)
		{	
			nPlayers--;
    	names[i].setText("");
    	names[i].enable(true);
    	repaint();
    	selected[i].setState(false);
    	selected[i].enable(true);
	// If AI, allow start at any time, so 8 AI games can be played.
	// NOTE: Using boss status as enable mechanism is broken.
	// Boss status change method in NetGameListener should be
	// called by NetGame when boss status changes.
   	start.enable(ai || (/*netGame.isBoss() && */ nPlayers >= 2));
    }

		public void nextTurn() {}
}

//// 1.0 Fix ////
// Class needed because of transparency problems with java 1.0
// Normally use PicturePanel.
class WizardPicturePanel extends Canvas
{
    private int width;
    private int height;
    private Color background;
    private Image image;

    WizardPicturePanel(Image image, Color color, int i, int j)
    {
    		this.image = image;
    		
    		// Use an image tracker to load the image so that we don't
    		// get a coloured square appearing, then the image overlaid
    		// - it looks slicker this way.
   			MediaTracker m = new MediaTracker(this);
    		m.addImage(image,0);
    		try {m.waitForAll();}
    		catch(Exception e) {;}
        width = i;
        height = j;
        background = color;
    }

    public void paint(Graphics g)
    {
        g.setColor(background);
        g.fillRect(0, 0, size().width, size().height);
        g.setColor(Color.black);
        g.drawLine(0,0,0,0);
        g.drawImage(image, 0, 0, this);
    }
	
    public Dimension preferredSize()
    {
        return minimumSize();
    }
    
    public Dimension minimumSize()
    {
    	return new Dimension(width, height);        
    }
}

//// End Fix ////

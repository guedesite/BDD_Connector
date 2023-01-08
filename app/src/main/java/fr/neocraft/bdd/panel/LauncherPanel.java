package fr.neocraft.bdd.panel;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import com.spinyowl.legui.component.Button;
import com.spinyowl.legui.component.Component;
import com.spinyowl.legui.component.Label;
import com.spinyowl.legui.component.Panel;
import com.spinyowl.legui.component.PasswordInput;
import com.spinyowl.legui.component.TextComponent;
import com.spinyowl.legui.component.TextInput;
import com.spinyowl.legui.component.Widget;
import com.spinyowl.legui.component.optional.align.HorizontalAlign;
import com.spinyowl.legui.component.optional.align.VerticalAlign;
import com.spinyowl.legui.event.MouseClickEvent;
import com.spinyowl.legui.listener.MouseClickEventListener;
import com.spinyowl.legui.style.Background;
import com.spinyowl.legui.style.color.ColorConstants;
import com.spinyowl.legui.style.font.FontRegistry;

import fr.neocraft.bdd.main;
import fr.neocraft.bdd.component.ComponentCursorCustom;
import fr.neocraft.bdd.util.BDD;
import fr.neocraft.bdd.util.BDDConnection;
import fr.neocraft.bdd.util.CRASH;
import fr.neocraft.bdd.util.NBT;


public class LauncherPanel extends Panel{

	private ArrayList<Component> accueil = new ArrayList<Component>();
	private ArrayList<Component> enregistrer = new ArrayList<Component>();
	private ArrayList<Component> connection = new ArrayList<Component>();
	

	
	public LauncherPanel(int frameX, int frameY, int width, int height) {
		super(0,0, width, height);
		this.getStyle().getBackground().setColor(ColorConstants.white());
		
	
		
		Label Titre = new Label();

	
			Titre.setSize(950, 135);
			Titre.setPosition(157, 150);
			Titre.getTextState().setText("BDD CONNECTOR");
			Titre.getStyle().setHorizontalAlign(HorizontalAlign.CENTER);
			Titre.getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
			Titre.getStyle().setFontSize(90F);
			Titre.getStyle().setFont(FontRegistry.ROBOTO_BOLD);
			accueil.add(Titre);
			
			 Button ConnectionButton = new Button();
	        ConnectionButton.setSize(493, 70);
	        ConnectionButton.setPosition(388, 325);
	        ConnectionButton.getTextState().setText("Se connecter à une BDD");
	        ConnectionButton.getStyle().setHorizontalAlign(HorizontalAlign.CENTER);
	        ConnectionButton.getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
	        ConnectionButton.getStyle().setFontSize(50F);
	        
	        ConnectionButton.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
	        	this.removeAll(accueil);
	        	this.addAll(connection);
	         });
	        main.ComponentCursorHover.add(new ComponentCursorCustom(ConnectionButton, main.cursorHover));
	        
	        accueil.add(ConnectionButton);
	        
	        
	        Button  EnregistrerButton = new Button();
	        EnregistrerButton.setSize(463, 70);
	        EnregistrerButton.setPosition(406, 420);
	        EnregistrerButton.getTextState().setText("Enregistrer une BDD");
	        EnregistrerButton.getStyle().setHorizontalAlign(HorizontalAlign.CENTER);
	        EnregistrerButton.getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
	        EnregistrerButton.getStyle().setFontSize(50F);

	        
	        EnregistrerButton.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
	        
	        	this.removeAll(accueil);
	        	this.addAll(enregistrer);
	         });
	        
	        main.ComponentCursorHover.add(new ComponentCursorCustom(EnregistrerButton, main.cursorHover));
	        
	        accueil.add(EnregistrerButton);
	        
	        this.addAll(accueil);
	        
	        TextInput Name = new TextInput("Default", 428, 120, 423, 40);
	        putStyleText(Name, 30);
	        TextInput url = new TextInput("IP:PORT", 428, 180, 423, 40);
	        putStyleText(url, 30);
	        TextInput user = new TextInput("USER", 428, 240, 423, 40);
	        putStyleText(user, 30);
	        TextInput table = new TextInput("TABLE", 428, 300, 423, 40);
	        putStyleText(table, 30);
	        PasswordInput mdp = new PasswordInput("MDP", 428, 360, 423, 40);
	        putStyleText(mdp, 30);
	        
	        main.ComponentCursorHover.add(new ComponentCursorCustom(Name, main.cursorWrite));
	        main.ComponentCursorHover.add(new ComponentCursorCustom(url, main.cursorWrite));
	        main.ComponentCursorHover.add(new ComponentCursorCustom(user, main.cursorWrite));
	        main.ComponentCursorHover.add(new ComponentCursorCustom(table, main.cursorWrite));
	        main.ComponentCursorHover.add(new ComponentCursorCustom(mdp, main.cursorWrite));
	        enregistrer.add(Name);
	        enregistrer.add(url);
	        enregistrer.add(user);
	        enregistrer.add(table);
	        enregistrer.add(mdp);
	        
	        Button testco = new Button("Tester", 408, 440, 463, 50);
	        putStyleText(testco, 40);
	        testco.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
	        	BDD bddTest = new BDD(false);
	        	try {
					bddTest.Openbdd(new BDDConnection(
							url.getTextState().getText(),
							user.getTextState().getText(),
							table.getTextState().getText(), 
							mdp.getTextState().getText(),
							"default"));
							
					testco.getTextState().setText("Connection établie avec succès");
					testco.getStyle().getBackground().setColor(ColorConstants.green());
					new Timer().schedule(new TimerTask() {

						@Override
						public void run() {
							testco.getTextState().setText("Tester");
							testco.getStyle().getBackground().setColor(ColorConstants.white());
						}
						
					}, 3000);
	        	} catch (Exception e) {
					CRASH.Push(e);
					testco.getTextState().setText("ERREUR, bdd injoignable");
					testco.getStyle().getBackground().setColor(ColorConstants.red());
					new Timer().schedule(new TimerTask() {

						@Override
						public void run() {
							testco.getTextState().setText("Tester");
							testco.getStyle().getBackground().setColor(ColorConstants.white());
						}
						
					}, 3000);
	        	}
	        
	        });
	        main.ComponentCursorHover.add(new ComponentCursorCustom(testco, main.cursorHover));
	        enregistrer.add(testco);
	        
	        Button Saveco = new Button("Enregistrer", 408, 500, 463, 50);
	        putStyleText(Saveco, 40);
	        Saveco.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
	        	
	        	boolean flag = true;
	        	for(BDDConnection c:main.allConnection)
	        	{
	        		if(c.FictifName.equals(Name.getTextState().getText()))
	        		{
	        			flag = false;
	        		}
	        	}

	        	
		        	BDDConnection bddco = new BDDConnection(
							url.getTextState().getText(),
							user.getTextState().getText(),
							table.getTextState().getText(), 
							mdp.getTextState().getText(),
							Name.getTextState().getText());
		        	if(flag)
		        	{
		        		main.allConnection.add(bddco);
		        	} else {
		        		for(int i = 0; i < main.allConnection.size(); i++)
		        		{
		        			if(main.allConnection.get(i).FictifName.equals(Name.getTextState().getText()))
			        		{
		        				main.allConnection.set(i, bddco);
			        		}
		        		}
		        	}
		        	NBT.saveNBTConnexion(bddco);
		        	Saveco.getTextState().setText("Connection enregistré !");
		        	Saveco.getStyle().getBackground().setColor(ColorConstants.green());
		        	main.initConnection();
				 	putWidgetCo(Name, url, user,table, mdp);
					new Timer().schedule(new TimerTask() {

						@Override
						public void run() {
							Saveco.getStyle().getBackground().setColor(ColorConstants.white());
							Saveco.getTextState().setText("Enregistrer");
						}
						
					}, 3000);
		        
	        });
	        main.ComponentCursorHover.add(new ComponentCursorCustom(Saveco, main.cursorHover));
	        enregistrer.add(Saveco);
	        Button Backco = new Button("Retour", 428, 560, 423, 50);
	        putStyleText(Backco, 40);
	        Backco.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
	        	
	        	this.removeAll(enregistrer);
	        	this.addAll(accueil);
		        
	        });
	        main.ComponentCursorHover.add(new ComponentCursorCustom(Backco, main.cursorHover));
	        enregistrer.add(Backco);
	        
	        
	        Button ConnectToSelect = new Button("Connection", 408, 560, 463, 50);
	        putStyleText(ConnectToSelect, 40);
	        ConnectToSelect.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
	        
	        	
		        	if(main.bdd.IsOpen)
		        	{
		        		main.bdd.Closebdd();
		        	}
		        	try {
						main.bdd.Openbdd(selectedCo);
						ConnectToSelect.getStyle().getBackground().setColor(ColorConstants.green());
						main.frame.getContainer().clearChildComponents();
						main.frame.getContainer().add(main.BDDpanel);
						main.BDDpanel.Init();
						main.IsBddPanel = true;
						main.frame.getContainer().add(main.fpsPour);
						
					} catch (Exception e) {
					
						CRASH.Push(e);
						ConnectToSelect.getStyle().getBackground().setColor(ColorConstants.red());
						main.IsBddPanel = false;
						new Timer().schedule(new TimerTask() {

							
							@Override
							public void run() {
								ConnectToSelect.getStyle().getBackground().setColor(ColorConstants.white());
							}
							
						}, 3000);
					}
	        	
		        
	        });
	        
	        connection.add(ConnectToSelect);
	        
	        
	        Button ReturnToSelect = new Button("Retour", 428, 620, 423, 50);
	        putStyleText(ReturnToSelect, 40);
	        ReturnToSelect.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
	        
	        	this.removeAll(connection);
	        	this.addAll(accueil);
		        
	        });
	        
	        connection.add(ReturnToSelect);

	       
	       Label Titre2 = new Label();
	        
	    	
			Titre2.setSize(950, 135);
			Titre2.setPosition(157, 20);
			Titre2.getTextState().setText("BDD CONNECTOR");
			Titre2.getStyle().setHorizontalAlign(HorizontalAlign.CENTER);
			Titre2.getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
			Titre2.getStyle().setFontSize(90F);
			Titre2.getStyle().setFont(FontRegistry.ROBOTO_BOLD);
	       
	       connection.add(Titre2);
	       
	       putWidgetCo(Name, url, user, table, mdp);
	   
	}

	private Widget allco;
	
	int idd = 1;
	
	private void addBddConnection(BDDConnection bo, TextComponent Name, TextComponent url, TextComponent user, TextComponent table, TextComponent mdp) {
		Button TempEdit = new Button("EDITER",398,0+(idd-1)*25,30,25);
		TempEdit.getStyle().setTextColor(0, 0, 0, 1);
		TempEdit.getStyle().getBackground().setColor(255, 255, 255, 1);
		 putStyleText(TempEdit, 16);
		 
		 TempEdit.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
			
			 Name.getTextState().setText(bo.FictifName);
			 url.getTextState().setText(bo.getURL());
			 user.getTextState().setText(bo.getUser());
			 table.getTextState().setText(bo.getTable());
			 mdp.getTextState().setText(bo.getMDP());
			 this.removeAll(connection);
			this.addAll(enregistrer);
			
			 
	        });
		 allco.add(TempEdit);
		 
		 Button TempSupp = new Button("SUPP",428,0+(idd-1)*25,35,25);
		 TempSupp.getStyle().setTextColor(0, 0, 0, 1);
		 TempSupp.getStyle().getBackground().setColor(255, 255, 255, 1);
		 putStyleText(TempSupp, 16);
		 
		 TempSupp.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
			
			 	new File("assets/co/"+bo.FictifName+".dat").delete();
			 	main.initConnection();
			 	this.removeAll(connection);
			 	putWidgetCo(Name, url, user, table, mdp);
			 	this.addAll(connection);
			 
			 
	        });
		 allco.add(TempSupp);
		 
		 
		 Button TempCoBdd = new Button(
					bo.FictifName,0,0+(idd-1)*25,398,25);
		 	TempCoBdd.getStyle().setTextColor(0, 0, 0, 1);
		 	TempCoBdd.getStyle().getBackground().setColor(255, 255, 255, 1);
			 putStyleTextAlignLeft(TempCoBdd, 20);
			 
			 
			 TempCoBdd.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
				
			    	 selectedCo = bo;
			    	 
			   
				    	 for(Component c:allco.getChildComponents())
				    	 {
				    		 c.getStyle().getBackground().setColor(ColorConstants.white());
				    	 }
				    	 TempEdit.getStyle().getBackground().setColor(ColorConstants.green());
				    	 TempSupp.getStyle().getBackground().setColor(ColorConstants.green());
				    	 TempCoBdd.getStyle().getBackground().setColor(ColorConstants.green());
			    	 
		        });
			 allco.add(TempCoBdd);
		 
		 idd++;
	}
	
	private void putWidgetCo(TextComponent Name, TextComponent url, TextComponent user, TextComponent table, TextComponent mdp) {
		if(allco != null) {
			connection.remove(allco);
		}
		allco = new Widget(408, 200, 463, 350);
        allco.setDraggable(false);
        allco.setResizable(false);
        allco.setMinimizable(false);
        allco.getStyle().getBackground().setColor(255, 255, 255, 1);
		
        idd = 1;
        for(BDDConnection bo:main.allConnection)
        {
        	addBddConnection(bo,Name, url, user, table, mdp);
        }
        connection.add(allco);

	}
	
	private synchronized void AfterputWidgetCo(TextComponent name, TextComponent url, TextComponent user,
			TextComponent table, TextComponent mdp) {
		putWidgetCo( name, url, user,
				 table,  mdp);
		
	}
	
	private BDDConnection selectedCo;
	
	private void putStyleText(Component c,float font) {
		c.getStyle().setHorizontalAlign(HorizontalAlign.CENTER);
        c.getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
        c.getStyle().setFontSize(font);
	}
	
	private void putStyleTextAlignLeft(Component c,float font) {
		c.getStyle().setHorizontalAlign(HorizontalAlign.LEFT);
        c.getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
        c.getStyle().setFontSize(font);
	}
	
}

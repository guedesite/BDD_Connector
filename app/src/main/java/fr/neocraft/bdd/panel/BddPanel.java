package fr.neocraft.bdd.panel;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.joml.Vector2f;
import org.joml.Vector4f;

import com.spinyowl.legui.animation.Animation;
import com.spinyowl.legui.component.Button;
import com.spinyowl.legui.component.Component;
import com.spinyowl.legui.component.Label;
import com.spinyowl.legui.component.Panel;
import com.spinyowl.legui.component.RadioButton;
import com.spinyowl.legui.component.RadioButtonGroup;
import com.spinyowl.legui.component.ScrollablePanel;
import com.spinyowl.legui.component.TextInput;
import com.spinyowl.legui.component.ToggleButton;
import com.spinyowl.legui.component.Tooltip;
import com.spinyowl.legui.component.Widget;
import com.spinyowl.legui.component.event.scrollbar.ScrollBarChangeValueEvent;
import com.spinyowl.legui.component.event.scrollbar.ScrollBarChangeValueEventListener;
import com.spinyowl.legui.component.event.textinput.TextInputContentChangeEvent;
import com.spinyowl.legui.component.event.textinput.TextInputContentChangeEventListener;
import com.spinyowl.legui.component.optional.align.HorizontalAlign;
import com.spinyowl.legui.component.optional.align.VerticalAlign;
import com.spinyowl.legui.event.CursorEnterEvent;
import com.spinyowl.legui.event.MouseClickEvent;
import com.spinyowl.legui.icon.Icon;
import com.spinyowl.legui.icon.ImageIcon;
import com.spinyowl.legui.image.LoadableImage;
import com.spinyowl.legui.image.loader.ImageLoader;
import com.spinyowl.legui.listener.CursorEnterEventListener;
import com.spinyowl.legui.listener.MouseClickEventListener;
import com.spinyowl.legui.style.Style.DisplayType;
import com.spinyowl.legui.style.color.ColorConstants;

import fr.neocraft.bdd.main;
import fr.neocraft.bdd.component.CustomTextInput;
import fr.neocraft.bdd.util.BDDColumCache;
import fr.neocraft.bdd.util.CRASH;
import fr.neocraft.bdd.util.NBT;

public class BddPanel extends Panel {
	
	private Button execute;
	
	private String condition ="1";
	private boolean isValideCondition = true;
	private Widget back;
	private  TextInput condi;
	
	
	public BddPanel(int width, int height) {
		super(0,0, width, height);
		
		this.getStyle().getBackground().setColor(ColorConstants.gray());
		
		Button Deco = new Button("Déconnexion",0,0,150,30);
		putStyleText(Deco, 24);
		
		Deco.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
		
			main. bdd.Closebdd();
			main.frame.getContainer().clearChildComponents();
			main.frame.getContainer().add(main.panel);
			main.IsBddPanel = false;
	        });
		
		this.add(Deco);

		
		back = new Widget(150,0, width-150, 30);
		 back.getStyle().setBorder(null);
		 back.getStyle().getBackground().setColor(ColorConstants.white());

		 back.setDraggable(false);
		 back.setCloseable(false);
		 back.setResizable(false);
		
		Button actualiser = new Button("Actualiser",0,0,75,30);
		putStyleText(actualiser, 16);
		
		actualiser.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
			if(ActualTable != null) {
				InsideTablePanel.remove(ActualTable);
				cache = null;
				try {
					setActualTableOpen(ActualTable);
				} catch (Exception e) {
					CRASH.Push(e);
				}
			}
			
			
			
			
			
	    });
		
		
		
		back.add(actualiser);
		
		Button reset = new Button("Reset",75,0,50,30);
		putStyleText(reset, 16);
		
		reset.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event2) -> {

				InsideTablePanel.clear();
				allTable.getContainer().clearChildComponents();
				condition= "1";
				condi.getTextState().setText("1");
				Init();
				try {
					if(ActualTable != null) {
						setActualTableOpen(ActualTable);
					}
				} catch (Exception e) {
					CRASH.Push(e);
				}
	
			
			
			
			
			
	    });
		back.add(reset);
		
		
		 RadioButton UseCache = new RadioButton("Cache", 175, 0, 75, 14);
	      RadioButton NotUseCache = new RadioButton("Query", 175, 16, 75, 14);
	
		ToggleButton OptiRender = addToggleOption("Optimisé le rendu ? Recommandé pour les grosses table. Sinon à éviter.", NBT.getBool("optimize"), 125,0, 50, 30, 30);
		PregetSlideImageOnClick(OptiRender, OptiRender.getStyle().getBackground().getIcon(), true).startAnimation();
		
		OptiRender.getListenerMap().addListener(CursorEnterEvent.class, (CursorEnterEventListener) (CursorEnterEvent event) -> {
				
				if(OptiRender.isToggled())
				{
					UseCache .getStyle().setDisplay(DisplayType.MANUAL);
					NotUseCache .getStyle().setDisplay(DisplayType.MANUAL);
					main.OptimiseRender = true;
				} else {
					UseCache .getStyle().setDisplay(DisplayType.NONE);
					NotUseCache .getStyle().setDisplay(DisplayType.NONE);
					main.OptimiseRender = false;
				}
				 NBT.setBool("optimize", main.OptimiseRender);
				if(ActualTable != null) {
					
				
					try {
						setActualTableOpen(ActualTable);
					} catch (Exception e) {
						CRASH.Push(e);
					}
				}
				
		    });
		
		back.add(OptiRender);
		
		 RadioButtonGroup radioButtonGroup = new RadioButtonGroup();

		 
		

		 
         UseCache.getStyle().setFontSize(16F);
         NotUseCache.getStyle().setFontSize(16F);
         
         UseCache.setChecked(NBT.getBool("usecache"));
         radioButtonGroup.setSelection(UseCache, NBT.getBool("usecache"));
         NotUseCache.setChecked(!NBT.getBool("usecache"));
         radioButtonGroup.setSelection(NotUseCache, NBT.getBool("usecache"));
         UseCache.setRadioButtonGroup(radioButtonGroup);
         NotUseCache.setRadioButtonGroup(radioButtonGroup);
         
         UseCache.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
 			
        	 if(UseCache.isChecked())
        	 {
        		 main.UseCache = true;
        	 } else {
        		 main.UseCache = false;
        	 }
        	 NBT.setBool("usecache", main.UseCache );
        	 if(ActualTable != null) {
					
 				
					try {
						setActualTableOpen(ActualTable);
					} catch (Exception e) {
						CRASH.Push(e);
					}
				}
				
		  });
         
         
         
         NotUseCache.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
  			
        	 if(!UseCache.isChecked())
        	 {
        		 main.UseCache = true;
        	 } else {
        		 main.UseCache = false;
        	 }
        	 if(ActualTable != null) {
					
 				
					try {
						setActualTableOpen(ActualTable);
					} catch (Exception e) {
						CRASH.Push(e);
					}
				}
				
		  });
         
         back.add(UseCache);
         back.add(NotUseCache);
		
        condi = new TextInput("1", width - 375, 0, 150, 30);
         condi.setTooltip(new Tooltip("Condition du query"));
         condi.getTooltip().setPosition(0, 30);
         condi.getTooltip().getStyle().setFontSize(18F);
         condi.getTooltip().getStyle().getBackground().setColor(ColorConstants.gray());
         condi.getTooltip().setSize(150, 50);
         putStyleTextAlignLeft( condi, 16);
         
         condi.getListenerMap().addListener(TextInputContentChangeEvent.class, (TextInputContentChangeEventListener) (TextInputContentChangeEvent event) -> {
		
        	 if(ActualTable != null)
        	 {
	        	 if(main.bdd.checkIfIsValidQuery("SELECT * FROM `"+ActualTable+"` WHERE "+condi.getTextState().getText()))
	        	 {
	        		 condition = condi.getTextState().getText();
	        		 condi.getStyle().getBackground().setColor(ColorConstants.white());
	        		 try {
							setActualTableOpen(ActualTable);
						} catch (Exception e) {
							CRASH.Push(e);
						}
	        	 }else {
	        		 condi.getStyle().getBackground().setColor(ColorConstants.red());
	        	 }
        	 }
        	 
		    });
         
         back.add(condi);
         this.add(back);
        execute = new Button("éxécuter",width - 225, 0, 75, 30);
         
         putStyleText( execute, 16);
         execute.getStyle().getBackground().setColor(ColorConstants.green());
 		
         execute.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event2) -> {

        	 if(!UpdateBDD.isEmpty() && ActualTable != null)
        	 {
        		 
	        	 for(int i = 0; i< UpdateBDD.size(); i++)
	        	 {
	        		
	        		 main.bdd.update("UPDATE `"+ActualTable+"` SET "+UpdateBDD.get(i).getUpdateString()+" WHERE id="+UpdateBDD.get(i).co.id);
	        		 UpdateBDD.get(i).co.getStyle().getBackground().setColor(ColorConstants.white());
	        	 }
	        	 UpdateBDD.clear();
	        	 this.remove(execute);
        	 }
 			
 			
 	    });
 		
         
		allCol = new ScrollablePanel(160, 95, 1115,615);
		allCol.setHorizontalScrollBarVisible(false);
		allCol.getStyle().getBackground().setColor(ColorConstants.white());
		allCol.getStyle().setBorder(null);
		allCol.getContainer().getStyle().setBorder(null);
		allCol.getContainer().getStyle().getBackground().setColor(ColorConstants.white());
		allCol.setVerticalScrollBarVisible(false);
		
		allNameCol = new ScrollablePanel(160, 45, 1115,HeightTablePanel+10);
		allNameCol.setHorizontalScrollBarVisible(false);
		allNameCol.getStyle().getBackground().setColor(ColorConstants.white());
		allNameCol.getStyle().setBorder(null);
		allNameCol.getContainer().getStyle().setBorder(null);
		allNameCol.getContainer().getStyle().getBackground().setColor(ColorConstants.white());
		allNameCol.setVerticalScrollBarVisible(false);
		
		
		allTable = new ScrollablePanel(0,30,150,main.HEIGHT-30);
		allTable.getStyle().getBackground().setColor(ColorConstants.white());
		allTable.getStyle().setBorder(null);
		allTable.getContainer().getStyle().setBorder(null);
		allTable.getContainer().getStyle().getBackground().setColor(ColorConstants.white());
		
		this.add(allTable);
	}
	
	private ScrollablePanel allTable;
	
	public void Init() {

		
		this.remove(allTable);
		int i = 0;
		try {
			ArrayList<String> all = main.bdd.getAllTable();
			//all.clear();
			//all.add("Point");
			System.out.println(Arrays.toString(all.toArray()));
			int height = all.size() * 20;
			allTable.setAutoResize(false);
			allTable.setSize(150,main.HEIGHT-30);
			allTable.getContainer().setSize(150, height);
			allTable.setHorizontalScrollBarVisible(false);
			int left = 0;
			if(height > main.HEIGHT-30)
			{

				allTable.setVerticalScrollBarVisible(true);
				allTable.getVerticalScrollBar().setSize(12, 60);
				allTable.getVerticalScrollBar().getListenerMap().addListener(ScrollBarChangeValueEvent.class, (ScrollBarChangeValueEventListener) (ScrollBarChangeValueEvent event) -> {
					
					allTable.getVerticalScrollBar().setCurValue(event.getNewValue());
			    });
				//allTable.getVerticalScrollBar().setPosition(0,20);
				left = 12;
			} else {
				allTable.setVerticalScrollBarVisible(false);
				
			}
			System.out.println(all.toString());
			for(String s:all) {
				Button table = new Button(s,left,20*i,150,20);
				table.getStyle().setTextColor(0, 0, 0, 1);
				table.getStyle().getBackground().setColor(255, 255,255,1);
				putStyleTextAlignLeft( table, 18);
				table.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
			
					for(Component c:allTable.getContainer().getChildComponents())
					{
						c.getStyle().getBackground().setColor(ColorConstants.white());
					}
					try {
						this.remove(allNameCol);
						this.remove(allCol);
						setActualTableOpen(s);
					} catch(Exception e) {
						CRASH.Push(e);
					}
					table.getStyle().getBackground().setColor(ColorConstants.green());
			     });
				allTable.add(table);
				i++;
			}
			allTable.getContainer().setSize(150,height);
		} catch (Exception e) {
			
			CRASH.Push(e);
		}
		this.add(allTable);
	}
	
	public HashMap<String, BDDColumCache> InsideTablePanel = new HashMap<String, BDDColumCache>();
	public HashMap<String, Float> CurValue = new HashMap<String, Float>();
	public int widthTablePanel = 150;
	public int HeightTablePanel = 30;
	public int FontTablePanel = 20;
	public String ActualTable;
	public ScrollablePanel allNameCol, allCol;
	private BDDColumCache cache;
	
	private int[] allDimOfPanel = new int[] { -3,-2,-1,0, 1, 2,3, 4,5, 6, 7, 8, 9, 10, 11, 12 ,13 ,14 ,15, 16 ,17, 18 ,19 ,20,21,22,23};
	
	private ArrayList<UpdateBdd> UpdateBDD = new ArrayList<UpdateBdd>();
	
	public void setActualTableOpen(String table) throws Exception {
		
	
		allCol.getContainer().clearChildComponents();
		allNameCol.getContainer().clearChildComponents();
		this.remove(allNameCol);
		if(!main.OptimiseRender)
		{
				ActualTable = table;
		
				ArrayList<String> All = main.bdd.getAllColumn(table);
				int h = main.bdd.NumberOfLigne(table) * HeightTablePanel;
				int w = widthTablePanel * All.size();
				cache = new BDDColumCache(w, h, All);
			
			
		
			
			allCol.getContainer().setSize( cache .Width, 615);
			
			
			allNameCol.getContainer().setSize(cache .Width, HeightTablePanel);
			if(cache .Width > 1115)
			{
				allNameCol.setSize(1115, HeightTablePanel);
				allCol.setSize(1115, 615);
			}else {
				allNameCol.setSize(cache .Width, HeightTablePanel);
				allCol.setSize(cache .Width, 615);
			}
			
			if(cache .Width > 1115)
			{
				
				
				allNameCol.setHorizontalScrollBarVisible(true);
				allNameCol.getHorizontalScrollBar().setSize(1115, 10);
				allNameCol.getHorizontalScrollBar().setPosition(0, HeightTablePanel);
				
				allNameCol.getHorizontalScrollBar().getListenerMap().addListener(ScrollBarChangeValueEvent.class, (ScrollBarChangeValueEventListener) (ScrollBarChangeValueEvent event) -> {
					
					allCol.getHorizontalScrollBar().setCurValue(event.getNewValue());
			    });
				
				allCol.setHorizontalScrollBarVisible(true);
				allCol.getHorizontalScrollBar().setSize(1115, 10);
				allCol.getHorizontalScrollBar().setPosition(0, 605);
				allCol.getHorizontalScrollBar().getListenerMap().addListener(ScrollBarChangeValueEvent.class, (ScrollBarChangeValueEventListener) (ScrollBarChangeValueEvent event) -> {
					
					allNameCol.getHorizontalScrollBar().setCurValue(event.getNewValue());
			    });
			}
			
			
			int i = 0;
			for(String s:cache.Colum) {
				Label col = new Label(s,5+ i * widthTablePanel, 0,widthTablePanel,HeightTablePanel );
				col.getStyle().setTextColor(0, 0, 0, 1);
				col.getStyle().getBackground().setColor(1, 1, 1, 1);
				putStyleText(col, 20);
				allNameCol.add(col);
				i++;
			}
			
			this.add(allNameCol);
			
	
			if(cache.Height > 615)
			{
				allCol.setVerticalScrollBarVisible(true);
				allCol.getVerticalScrollBar().setSize(10, 615);
				if(cache .Width > 1115)
				{
					allCol.getVerticalScrollBar().setPosition(1105,0);
				}
				else {
					allCol.getVerticalScrollBar().setPosition(cache .Width-10,0);
				}
				
				allCol.getContainer().setSize(cache.Width, cache.Height);
				allCol.getVerticalScrollBar().setMaxValue(cache.Height);
				if(CurValue.containsKey(table))
				{
					allCol.getVerticalScrollBar().setCurValue(CurValue.get(table));
				}
				else {
					allCol.getVerticalScrollBar().setCurValue(0);
					CurValue.put(table, 0F);
				}
		
				
				allCol.getVerticalScrollBar().getListenerMap().addListener(ScrollBarChangeValueEvent.class, (ScrollBarChangeValueEventListener) (ScrollBarChangeValueEvent event) -> {
					
					main.panelscrollingstat = 1;
				
			    });
			}
			
			int id = main.bdd.GetFreeId();
			ResultSet result = main.bdd.query("SELECT * FROM `"+table+"` WHERE "+condition, id);
			if(result != null) 
			{
				try {
					int o = 0;
					
					while( result.next())
					{
						i = 0;
						
						
					
						for(String s:cache.Colum) {
							
							CustomTextInput col = new CustomTextInput(result.getObject(s)+"", i * widthTablePanel, 0 + o * HeightTablePanel,widthTablePanel,HeightTablePanel ,result.getInt("id"));
							col.getStyle().setTextColor(0, 0, 0, 1);
							col.getStyle().getBackground().setColor(1,1,1,1);
							putStyleText(col, 20);
							allCol.add(col);
							col.getListenerMap().addListener(TextInputContentChangeEvent.class, (TextInputContentChangeEventListener) (TextInputContentChangeEvent event) -> {
								
								try {
									UpdateBDD.add(new UpdateBdd(s,col.getTextState().getText(),col));
									if(!back.contains(execute))
									{
										back.add(execute);
									}
								} catch (Exception e) {
									CRASH.Push(e);
								}
								col.getStyle().getBackground().setColor(ColorConstants.lightRed());
					
						    });
							i++;
						}
						
						o++;
					}
				} catch(Exception e) {
					CRASH.Push(e);
				}
			}
			main.bdd.CloseFreeId(id);
			this.add(allCol);
			
		} else 
		{
		
			if(main.UseCache)
			{
				ActualTable = table;
				cache = InsideTablePanel.get(table);
				boolean flag = false;
				
				if(cache == null)
				{
					ArrayList<String> All = main.bdd.getAllColumn(table);
					int h = main.bdd.NumberOfLigne(table) * HeightTablePanel;
					int w = widthTablePanel * All.size();
					cache = new BDDColumCache(w, h, All);
					flag = true;
				}
				
			
				allCol.getContainer().setSize( cache .Width, 615);
			
				
				allNameCol.getContainer().setSize(cache .Width, HeightTablePanel);
				if(cache .Width > 1115)
				{
					allNameCol.setSize(1115, HeightTablePanel);
					allCol.setSize(1115, 615);
				}else {
					allNameCol.setSize(cache .Width, HeightTablePanel);
					allCol.setSize(cache .Width, 615);
				}
				
				if(cache .Width > 1115)
				{
					
					
					allNameCol.setHorizontalScrollBarVisible(true);
					allNameCol.getHorizontalScrollBar().setSize(1115, 10);
					allNameCol.getHorizontalScrollBar().setPosition(0, HeightTablePanel);
					
					allNameCol.getHorizontalScrollBar().getListenerMap().addListener(ScrollBarChangeValueEvent.class, (ScrollBarChangeValueEventListener) (ScrollBarChangeValueEvent event) -> {
						
						allCol.getHorizontalScrollBar().setCurValue(event.getNewValue());
				    });
					
					allCol.setHorizontalScrollBarVisible(true);
					allCol.getHorizontalScrollBar().setSize(1115, 10);
					allCol.getHorizontalScrollBar().setPosition(0, 605);
					allCol.getHorizontalScrollBar().getListenerMap().addListener(ScrollBarChangeValueEvent.class, (ScrollBarChangeValueEventListener) (ScrollBarChangeValueEvent event) -> {
						
						allNameCol.getHorizontalScrollBar().setCurValue(event.getNewValue());
				    });
				}
				
				
				int i = 0;
				
				for(String s:cache.Colum) {
					Label col = new Label(s,5+ i * widthTablePanel, 0,widthTablePanel,HeightTablePanel );
					col.getStyle().setTextColor(0, 0, 0, 1);
					col.getStyle().getBackground().setColor(1,1,1,1);
					putStyleText(col, 20);
					allNameCol.add(col);
					i++;
					
				}
				
				this.add(allNameCol);
				
		
				if(cache.Height > 615)
				{
					allCol.setVerticalScrollBarVisible(true);
					allCol.getVerticalScrollBar().setSize(10, 615);
					if(cache .Width > 1115)
					{
						allCol.getVerticalScrollBar().setPosition(1105,0);
					}
					else {
						allCol.getVerticalScrollBar().setPosition(cache .Width-10,0);
					}
					allCol.getContainer().setSize(cache.Width, cache.Height);
					allCol.getVerticalScrollBar().setMaxValue((float) (cache.Height / 1.1));
					if(CurValue.containsKey(table))
					{
						allCol.getVerticalScrollBar().setCurValue(CurValue.get(table));
					}
					else {
						allCol.getVerticalScrollBar().setCurValue(0);
						CurValue.put(table, 0F);
					}
					allCol.getVerticalScrollBar().getListenerMap().addListener(ScrollBarChangeValueEvent.class, (ScrollBarChangeValueEventListener) (ScrollBarChangeValueEvent event) -> {
						
						main.panelscrollingstat = 1;
					
				    });
				}
				
				int id = main.bdd.GetFreeId();
				ResultSet result = main.bdd.query("SELECT * FROM `"+table+"` WHERE "+condition, id);
				if(result != null) 
				{
					try {
						int o = 0;
						
						while(result.next())
						{
							i = 0;
							for(String s:cache.Colum) {
								
								CustomTextInput  col = new CustomTextInput (result.getObject(s)+"",5+ i * widthTablePanel, 0 + o * HeightTablePanel ,widthTablePanel,HeightTablePanel,result.getInt("id") );
								col.getStyle().setTextColor(0, 0, 0, 1);
								col.getStyle().getBackground().setColor(1,1,1,1);
								putStyleText(col, 20);
								//allCol.getContainer().add(col);
								col.getListenerMap().addListener(TextInputContentChangeEvent.class, (TextInputContentChangeEventListener) (TextInputContentChangeEvent event) -> {
									
									try {
										UpdateBDD.add(new UpdateBdd(s,col.getTextState().getText(),col));
										if(!back.contains(execute))
										{
											back.add(execute);
										}
									} catch (Exception e) {
										CRASH.Push(e);
									}
									col.getStyle().getBackground().setColor(ColorConstants.lightRed());
							    });
								cache.comp.add(col);
								if(col.getPosition().y   / HeightTablePanel >= allDimOfPanel[0] && allDimOfPanel[allDimOfPanel.length - 1] >= col.getPosition().y  / HeightTablePanel)
								{
									allCol.add(col);
								}
								i++;
							}
							o++;
						}
					} catch(Exception e) {
						CRASH.Push(e);
					}
				}
				
				main.bdd.CloseFreeId(id);
				
				/*int val = 0;
				int o = 0;
				for(Component c:cache.comp)
				{
		
					if(o >= allDimOfPanel[0] && allDimOfPanel[allDimOfPanel.length - 1] >= o)
					{
					
						
						allCol.getContainer().add(c);
						
					}
					o++;
				}  */
				
				this.add(allCol);
				if(flag) {
					InsideTablePanel.put(table, cache);
				}
			} else {
				ActualTable = table;
				cache = InsideTablePanel.get(table);
			
				
			
					ArrayList<String> All = main.bdd.getAllColumn(table);
					int h = main.bdd.NumberOfLigne(table) * HeightTablePanel;
					int w = widthTablePanel * All.size();
					cache = new BDDColumCache(w, h, All);
				
				
				
			
					allCol.getContainer().setSize( cache .Width, 615);
				
					
					allNameCol.getContainer().setSize(cache .Width, HeightTablePanel);
					if(cache .Width > 1115)
					{
						allNameCol.setSize(1115, HeightTablePanel);
						allCol.setSize(1115, 615);
					}else {
						allNameCol.setSize(cache .Width, HeightTablePanel);
						allCol.setSize(cache .Width, 615);
					}
				
				if(cache .Width > 1115)
				{
					
					
					allNameCol.setHorizontalScrollBarVisible(true);
					allNameCol.getHorizontalScrollBar().setSize(1115, 10);
					allNameCol.getHorizontalScrollBar().setPosition(0, HeightTablePanel);
					
					allNameCol.getHorizontalScrollBar().getListenerMap().addListener(ScrollBarChangeValueEvent.class, (ScrollBarChangeValueEventListener) (ScrollBarChangeValueEvent event) -> {
						
						allCol.getHorizontalScrollBar().setCurValue(event.getNewValue());
				    });
					
					allCol.setHorizontalScrollBarVisible(true);
					allCol.getHorizontalScrollBar().setSize(1115, 10);
					allCol.getHorizontalScrollBar().setPosition(0, 605);
					allCol.getHorizontalScrollBar().getListenerMap().addListener(ScrollBarChangeValueEvent.class, (ScrollBarChangeValueEventListener) (ScrollBarChangeValueEvent event) -> {
						
						allNameCol.getHorizontalScrollBar().setCurValue(event.getNewValue());
				    });
				}
				
				
				int i = 0;
				for(String s:cache.Colum) {
					Label col = new Label(s,5+ i * widthTablePanel, 0,widthTablePanel,HeightTablePanel );
					col.getStyle().setTextColor(0, 0, 0, 1);
					col.getStyle().getBackground().setColor(1,1,1,1);
					putStyleText(col, 20);
					allNameCol.add(col);
					i++;
				}
				
				this.add(allNameCol);
				
		
				if(cache.Height > 615)
				{
					allCol.setVerticalScrollBarVisible(true);
					allCol.getVerticalScrollBar().setSize(10, 615);
					if(cache .Width > 1115)
					{
						allCol.getVerticalScrollBar().setPosition(1105,0);
					}
					else {
						allCol.getVerticalScrollBar().setPosition(cache .Width-10,0);
					}
					allCol.getContainer().setSize(cache.Width, cache.Height);
					allCol.getVerticalScrollBar().setMaxValue((float) (cache.Height / 1.1));
					if(CurValue.containsKey(table))
					{
						allCol.getVerticalScrollBar().setCurValue(CurValue.get(table));
					}
					else {
						allCol.getVerticalScrollBar().setCurValue(0);
						CurValue.put(table, 0F);
					}
					allCol.getVerticalScrollBar().getListenerMap().addListener(ScrollBarChangeValueEvent.class, (ScrollBarChangeValueEventListener) (ScrollBarChangeValueEvent event) -> {
						
						main.panelscrollingstat = 1;
					
				    });
				}
				
				int id = main.bdd.GetFreeId();
				ResultSet result = main.bdd.query("SELECT * FROM `"+table+"` WHERE "+condition, id);
				if(result != null) 
				{
					try {
						int o = 0;
						int val = 0;
						boolean flag = true;
						while(flag &&result.next())
						{
							if(o >= allDimOfPanel[0] && allDimOfPanel[allDimOfPanel.length - 1] >= o)
							{
										i = 0;
										for(String s:cache.Colum) {
											CustomTextInput  col = new CustomTextInput (result.getObject(s)+"",5+ i * widthTablePanel, 0 + o * HeightTablePanel,widthTablePanel,HeightTablePanel, result.getInt("id") );
											col.getStyle().setTextColor(0, 0, 0, 1);
											col.getStyle().getBackground().setColor(1,1,1,1);
											putStyleText(col, 20);
											col.getListenerMap().addListener(TextInputContentChangeEvent.class, (TextInputContentChangeEventListener) (TextInputContentChangeEvent event) -> {
												
												try {
													UpdateBDD.add(new UpdateBdd(s,col.getTextState().getText(),col));
													if(!back.contains(execute))
													{
														back.add(execute);
													}
												} catch (Exception e) {
													CRASH.Push(e);
												}
												col.getStyle().getBackground().setColor(ColorConstants.lightRed());
										    });
											allCol.add(col);
											i++;
										}
							}else if( o > allDimOfPanel[allDimOfPanel.length - 1]) {
								flag = false;
							}
							
	
							o++;
						}
					} catch(Exception e) {
						CRASH.Push(e);
					}
				}
				
				main.bdd.CloseFreeId(id);
				
				
				
				this.add(allCol);
				
			}
		}
	}
	
	private void putStyleText(Component c, float font) {
		c.getStyle().setHorizontalAlign(HorizontalAlign.CENTER);
        c.getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
        c.getStyle().setFontSize(font);
	}
	
	private void putStyleTextAlignLeft(Component c, float font) {
		c.getStyle().setHorizontalAlign(HorizontalAlign.LEFT);
        c.getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
        c.getStyle().setFontSize(font);
	}


	public void eventAllCol() {

		if(ActualTable == null) {
			return;
		}
		if(allCol.getVerticalScrollBar().isVisible())
		{
			CurValue.put(ActualTable,allCol.getVerticalScrollBar().getCurValue());
		}
		if(main.OptimiseRender)
		{
		
			if(main.UseCache)
			{
		 		allCol.getContainer().clearChildComponents();
				int val = (int) allCol.getVerticalScrollBar().getCurValue() / HeightTablePanel;
			
	
				for(Component c:cache.comp)
				{
		
					if(c.getPosition().y   / HeightTablePanel >= val +allDimOfPanel[0] && val + allDimOfPanel[allDimOfPanel.length - 1] >= c.getPosition().y  / HeightTablePanel)
					{
						allCol.add(c);
					}
	
				} 
			} else {
				allCol.getContainer().clearChildComponents();
				int id = main.bdd.GetFreeId();
				ResultSet result = main.bdd.query("SELECT * FROM `"+ActualTable+"` WHERE "+condition, id);
				if(result != null) 
				{
					try {
						int o = 0;
						int val = (int) allCol.getVerticalScrollBar().getCurValue() / HeightTablePanel;
						boolean flag = true;
						while(flag && result.next())
						{
							if(o >= val+allDimOfPanel[0] && val+allDimOfPanel[allDimOfPanel.length - 1] >= o)
							{
								
								
										int i = 0;
										for(String s:cache.Colum) {
											CustomTextInput  col = new CustomTextInput (result.getObject(s)+"",5+ i * widthTablePanel, 0 + o * HeightTablePanel,widthTablePanel,HeightTablePanel , result.getInt("id"));
											putStyleText(col, 20);
												col.getListenerMap().addListener(TextInputContentChangeEvent.class, (TextInputContentChangeEventListener) (TextInputContentChangeEvent event) -> {
												
												try {
													UpdateBDD.add(new UpdateBdd(s,col.getTextState().getText(),col));
													if(!back.contains(execute))
													{
														back.add(execute);
													}
												} catch (Exception e) {
													CRASH.Push(e);
												}
												col.getStyle().getBackground().setColor(ColorConstants.lightRed());
										    });
											allCol.add(col);
											i++;
										}
							
							}else if( o > val+allDimOfPanel[allDimOfPanel.length - 1]) {
								flag = false;
							}
	
							o++;
						}
					} catch(Exception e) {
						CRASH.Push(e);
					}
				}
				
				main.bdd.CloseFreeId(id);
			}
		}
		
	}
	
	 private Animation PregetSlideImageOnClick(ToggleButton switchMDP2, Icon bgSwitchMDP, boolean first) {

		 return getSlideImageOnClick(switchMDP2, bgSwitchMDP, first);
	 }
	 
	    private Animation getColorAnimation(ToggleButton toggleButton, Vector4f targetColor) {
	        return new Animation() {
	            private Vector4f baseColor;
	            private Vector4f endColor;
	            private Vector4f colorVector;
	            private double time;
	            private double spentTime;
	            
	         
	            @Override
	            protected void beforeAnimation() {
	                time = 1.5d;
	                spentTime = 0;
	                baseColor = new Vector4f(toggleButton.getStyle().getBackground().getColor());
	                endColor = targetColor;
	                colorVector = new Vector4f(endColor).sub(baseColor);
	            }

	            @Override
	            protected boolean animate(double delta) {
	                spentTime += delta;
	                float percentage = (float) (spentTime / time);

	                Vector4f newColor = new Vector4f(baseColor).add(new Vector4f(colorVector).mul(percentage));
	                toggleButton.getStyle().getBackground().setColor(newColor);
	                return spentTime >= time;
	            }

	            @Override
	            protected void afterAnimation() {
	                toggleButton.getStyle().getBackground().setColor(endColor);
	            }
	        };
	    }
	    
	    private Animation getSlideImageOnClick(ToggleButton toggleButton, Icon bgImageNormal, boolean first) {
	        return new Animation() {
	            private float initialPosition;
	            private double time;
	            private double spentTime;
	            private float endPosition;

	            @Override
	            protected void beforeAnimation() {
	                time = 0.5d;
	                spentTime = 0;
	                initialPosition = bgImageNormal.getPosition().x;

	                if (!toggleButton.isToggled()) {
	                    endPosition = 1;
	                } else {
	                    endPosition = toggleButton.getSize().x - bgImageNormal.getSize().x-1;
	                }
	            }

	            @Override
	            protected boolean animate(double delta) {
	                spentTime += delta;
	                float percentage = (float) (spentTime / time);

	                bgImageNormal.getPosition().x = initialPosition + (endPosition - initialPosition) * percentage;
	                return spentTime >= time;
	            }

	            @Override
	            protected void afterAnimation() {
	            	if(first)
	            	{
	            		bgImageNormal.getPosition().x = endPosition;
	            	}
	            }
	        };
	    }
	    
	    private LoadableImage tg = ImageLoader.loadImage("assets/textures/SwitchBall.png");
	    
	    private ToggleButton addToggleOption(String info, boolean toggle, int x, int y, int w, int h, int ytool)
		{
			ToggleButton Switch = new ToggleButton("",x, y,w, h);
	        Switch.setToggled(toggle);
	        Switch.setTooltip(new Tooltip(info));
	        Switch.getTooltip().setPosition(0, ytool);
	        Switch.getTooltip().getStyle().setFontSize(16F);
	        Switch.getTooltip().getStyle().getBackground().setColor(ColorConstants.gray());
	        Switch.getStyle().getBackground().setColor(0, 0, 0, 0);
	        Switch.getTooltip().setSize(w*4, 50);
	        Switch.setToggledBackgroundColor(ColorConstants.lightBlue());
	        Icon bgSwitchMDP = new ImageIcon(tg);
	      //  Switch.getStyle().setBorderRadius(12);
	        bgSwitchMDP.setSize(new Vector2f(20, 30));
	        bgSwitchMDP.setPosition(new Vector2f(0, 0));
	        Switch.getStyle().getBackground().setColor(ColorConstants.red());
	        Switch.getStyle().getBackground().setIcon(bgSwitchMDP);
	   
	
	        Switch.getListenerMap().addListener(MouseClickEvent.class,(MouseClickEventListener) event -> PregetSlideImageOnClick(Switch, bgSwitchMDP, false).startAnimation());

	       
	     

	        
	       
	        return(Switch);
		}
	    
	    public class UpdateBdd {
	    	private String val;
	    	private String key;
	    	public CustomTextInput co;
	    	public UpdateBdd(String colum, String value , CustomTextInput c) {
	    		val= value;
	    		key = colum;

	    		co = c;
	    	}
	    	
	    	public String getUpdateString() {

		    		try {
		    			int v = Integer.parseInt(val);
		    			return key+"="+v;
		    		} catch(Exception e)
		    		{
		    			return key+"='"+val+"'";
		    		}
		    	
	    	}
	    }
}

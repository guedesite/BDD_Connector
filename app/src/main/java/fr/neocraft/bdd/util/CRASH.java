package fr.neocraft.bdd.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.joml.Vector2f;

import com.spinyowl.legui.component.Button;
import com.spinyowl.legui.component.Label;
import com.spinyowl.legui.component.TextArea;
import com.spinyowl.legui.component.optional.align.HorizontalAlign;
import com.spinyowl.legui.component.optional.align.VerticalAlign;
import com.spinyowl.legui.event.MouseClickEvent;
import com.spinyowl.legui.icon.ImageIcon;
import com.spinyowl.legui.image.loader.ImageLoader;
import com.spinyowl.legui.listener.MouseClickEventListener;
import com.spinyowl.legui.style.color.ColorConstants;

import fr.neocraft.bdd.main;
import fr.neocraft.bdd.component.ComponentCursorCustom;
import fr.neocraft.bdd.component.ImageAlpha; 



public class CRASH {

	

	private static Label ErrorTitle;
	private static TextArea ErrorText;
	private static Label ErrorFile;
	private static Button Close, Folder;
	private static ImageAlpha Background;
	
	
	
	public static void Init() {
		
		

		Background = new ImageAlpha(ImageLoader.loadImage("assets/textures/ErrorPanel.png"));
		Background.setPosition(440, 160);
		Background.setSize(400, 400);

		
		
		Folder = new Button(360, 4, 16, 16);
		Folder.getTextState().setText("");
		Folder.getStyle().setBorder(null);

		ImageIcon imgf = new ImageIcon(ImageLoader.loadImage("assets/textures/Folder.png"));
		imgf.setSize(new Vector2f(16,16));
		Folder.getStyle().getBackground().setIcon(imgf);
		Folder.setSize(new Vector2f(16,16));
		Folder.getStyle().getBackground().setColor(ColorConstants.transparent());

		Folder.getHoveredStyle().getBackground().setColor(ColorConstants.transparent());

		ImageIcon imgfHover = new ImageIcon(ImageLoader.loadImage("assets/textures/FolderHover.png"));
		imgfHover.setSize(new Vector2f(16,16));
		Folder.getHoveredStyle().getBackground().setIcon(imgfHover);
		Folder.setSize(new Vector2f(16,16));
        ComponentCursorCustom HoverFolderBTN = new ComponentCursorCustom(Folder, main.cursorHover);
        
        Folder.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
        	try {
				Desktop.getDesktop().open(new File("assets/crash/"));
			} catch (IOException e) {
				e.printStackTrace();
			}
        	 
         });
        
        
        

        main.ComponentCursorHover.add(HoverFolderBTN);
        
        Background.add(Folder);
		
		Close = new Button(380, 4, 16, 16);
		Close.getTextState().setText("");
		Close.getStyle().setBorder(null);

		Close.getStyle().getBackground().setIcon(new ImageIcon(ImageLoader.loadImage("assets/textures/Close.png")));
		Close.setSize(new Vector2f(16,16));
        Close.getStyle().getBackground().setColor(ColorConstants.transparent());

        Close.getHoveredStyle().getBackground().setColor(ColorConstants.transparent());
        Close.getHoveredStyle().getBackground().setIcon(new ImageIcon(ImageLoader.loadImage("assets/textures/CloseHover.png")));
        Close.setSize(new Vector2f(16,16));
        ComponentCursorCustom HoverCloseBTN = new ComponentCursorCustom(Close, main.cursorHover);
        
        Close.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
        
        	 main.panel.remove(Background);
        
         });
        
        main.ComponentCursorHover.add(HoverCloseBTN);
        
        Background.add(Close);
		
		ErrorTitle = new Label("Erreur fatal !", 5,2, 155, 32);
		ErrorTitle.getStyle().setTextColor(ColorConstants.white());
		ErrorTitle.getStyle().setFontSize(28F);
		ErrorTitle.getStyle().setHorizontalAlign(HorizontalAlign.LEFT);
		Background.add(ErrorTitle);
		
		ErrorText = new TextArea(5, 42, 390, 330);
		ErrorText.getTextState().setText("null");
		
		ErrorText.setHorizontalScrollBarVisible(true);
		ErrorText.setHorizontalScrollBarHeight(290);
		
		ErrorText.setVerticalScrollBarVisible(true);
		ErrorText.setVerticalScrollBarWidth(150);
		
		ErrorText.getStyle().setHorizontalAlign(HorizontalAlign.LEFT);
		ErrorText.getStyle().setVerticalAlign(VerticalAlign.TOP);
		
		ErrorText.getStyle().getBackground().setColor(ColorConstants.transparent());
		ErrorText.getFocusedStyle().getBackground().setColor(ColorConstants.transparent());
		ErrorText.getHoveredStyle().getBackground().setColor(ColorConstants.transparent());
		ErrorText.getPressedStyle().getBackground().setColor(ColorConstants.transparent());
		ErrorText.setEditable(false);;
		ErrorText.getTextAreaField().getStyle().getBackground().setColor(ColorConstants.transparent());
		ErrorText.getTextAreaField().getFocusedStyle().getBackground().setColor(ColorConstants.transparent());
		ErrorText.getTextAreaField().getHoveredStyle().getBackground().setColor(ColorConstants.transparent());
		ErrorText.getTextAreaField().getPressedStyle().getBackground().setColor(ColorConstants.transparent());
		ErrorText.getTextAreaField().setEditable(false);
		
		ErrorFile = new Label("Fichier: Crash-0.txt", 5, 375, 1, 20);
		ErrorFile.getStyle().setTextColor(ColorConstants.white());
		ErrorFile.getStyle().setFontSize(16F);
		ErrorFile.getStyle().setHorizontalAlign(HorizontalAlign.LEFT);
		
		main.ComponentCursorHover.add(new ComponentCursorCustom(ErrorFile, main.cursorHover));
		
		ErrorFile.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) (MouseClickEvent event) -> {
				try {
					Desktop.getDesktop().open(new File("assets/crash/"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			
        });
		
		Background.add(ErrorText);

		Background.add(ErrorFile);
		
	}
	
	
	
	
	
	public static void Push(Exception e)
	{
		
		e.printStackTrace();
		int i = 0; 
		File f = GetFileError("Crash-"+LocalDate.now().toString()+"-"+i+".txt");
		while(f.exists())
		{
			i++;
			f = GetFileError("Crash-"+LocalDate.now().toString()+"-"+i+".txt");
		
		}
		try {
		
			System.err.println("Error saved in: "+f.getAbsolutePath());
		
			setFileError().mkdir();

			
			PrintWriter writer = new PrintWriter(f, "UTF-8");
			
			writer.println("########################	error	########################");
			writer.println(" ");
			writer.println("Time: "+LocalDate.now().toString().replace("-", "/")+" "+new SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
			writer.println(" ");
			
			e.printStackTrace(writer);
			
			writer.println(" ");
			writer.println("########################	error	########################");
			writer.println(" ");
			writer.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		
		
		ErrorTitle.getTextState().setText("Erreur fatal !");
		
		String fi = e.getCause()+": "+e.getLocalizedMessage()+"\r\n\r\n";
		for(int o = e.getStackTrace().length -1; o >= 0; o--)
		{
			fi += e.getStackTrace()[o].toString()+"\r\n";
		}
		fi += "\r\n"+e.getMessage();
	
	 ErrorText.getTextState().setText(fi);
		
		ErrorFile.getTextState().setText("Fichier: Crash-"+LocalDate.now().toString()+"-"+i+".txt");
		main.panel.add(Background);
	}
	
	
	 
	  private static File GetFileError(String f) {
		   
		    return new File("assets/crash/"+f);
		  }
	  
	  private static File setFileError() {
		  
		    return new File("assets/crash");
		  }
	
}

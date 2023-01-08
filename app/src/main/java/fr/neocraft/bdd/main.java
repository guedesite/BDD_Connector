package fr.neocraft.bdd;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetMonitors;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import com.spinyowl.legui.DefaultInitializer;
import com.spinyowl.legui.animation.Animator;
import com.spinyowl.legui.animation.AnimatorProvider;
import com.spinyowl.legui.component.Frame;
import com.spinyowl.legui.component.Label;
import com.spinyowl.legui.component.optional.align.HorizontalAlign;
import com.spinyowl.legui.component.optional.align.VerticalAlign;
import com.spinyowl.legui.event.WindowSizeEvent;
import com.spinyowl.legui.image.loader.ImageLoader;
import com.spinyowl.legui.listener.WindowSizeEventListener;
import com.spinyowl.legui.style.color.ColorConstants;
import com.spinyowl.legui.style.color.ColorUtil;
import com.spinyowl.legui.system.context.Context;
import com.spinyowl.legui.system.renderer.nvg.NvgRenderer;
import com.spinyowl.legui.theme.Themes;
import com.spinyowl.legui.theme.colored.FlatColoredTheme;

import fr.neocraft.bdd.panel.BddPanel;
import fr.neocraft.bdd.panel.LauncherPanel;
import fr.neocraft.bdd.util.BDD;
import fr.neocraft.bdd.util.BDDConnection;
import fr.neocraft.bdd.util.CRASH;
import fr.neocraft.bdd.util.NBT;


public class main {
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int FPS = 60;
	public static volatile boolean running = false;
	private static long[] monitors = null;
	public static LauncherPanel panel;
	public static BddPanel BDDpanel;
	public static boolean IsBddPanel = false;
	private static Context context;

	public static int FrameX = 0;
	public static int FrameY = 0;
	
	public static boolean OptimiseRender = NBT.getBool("optimize");
	public static boolean UseCache = NBT.getBool("usecache");
	
	public static String BDDCoUrlFormat = "jdbc:mysql://{ip}/{table}";

	public static Label fpsPour = new Label("0%");
	
	public static long cursorHover;
	public static long cursorWrite;
	public static long cursorStandar;

	public static ArrayList<BDDConnection> allConnection;
	public static BDD bdd;
	
	public static ArrayList<fr.neocraft.bdd.component.ComponentCursorCustom> ComponentCursorHover = new ArrayList<fr.neocraft.bdd.component.ComponentCursorCustom>();;
	public static String path;
	
	public static Frame frame;
	
	
	public static int panelscrollingstat = 0;
	
	  public static void main(String[] args) {
		  
		  
	 		System.setProperty("org.lwjgl.util.DebugLoader", "true");
	 		System.setProperty("org.lwjgl.util.Debug", "true");
	 		System.setProperty("java.library.path", GetNative() );
	 		System.setProperty("org.lwjgl.librarypath", GetNative() );
	 		
	    System.setProperty("joml.nounsafe", Boolean.TRUE.toString());
	    System.setProperty("java.awt.headless", Boolean.FALSE.toString());
	    

	    
	    if (!glfwInit()) {
	        throw new RuntimeException("Can't initialize GLFW");
	    }


	    long window = glfwCreateWindow(WIDTH, HEIGHT, "NeoCraft BDD CONNECTOR", NULL, NULL);
	    
	    GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, 1);
		GLFW.glfwWindowHint(GLFW. GLFW_RESIZABLE, 0);
	    
	    glfwShowWindow(window);
	    
	    glfwMakeContextCurrent(window);
	    GLCapabilities glCapabilities = GL.createCapabilities();
	    GLFW.glfwSwapInterval(0);
	    
	    glfwMakeContextCurrent(window);
	    GL.setCapabilities(glCapabilities);
	    GLFW.glfwSwapInterval(0);
	    
	    cursorHover = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
		cursorWrite = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR);
		cursorStandar = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
	    
	    PointerBuffer pointerBuffer = glfwGetMonitors();
	    int remaining = pointerBuffer.remaining();
	    monitors = new long[remaining];
	    for (int i = 0; i < remaining; i++) {
	        monitors[i] = pointerBuffer.get(i);
	    }

	   frame = new Frame(WIDTH, HEIGHT);

	    CRASH.Init();
	    
	    initConnection();
	    createGuiElements();
	    
		fpsPour.setSize(16,16);
		fpsPour.setPosition(WIDTH - 23, HEIGHT - 18);
		
		
		fpsPour.getStyle().setHorizontalAlign(HorizontalAlign.CENTER);
		fpsPour.getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
		fpsPour.getStyle().setFontSize(15F);
		
		frame.getContainer().add(fpsPour);
	    
	    Themes.setDefaultTheme(new FlatColoredTheme(
	    		ColorUtil.rgba(245, 245, 245, 1), // backgroundColor
	    		ColorUtil.rgba(176, 190, 197, 1), // borderColor
	    		ColorUtil.rgba(176, 190, 197, 1), // sliderColor
	    		ColorUtil.rgba(100, 181, 246, 1), // strokeColor
	    		ColorUtil.rgba(165, 214, 167, 1), // allowColor
	    		ColorUtil.rgba(239, 154, 154, 1), // denyColor
	        ColorConstants.transparent() // shadowColor
	    ));

	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    FrameX = (int)(screenSize.getWidth()-WIDTH)/2;
	    FrameY = (int)(screenSize.getHeight()-HEIGHT)/2;
	    GLFW.glfwSetWindowPos(window, FrameX, FrameY);

	
	    DefaultInitializer initializer = new DefaultInitializer(window, frame);
	    GLFWKeyCallbackI glfwKeyCallbackI = (w1, key, code, action, mods) -> running = !(key == GLFW_KEY_ESCAPE && action != GLFW_RELEASE);
	    GLFWWindowCloseCallbackI glfwWindowCloseCallbackI = w -> running = false;
	    
	    
	    initializer.getCallbackKeeper().getChainKeyCallback().add(glfwKeyCallbackI);
	    initializer.getCallbackKeeper().getChainWindowCloseCallback().add(glfwWindowCloseCallbackI);
	   

	    
	    
	    fr.neocraft.bdd.util.image_parser resource_01 = fr.neocraft.bdd.util.image_parser.load_image("assets/textures/icone.png");
	    GLFWImage image = GLFWImage.malloc(); GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
        image.set(resource_01.get_width(), resource_01.get_heigh(), resource_01.get_image());
        imagebf.put(0, image);
        GLFW.glfwSetWindowIcon(window, imagebf);
	    
	    running = true;
	    
	    NvgRenderer renderer = (NvgRenderer)initializer.getRenderer();
	    Animator animator = AnimatorProvider.getAnimator();
	    renderer.initialize();
	    context = initializer.getContext();
//	    context.setDebugEnabled(true);

	    
	    
	    
	    bdd = new BDD();

	    
	    glViewport(0, 0, WIDTH, HEIGHT);
	    double lasttime = GLFW.glfwGetTime();
	    
	    int nbFrames = 0;

	    double lastTime =  GLFW.glfwGetTime();
	    
	    while (running) {

	    		if(IsBddPanel) {
	    		
	    				if(panelscrollingstat == 30)
	    				{
	    					panelscrollingstat = 0;
	    					BDDpanel.eventAllCol();
	    				} else if(panelscrollingstat < 30 && panelscrollingstat > 0){
	    					panelscrollingstat++;
	    				}
	    		}
	
	   
	        context.updateGlfwWindow();

	   
	   
	        glClearColor(1, 1, 1, 1);
	        glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
	        
	        GLFW.glfwSetCursor(window, cursorStandar);
		       for(int i = 0; i < ComponentCursorHover.size(); i++)
		       {
		    	   if(ComponentCursorHover.get(i).c.isHovered())
		    	   {
		    		   GLFW.glfwSetCursor(window, ComponentCursorHover.get(i).cursorId);
		    	   }
		       }
	        
		      
		       
	        renderer.render(frame, context);

	        
	        glfwPollEvents();
	        glfwSwapBuffers(window);

	        animator.runAnimations();

	        initializer.getSystemEventProcessor().processEvents(frame, context);
	        initializer.getGuiEventProcessor().processEvents();


	        
	        try {
		        while (GLFW.glfwGetTime() < lasttime + 1.0/FPS) {
		         
					Thread.sleep(1);
		
		        }
	        } catch (InterruptedException e) {
	        	CRASH.Push(e);
			}
	        lasttime += 1.0/FPS;

	        nbFrames++;
	        if ( GLFW.glfwGetTime() -  lastTime >= 0.1 ){ 
	        	 fpsPour.getTextState().setText(nbFrames*10+"FPS" );
	            nbFrames = 0;
	            lastTime += 0.1;
	        }

	        
	    }

	    renderer.destroy();
	    glfwDestroyWindow(window);
	    glfwTerminate();
	    
	    System.exit(-1);
	}

	 	
	 	
	private static void createGuiElements() {
	    panel = new LauncherPanel(FrameX, FrameY, WIDTH, HEIGHT);
	    BDDpanel = new BddPanel(main.WIDTH, main.HEIGHT);
	    
	    panel.setFocusable(true);
	 /*   gui.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
	    	CursorPoint = event.getPosition();
			System.out.println(Example.CursorPoint.toString()); 
	    });
	    gui.getListenerMap().addListener(MouseDragEvent.class, ( MouseDragEventListener) event -> {
	    	System.out.println(Example.FrameX);
			FrameX += Math.round(Mouse.getCursorPosition().x - CursorPoint.x);
			FrameY += Math.round(Mouse.getCursorPosition().y - CursorPoint.y);
			System.out.println(Example.FrameX);
	   
	    }); */
	    panel.getListenerMap().addListener(WindowSizeEvent.class, (WindowSizeEventListener) event -> panel.setSize(event.getWidth(), event.getHeight()));
	    frame.getContainer().add(panel);
	    frame.getContainer().setFocusable(true);

	}
	
	public static boolean IsFocusOnPanel(int PosX, int PosY, int Width, int Height) {
		Point e = MouseInfo.getPointerInfo().getLocation();
		return e.x > PosX && e.x < PosX + Width && e.y > PosY && e.y < PosY + Height;
		
	}

	private static String GetNative() {
	  
	    return String.valueOf("assets/natives/");
	  }
	
	public static void initConnection() {
		allConnection = new ArrayList<BDDConnection>();
	
		File[] allf = new File("assets/co").listFiles();
	    
	    ArrayList<String> tempfile = new ArrayList<String>();
	    for(File f:allf)
	    {
	    	if(f.isFile())
	    	{
	    		if(f.getName().contains(".dat"))
	    		{
	    			tempfile.add(f.getName());
	    		}
	    	}
	    }
	    Collections.sort(tempfile);
	    for(String f:tempfile)
	    {
	    	allConnection.add(NBT.LoadNBTConnexion(f));
	    }

	    
	 
	}

}

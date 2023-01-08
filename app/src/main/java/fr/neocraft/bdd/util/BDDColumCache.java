package fr.neocraft.bdd.util;

import java.util.ArrayList;

import com.spinyowl.legui.component.Component;


public class BDDColumCache {

	public ArrayList<Component> comp;
	public int Width, Height;
	public ArrayList<String> Colum;
	
	public BDDColumCache(ArrayList<Component> c, int width, int height, ArrayList<String> colum)
	{
		comp = c;
		Width = width;
		Height = height;
		Colum = colum;
	}
	
	public BDDColumCache(int width, int height, ArrayList<String> colum)
	{
		Colum = colum;
		Width = width;
		Height = height;
		comp = new ArrayList<Component>();
	}
}

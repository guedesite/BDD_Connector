package fr.neocraft.bdd.util;

import java.io.File;
import java.io.IOException;

import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;


public class NBT {
	private static final String filePath = "assets/co/";
	private static final String optionPath = "assets/";
	private static final String oldfilePath = "assets/co/old/";
	
	

	public static BDDConnection LoadNBTConnexion(String file) {
		File f = new File(filePath+file.replace(".dat", "")+".dat");
		if(f.exists())
		{
			try {
				CompoundTag tag = (CompoundTag) NBTUtil.read(f).getTag();
				return new BDDConnection(tag.getString("URL"), tag.getString("User"), tag.getString("Table"), tag.getString("MDP"),tag.getString("FName"));
			} catch (IOException e) {
				return null;
			}
		} else {
			return null;
		}
		
	
	}
	
	public static void saveNBTConnexion(BDDConnection co)
	{
		
		CompoundTag tag = new CompoundTag();
		tag.putString("URL", co.getURL());
		tag.putString("Table", co.getTable());
		tag.putString("User", co.getUser());
		tag.putString("MDP", co.getMDP());
		tag.putString("FName", co.FictifName);
		try {
			NBTUtil.write(tag, filePath+co.FictifName+".dat");
		} catch (IOException e) {
		CRASH.Push(e);
		}
	}
	
	public static boolean getBool(String key) {
		File f = new File(optionPath+"option.dat");
		if(f.exists())
		{
			try {
				return ((CompoundTag) NBTUtil.read(f).getTag()).getBoolean(key);
			} catch (Exception e) {
				CRASH.Push(e);
				return false;
			}
		} else {
			defDefault();
			return getBool(key);
		}
	}
	
	public static void setBool(String key, boolean value) {
		File f = new File(optionPath+"option.dat");
		if(f.exists())
		{
			try {
				CompoundTag tag = (CompoundTag) NBTUtil.read(f).getTag();
				tag.putBoolean(key, value);
				NBTUtil.write(tag, optionPath+"option.dat");
			} catch (Exception e) {
				CRASH.Push(e);
			
			}
		} else {
			defDefault();
		}
	}
	
	private static void defDefault() {
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("optimize", true);
		tag.putBoolean("usecache", false);
		try {
			NBTUtil.write(tag, optionPath+"option.dat");
		} catch (IOException e) {
			CRASH.Push(e);
		}
	}
}

package fr.neocraft.bdd.util;

import java.io.Serializable;

public class BDDConnection implements Serializable{

	
	private static final long serialVersionUID = 16414343346343L;
	private String URL,  MDP, User, Table;
	public String FictifName = "default";

	
	public BDDConnection(String url,  String user, String table, String mdp, String FName) {
		URL=url;
		User=user;
		MDP=mdp;
		FictifName = FName;
		User=user;
		Table = table;
	
	}
	
	public String getMDP() {
		return MDP;
	}
	
	public String getURL() {
		return URL;
	}
	
	public String getUser() {
		return User;
	}


	public String getTable() {
		return Table;
	}


	

	
}

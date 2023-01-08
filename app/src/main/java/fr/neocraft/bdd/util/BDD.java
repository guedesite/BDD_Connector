package fr.neocraft.bdd.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fr.neocraft.bdd.main;

public class BDD {
	
	public boolean IsDebug = true;
	public boolean IsOpen = false;
	public boolean IsClass = false;
	private BDDConnection DataConnexion;
	public Connection connexion;
	private  Map<Integer, Statement> s = new HashMap<Integer, Statement>();
	private int Id = 0;
	
	
	
	public BDD()
	{
		IsOpen = false;
		IsClass = false;
		try {
			Class.forName( "com.mysql.cj.jdbc.Driver" );
			System.out.println("Openbdd CLASS LOAD");
		}
		catch ( ClassNotFoundException e ) {
			this.IsClass = false;
			erreur("bdd", e);
	   }
	}
	
	public BDD(boolean Null)
	{ }

	public void Openbdd(BDDConnection data) throws Exception
	{
		if(DataConnexion == null || !data.equals(DataConnexion))
		{
			if(IsOpen)
			{
				this.Closebdd();
			}
			DataConnexion = data;
		
			System.out.println("TRY BDD:");
			

		    this.connexion = DriverManager.getConnection(main.BDDCoUrlFormat.replace(
					"{ip}", DataConnexion.getURL()).replace("{table}", DataConnexion.getUser()), DataConnexion.getUser(),DataConnexion.getMDP() );
		    this.IsOpen = true;
		    System.out.println("Openbdd BDD OPEN");
		}
		
	}
	
	public void Closebdd()
	{
		try {
			Iterator it = this.s.values().iterator();
			int i = 0;
			while(it.hasNext())
			{
				((Statement)it.next()).close();
			}
			this.s = new HashMap<Integer, Statement>();
			DataConnexion = null;
			this.connexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 this.erreur("Closebdd", "success");
		 this.IsOpen = false;
	}

	
	public long getId() {
		return this.Id;
	}
	
	public Map getStatement()
	{
		return this.s;
	}
	
	public int getNbStatementActive()
	{
		
		if(this.s.isEmpty()) {
			return 0;
		} else {
			Iterator it = this.s.values().iterator();
			int i = 0;
			while(it.hasNext())
			{
				i++;
				it.next();
			}
			return i;
		}
	}
	
	
	public int GetFreeId() {
		if(this.Id >= Integer.MAX_VALUE-10)
		{
			s.clear();
			this.Id = 0;
		}
		this.Id++;
		if(this.IsDebug)
		{
			System.out.println("[BDD DEBUG] Create id: "+this.Id);
		}
		try {
			this.s.put(this.Id, this.connexion.createStatement());
			return this.Id;
		} catch (SQLException e) {
			this.erreur("GetFreeId("+this.Id+")", e);
			CloseFreeId(this.Id);
			return -1;
		}catch(NullPointerException e)
		{
			this.erreur("GetFreeId("+this.Id+")", e);
			return -1;
		}
	}
	
	public void CloseFreeId(int i) {
		Statement o = this.s.get(i);
		if(this.IsDebug)
		{
			System.out.println("[BDD DEBUG] delete id: "+i);
		}
		if(o != null)
		{
			try {
				o.close();
				if(this.s.remove(i, o)) {
				}
			} catch(SQLException e)
			{
				this.erreur("CloseFreeId("+i+")", e);
			}
			catch(NullPointerException e)
			{
				this.erreur("CloseFreeId("+i+")", e);
			}
		}
		else
		{
			this.erreur("CloseFreeId("+i+")", "NULL");
		}
	}
	
	public boolean Exist(String query)
	{
		int id = GetFreeId();
		if(this.s.containsKey(id))
		{
			ResultSet r = query(query, id);
			if(this.IsDebug)
			{
				System.out.println("[BDD DEBUG] EXIST: "+query);
			}
			if(r != null)
			{
				try {
					while(r.next())
					{
						CloseFreeId(id);
						return true;
					}
				} catch(Exception e)
				{
					this.erreur("Exist", e, query);
				}
			}else {
				this.erreur("Exist", "NULL", query);
			}
		}
		else
		{
			this.erreur("query", "key "+id+" null", query);
		}
		CloseFreeId(id);
		return false;
	}
	
	public ResultSet query(String query, int id)
	{
		ResultSet r = null;
		if(this.s.containsKey(id))
		{
			if(this.IsDebug)
			{
				System.out.println("[BDD DEBUG] QUERY: "+query);
			}
			try {
				r = this.s.get(id).executeQuery(query);
			} catch (SQLException e) {
				this.erreur("query", e, query);
			}catch(NullPointerException e)
			{
				this.erreur("query", e, query);
			}
		}
		else
		{
			this.erreur("query", "key "+id+" null", query);
		}
		return r;
	}

	
	public boolean update(String query)
	{
		int id = GetFreeId();
		if(this.s.containsKey(id))
		{
			if(this.IsDebug)
			{
				System.out.println("[BDD DEBUG] UPDATE: "+query);
			}
			try {
				this.s.get(id).executeUpdate(query);
				CloseFreeId(id);
				return true;
			} catch (SQLException e) {
				this.erreur("update", e, query);
			}
		}
		else
		{
			this.erreur("update", "key "+id+" null", query);
		}
		CloseFreeId(id);
		return false;
	}

	
	public boolean checkIfIsValidQuery(String query) {
		ResultSet r = null;
		int id=GetFreeId();
		if(this.s.containsKey(id))
		{
			if(this.IsDebug)
			{
				System.out.println("[BDD DEBUG] checkIfIsValidQuery: "+query);
			}
			try {
				r = this.s.get(id).executeQuery(query);
				while(r.next())
				{
					return true;
				}
				return true;
			} catch (SQLException e) {
				return false;
			}catch(NullPointerException e)
			{
				return false;
			}
		}
		else
		{
			this.erreur("query", "key "+id+" null", query);
		}
		return false;
	}
	
	public boolean execute(String query)
	{
		int id = GetFreeId();
		if(this.s.containsKey(id))
		{
			if(this.IsDebug)
			{
				System.out.println("[BDD DEBUG] EXECUTE: "+query);
			}
			try {
				this.s.get(id).execute(query);
				CloseFreeId(id);
				return true;
			} catch (SQLException e) {
				this.erreur("query", e, query);
		
			}
		}
		else
		{
			this.erreur("execute", "key "+id+" null", query);

		}
		CloseFreeId(id);
		return false;
	}

	public void erreur(String f, Exception e, String stat)
	{
		System.err.println("ERREUR LORS DE L'EXECUTION '"+f +"'");
		System.err.println(" INFO: ");
		e.printStackTrace();
		System.err.println(" DATA: " + stat);
		CRASH.Push(e);
	}

	public void erreur(String f, Exception e) {
		System.err.println("ERREUR LORS DE L'EXECUTION '"+f +"'");
		System.err.println(" INFO: ");
		e.printStackTrace();
		CRASH.Push(e);
	}
	public void erreur(String f, String e) {
		System.err.println("ERREUR LORS DE L'EXECUTION '"+f +"'");
		System.err.println(" INFO: "+e);
	}

	public void erreur(String f, String e, String stat)
	{
		System.err.println("ERREUR LORS DE L'EXECUTION '"+f +"'");
		System.err.println(" INFO: "+e);
		System.err.println(" DATA: " + stat);
	}

	public ArrayList<String> getAllTable() throws Exception {
		ArrayList<String> ar = new ArrayList<String>();
		DatabaseMetaData md = connexion.getMetaData();
		String[] types = {"TABLE"};
		ResultSet rs = md.getTables(null, null, "%", types);
		while (rs.next()) {
			ar.add(rs.getString(3));
		}
		return ar;
	}
	
	public ArrayList<String> getAllColumn(String table) throws Exception {
		ArrayList<String> ar = new ArrayList<String>();
		
		String query = "SELECT * FROM `"+table+"`";
		int id = GetFreeId();
		ResultSet r = query(query, id);
		if(r != null)
		{
			ResultSetMetaData rsMetaData;
			rsMetaData = r.getMetaData();
			int numberOfColumns = rsMetaData.getColumnCount();
			for (int i = 1; i < numberOfColumns + 1; i++) {
				String columnName = rsMetaData.getColumnName(i);
				ar.add(columnName);
			}
		}
		CloseFreeId(id);
		return ar;
	}

	public int NumberOfLigne(String Table, String condition)
	{
		int id = GetFreeId();
		ResultSet r = query("SELECT count(*) AS nbLignes FROM `"+Table+"` WHERE "+condition, id);
		int id2 = 0;
		if(r != null)
		{
			try {
				while(r.next())
				{
					id2 =  r.getInt("nbLignes");
				}
			} catch(Exception e)
			{
				this.erreur("compterligne", e,"SELECT count(*) AS nbLignes FROM `"+Table+"` WHERE "+condition);
			}
		}
		else
		{
			this.erreur("compterligne", "NULL","SELECT count(*) AS nbLignes FROM `"+Table+"` WHERE "+condition);
		}
		CloseFreeId(id);
		return id2;
	}
	public int NumberOfLigne(String Table)
	{
		return NumberOfLigne(Table, "1");
	}
	
}

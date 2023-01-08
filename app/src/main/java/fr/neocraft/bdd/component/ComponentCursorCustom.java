package fr.neocraft.bdd.component;

import com.spinyowl.legui.component.Component;

public class ComponentCursorCustom {
	public Component c;
	public long cursorId;
	
	public ComponentCursorCustom(Component comp, long cursor)
	{
		c = comp;
		this.cursorId = cursor;
	}
}
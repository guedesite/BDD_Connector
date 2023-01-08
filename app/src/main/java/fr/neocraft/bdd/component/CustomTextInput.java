package fr.neocraft.bdd.component;

import com.spinyowl.legui.component.TextInput;

public class CustomTextInput extends TextInput{
	

	private static final long serialVersionUID = 7433653713474179178L;
	
	public int id;
	public CustomTextInput(String text, float x, float y, float width, float height, int idd) {
		
        super(text, x, y, width, height);
        id = idd;
    }
}

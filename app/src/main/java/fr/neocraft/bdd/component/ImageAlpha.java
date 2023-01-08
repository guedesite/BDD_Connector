package fr.neocraft.bdd.component;

import com.spinyowl.legui.component.ImageView;
import com.spinyowl.legui.image.Image;
import com.spinyowl.legui.style.color.ColorConstants;

public class ImageAlpha extends ImageView{


	private static final long serialVersionUID = -604478627272626150L;

	public ImageAlpha(Image image) {
        super(image);
        this.getStyle().getBackground().setColor(ColorConstants.transparent());
        this.getStyle().setBorder(null);

    }
}

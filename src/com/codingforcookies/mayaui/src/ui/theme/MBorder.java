package com.codingforcookies.mayaui.src.ui.theme;

import com.codingforcookies.mayaui.src.MayaUI;
import com.codingforcookies.mayaui.src.ui.RenderHelper;

public class MBorder implements Cloneable {
	public MBorderOptions top, right, bottom, left;
	
	public MBorder() { }

	public MBorder(String type, String parse) {
		parse(type, parse);
	}
	
	public void parse(String type, String parse) {
		if(type != "") {
			String[] types = type.substring(type.indexOf("-") + 1).split("-");
			for(String str : types) {
				if(str.equals("top"))
					top = createBorder("top", parse);
				else if(str.equals("right"))
					right = createBorder("right", parse);
				else if(str.equals("bottom"))
					bottom = createBorder("bottom", parse);
				else if(str.equals("left"))
					left = createBorder("left", parse);
				else
					System.err.println("Unknown border type " + str);
			}
		}else
			top = right = bottom = left = createBorder("all", type);
	}
	
	private MBorderOptions createBorder(String side, String type) {
		String[] options = type.split(" ");
		MBorderType borderType = MBorderType.valueOf(options[0].toUpperCase());
		float size = ((Integer)MayaUI.parseConfigValue(options[1])).floatValue();
		MayaColor color = (MayaColor)MayaUI.parseConfigValue(options[2]);
		
		return new MBorderOptions(side, borderType, size, color);
	}

	public void render(float width, float height) {
		boolean istop = top != null;
		boolean isbottom = bottom != null;
		if(istop)
			RenderHelper.renderBox(0, 0, width, top.size, top.color);
		if(isbottom)
			RenderHelper.renderBox(0, bottom.size - height, width, bottom.size, bottom.color);
		if(right != null)
			RenderHelper.renderBox(width - right.size, (istop ? -top.size : 0), right.size, height - (istop ? top.size : 0) - (isbottom ? bottom.size : 0), right.color);
		if(left != null)
			RenderHelper.renderBox(0, (istop ? -top.size : 0), left.size, height - (istop ? top.size : 0) - (isbottom ? bottom.size : 0), left.color);
				/*
			case OUTER:
				if(top)
					RenderHelper.renderBox((left ? -size : 0), size, width + (left ? size : 0) + (right ? size : 0), size);
				if(right)
					RenderHelper.renderBox(width, 0, size, height);
				if(bottom)
					RenderHelper.renderBox((left ? -size : 0), -height, width + (left ? size : 0) + (right ? size : 0), size);
				if(left)
					RenderHelper.renderBox(-size, 0, size, height);
				break;
		}*/
	}
	
	public String toString() {
		String ret = "";
		if(top != null)
			ret += top.toString();
		if(right != null)
			ret += right.toString();
		if(bottom != null)
			ret += bottom.toString();
		if(left != null)
			ret += left.toString();
		return ret;
	}
	
	public MBorder clone() {
		try {
			return (MBorder)super.clone();
		} catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
class MBorderOptions {
	public String side = "";
	public MBorderType borderType;
	public float size;
	public MayaColor color;
	
	public MBorderOptions(String side, MBorderType borderType, float size, MayaColor color) {
		this.side = side;
		this.borderType = borderType;
		this.size = size;
		this.color = color;
	}
	
	public String toString() {
		return "border-" + side + "(" + borderType + "; " + size + "; " + color.toString() + ")";
	}
}
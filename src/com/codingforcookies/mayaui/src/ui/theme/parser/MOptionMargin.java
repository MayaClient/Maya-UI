package com.codingforcookies.mayaui.src.ui.theme.parser;

import org.lwjgl.opengl.GL11;

import com.codingforcookies.mayaui.src.MayaUI;
import com.codingforcookies.mayaui.src.ui.theme.UITheme;

public class MOptionMargin extends MOptionParser {
	public float top = 0F, right = 0F, bottom = 0F, left = 0F;
	
	public boolean shouldParse(String keyclass, String key, String value) {
		return key.startsWith("margin");
	}
	
	public MOptionRuntime[] getRuntime() {
		return new MOptionRuntime[] { MOptionRuntime.PRERENDER };
	}
	
	public MOptionMargin getDefault() {
		top = 0F;
		left = 0F;
		return this;
	}
	
	public <T> T getValue(T type) { return null; }
	
	public MOptionParser parse(UITheme theme, String keyclass, String key, String value) {
		MOptionMargin premargin = (MOptionMargin)theme.getClass(keyclass).get("margin");
		if(premargin == null)
			premargin = this;
		
		if(!key.equals("margin")) {
			String[] types = key.substring(key.indexOf("-") + 1).split("-");
			for(String str : types) {
				if(str.equals("top"))
					premargin.top = MayaUI.parseConfigFloat(value);
				else if(str.equals("right"))
					premargin.right = MayaUI.parseConfigFloat(value);
				else if(str.equals("bottom"))
					premargin.bottom = MayaUI.parseConfigFloat(value);
				else if(str.equals("left"))
					premargin.left = MayaUI.parseConfigFloat(value);
				else
					System.err.println("Unknown side " + str);
			}
		}else
			premargin.top = premargin.left = MayaUI.parseConfigFloat(value);
		
		if(premargin == this)
			theme.getClass(keyclass).set("margin", premargin);
		return null;
	}
	
	public void run(MOptionRuntime runtime, float width, float height) {
		switch(runtime) {
			case PRERENDER:
				GL11.glTranslatef(left, -top, 0F);
				break;
			default:
				break;
		}
	}
}
package com.codingforcookies.gridui.src;

import javax.swing.UIManager;

import com.codingforcookies.gridui.src.ui.theme.ThemeManager;

public class GRiDUI {
	public static final String version = "Chill Leopard";
	
	public static void initialize() {
		ThemeManager.loadThemes();
		
		if(!ThemeManager.setTheme("GRiD")) {
			System.err.println("Failed to load default theme");
			return;
		}
		
		System.out.println("Loaded default theme");
		
		
	}
	
	public static UIManager createUIManager() {
		UIManager uimanager = new UIManager();
		return uimanager;
	}
}
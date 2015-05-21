package com.codingforcookies.mayaui.src.ui.theme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codingforcookies.mayaui.src.exceptions.ThemeInvalidException;
import com.codingforcookies.mayaui.src.texture.MayaFontRenderer;
import com.codingforcookies.mayaui.src.texture.MayaTextureLoader;

public class ThemeManager {
	private static HashMap<String, File> availableThemes;
	private static UITheme currentTheme;
	
	public static Set<String> getThemes() { return availableThemes.keySet(); }
	public static UITheme getTheme() { return currentTheme; }
	
	public static boolean setTheme(String name) {
		if(availableThemes.containsKey(name)) {
			currentTheme = processTheme(availableThemes.get(name));
			if(currentTheme != null) {
				System.out.println("Applied theme '" + name + "'");
				MayaColor.GLOBAL_TEXT = currentTheme.getClass("global").get("color", new MayaColor(), MayaColor.WHITE);
				MayaColor.GLOBAL_BACKGROUND = currentTheme.getClass("global").get("background-color", new MayaColor(), MayaColor.BLUE);
				
				//currentTheme.output();
				
				return true;
			}
		}
		
		System.err.println("Failed to apply theme '" + name + "'");
		return false;
	}

	private static final File themeLocation = new File("E:/Git/Maya Client/Maya-UI/themes");
	private static final String themePattern = "([^{]+)\\s*\\{\\s*([^}]+)\\s*}";

	public static void loadThemes() {
		availableThemes = new HashMap<String, File>();
		
		for(File folder : themeLocation.listFiles()) {
			if(folder.isDirectory()) {
				for(File file : folder.listFiles()) {
					String themename = isTheme(file);
					if(themename != null)
						availableThemes.put(themename, file);
				}
			}
		}
	}

	private static String isTheme(File file) {
		if(!file.getName().endsWith(".mayatheme"))
			return null;
		
		return processThemeForName(file);
	}
	
	private static String processThemeForName(File file) {
		return (String)process(file, new String[] { "info", "name" });
	}
	
	private static UITheme processTheme(File file) {
		return (UITheme)process(file, null);
	}
	
	private static Object process(File file, String[] returnkey) {
		try {
			String content = getFileContents(file);
			if(content == null)
				return null;
			
			UITheme theme = null;
			if(returnkey == null)
				theme = new UITheme();
			
			HashMap<String, String> themevars = new HashMap<String, String>();
			
			Matcher m = Pattern.compile(themePattern).matcher(content);
			while(m.find()) {
				String key = m.group(1).trim();
				
				if(returnkey != null)
					if(!key.equals(returnkey[0]))
						continue;
				
				String[] split = m.group(2).split("\n");
				
				for(String line : split) {
					if(line.startsWith("#"))
						continue;
					if(key.equals("vars")) {
						String[] set = line.split(":");
						set[0] = set[0].trim();
						set[1] = set[1].trim();
						
						themevars.put(set[0], set[1]);
					}else{
						String[] set = line.split(":");
						set[0] = set[0].trim();
						set[1] = set[1].trim();
						
						while(set[1].contains("[") && set[1].contains("]")) {
							String varkey = set[1].substring(set[1].indexOf("[") + 1, set[1].indexOf("]"));
							if(themevars.containsKey(varkey))
								set[1] = set[1].replace("[" + varkey + "]", themevars.get(varkey));
							else
								throw new ThemeInvalidException("Variable " + varkey + " does not exist");
						}
						
						if(returnkey != null) {
							if(returnkey[1].equals(set[0]))
								return set[1];
						}else
							theme.set(key, set[0], set[1]);
					}
				}
			}
			
			if(returnkey != null)
				return null;
			
			File fontFile = new File(file.getParentFile(), "font.png");
			System.out.println("  Loading font...");
			MayaTextureLoader.loadFile("font", fontFile);
			MayaFontRenderer.font = MayaTextureLoader.getTexture("font");
			
			if(theme.name == null)
				throw new ThemeInvalidException("Invalid Theme: " + file.getName());
			else
				System.out.println("Loaded theme '" + theme.name + "' from file '" + file.getName() + "'");
			
			return theme;
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static String getFileContents(File file) {
		try {
			InputStream in = new FileInputStream(file);
			InputStreamReader is = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(is);

			String content = "";

			String read;
			while((read = br.readLine()) != null) {
				if(read.contains(":"))
					content += "\n";
				content += read.trim();
			}
			
			br.close();
			is.close();
			in.close();
			
			return content;
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
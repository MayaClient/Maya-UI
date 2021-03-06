package com.codingforcookies.mayaui.src.ui.theme;

import java.util.HashMap;

import com.codingforcookies.mayaui.src.ui.theme.parser.MOptionParser;

/**
 * Themes are reminiscent of CSS in how they are loaded. This class holds all name, creator, description, and class information.
 * @author Stumblinbear
 */
public class UITheme {
	public String name = "";
	public String creator = "";
	public String description = "";
	
	/**
	 * Hashmap of next-level classes.
	 */
	public HashMap<String, UIClass> classes;
	
	public UITheme() {
		classes = new HashMap<String, UIClass>();
		
		classes.put("global", new UIClass(null, "global"));
	}
	
	/**
	 * Clear all classes.
	 */
	public void unload() {
		classes.clear();
	}

	/**
	 * If a next-level class exists.
	 */
	public boolean hasClass(String key) {
		return classes.containsKey(key);
	}
	
	/**
	 * Returns a class if it exists, otherwise returns "global".
	 */
	public UIClass getClass(String key) {
		if(key.contains(" ")) {
			String[] keys = key.split(" ");
			UIClass lowest = getLowestClass(key.substring(0, key.lastIndexOf(" ")));
			if(!lowest.hasClass(keys[keys.length - 1]))
				return getUIClass(lowest.classes, lowest, keys[keys.length - 1]);
			else
				return lowest.getClass(keys[keys.length - 1]);
		}else if(classes.containsKey(key))
			return classes.get(key);
		return getUIClass(classes, getClass("global"), key);
	}
	
	/**
	 * Set a value under a class.
	 */
	public void set(String key, String classname, MOptionParser parser) {
		if(!classes.get("global").has(classname))
			if(parser != null)
				classes.get("global").set(classname, parser.clone().getDefault());
		
		getLowestClass(key).set(classname, parser);
	}
	
	/**
	 * Get the lowest class in the class string.
	 */
	private UIClass getLowestClass(String key) {
		String[] keys = key.split(" ");
		UIClass uiclass = getUIClass(classes, getClass("global"), keys[0]);
		
		for(int i = 1; i < keys.length; i++)
			uiclass = getUIClass(uiclass.classes, uiclass, keys[i]);
		
		return uiclass;
	}
	
	/**
	 * Get the class, and create if it doesn't exist.
	 */
	private UIClass getUIClass(HashMap<String, UIClass> classlist, UIClass parent, String key) {
		if(!classlist.containsKey(key))
			classlist.put(key, new UIClass(parent, key));
		return classlist.get(key);
	}
	
	/**
	 * Output theme information.
	 */
	public void output() {
		for(String key : classes.keySet())
			outputLower(classes.get(key), 1);
	}
	
	private void outputLower(UIClass uiclass, int level) {
		String spacing = "";
		for(int i = 0; i < level; i++)
			spacing += "  ";
		
		System.out.println(spacing.substring(2) + buildParentList(uiclass, uiclass.name) + ":");
		for(String key : uiclass.values.keySet())
			System.out.println(spacing + key + ": " + uiclass.values.get(key).toString());
		
		for(String key : uiclass.classes.keySet()) {
			outputLower(uiclass.classes.get(key), level + 1);
		}
	}
	
	/**
	 * Returns the parent class branch list from a child class.<br /><br />
	 * Example:<br />
	 * &nbsp;&nbsp;&nbsp;&nbsp;buildParentList(uiclasspanel, ".title");<br />
	 * Returns:<br />
	 * &nbsp;&nbsp;&nbsp;&nbsp;global panel .title
	 */
	public String buildParentList(UIClass uiclass, String parents) {
		if(uiclass.parent != null) {
			parents = uiclass.parent.name + " " + parents;
			return buildParentList(uiclass.parent, parents);
		}
		return parents;
	}
}
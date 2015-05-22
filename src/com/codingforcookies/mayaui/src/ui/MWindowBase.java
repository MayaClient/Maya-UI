package com.codingforcookies.mayaui.src.ui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.codingforcookies.mayaui.src.MayaUI;
import com.codingforcookies.mayaui.src.ui.theme.components.UIComponent;

/**
 * Base class for Maya UI windows and panels.
 * @author Stumblinbear
 */
public class MWindowBase extends MayaUpdate implements MayaRender {
	/**
	 * The parent UI Manager
	 */
	protected UIManager uimanager;
	
	public float x, y, width, height;
	
	/**
	 * A list of all the components in the window.
	 */
	private List<UIComponent> components;
	
	public MWindowBase(UIManager uimanager, float x, float y, float width, float height) {
		this.x = x;
		this.y = MayaUI.SCREEN_HEIGHT - y;
		this.width = width;
		this.height = height;
		
		components = new ArrayList<UIComponent>();
		
		init();
	}
	
	/**
	 * The initialization function. Add components here.
	 */
	public void init() {
		
	}
	
	/**
	 * Updates all components in the window.
	 */
	public void update() {
		for(UIComponent component : components)
			component.update();
	}

	/**
	 * Renders all components in the window.
	 */
	public void render() {
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(x, y, 0F);
			drawComponents();
		}
		GL11.glPopMatrix();
	}
	
	/**
	 * Add a component to the window.
	 */
	public void addComponent(UIComponent component) {
		components.add(component);
	}

	/**
	 * For render functions that override the super render function. Just draws the components.
	 */
	public void drawComponents() {
		for(UIComponent component : components)
			component.render();
	}
}
package com.codingforcookies.mayaui.src.notification;

import org.lwjgl.opengl.Display;

import com.codingforcookies.mayaui.src.MayaUI;
import com.codingforcookies.mayaui.src.ui.MWindowBase;

public class NotificationManager {
	private static MWindowBase notificationWindow;
	
	public static void addNotification(MNotification mNotification) {
		if(notificationWindow == null) {
			notificationWindow = new MWindowBase(0, 0, Display.getWidth(), Display.getHeight()) {
				public void update() {
					super.update();
					
					if(components.size() == 0) {
						scheduledForDrop = true;
						notificationWindow = null;
					}
				}
			};
			
			MayaUI.getUIManager().createWindow(notificationWindow);
		}
		
		mNotification.number = notificationWindow.getComponents().size();
		notificationWindow.addComponent(mNotification);
	}
}
package com.pixelmkmenu.pixelmkmenu.interfaces;

public interface IMouseEventListener {
	void mousePressed(IMouseEventProvider MouseEventProvider, int mouseX, int mouseY, int button);

	void mouseMoved(IMouseEventProvider MouseEventProvider, int mouseX, int mouseY);

	void mouseReleased(IMouseEventProvider guiPixelMKIngameMenu, int mouseX, int mouseY, int button);
}

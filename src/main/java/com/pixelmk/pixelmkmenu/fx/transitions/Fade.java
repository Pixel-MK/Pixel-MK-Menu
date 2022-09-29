package com.pixelmk.pixelmkmenu.fx.transitions;

import com.pixelmk.pixelmkmenu.fx.ScreenTransition;
import com.pixelmk.pixelmkmenu.helpers.FBO;

public class Fade extends ScreenTransition{
    public void render(FBO active, FBO last, int width, int height, float transitionPct) {
		if(last != null) drawFBO(last, 0, 0, width, height, 0, 1.0f - transitionPct);
		if (active != null) drawFBO(active, 0, 0, width, height, 0, transitionPct);
	}

	public String getName() {
		return "fadebasic";
	}

	public boolean isHighMotion() {
		return false;
	}

	public int getTransitionTime() {
		return 250;
	}

	public ScreenTransition.TransitionType getTransitionType(){
		return ScreenTransition.TransitionType.Sine;
	}
}

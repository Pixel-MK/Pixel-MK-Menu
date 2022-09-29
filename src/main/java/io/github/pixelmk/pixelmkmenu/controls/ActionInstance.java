package io.github.pixelmk.pixelmkmenu.controls;

import javax.annotation.Nullable;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;

/**
 * <p>
 * Replaces the <code>OnPress</code> with non-functional code to help with
 * people not used to functional interfaces or functional programming.
 * </p>
 *
 * Can be used like the following:
 * <pre><code>
 * new GuiButtonMainMenu("examplemod.exampleModsMenuButton", new ActionInstance(ButtonAction.OPEN_GUI, ScreenType.MODS));
 * </code></pre>
 */
public class ActionInstance implements OnPress {

	public Button source;
	protected ButtonAction action;
	protected Object data;

	public ActionInstance(ButtonAction action, Object data) {
		this.action = action;
		this.data = data;
	}

	@Override
	public void onPress(Button button) {
		this.action.onPress(this);
	}

	@Nullable
	public Object getData() {
		return this.data;
	}

}

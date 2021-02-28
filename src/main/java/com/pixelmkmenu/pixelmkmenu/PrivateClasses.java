package com.pixelmkmenu.pixelmkmenu;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class PrivateClasses<C> {

	public Class<? extends C> Class = null;

	private final String className;

	private PrivateClasses(ObfuscationMapping mapping) {
		this.className = mapping.getName();
		Class<? extends C> reflectedClass = null;
		try {
			reflectedClass = (Class)Class.forName(this.className);
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		this.Class = reflectedClass;
	}

	public static final PrivateClasses<Slot> SlotCreativeInventory = new PrivateClasses(ObfuscationMapping.SlotCreativeInventory);

	public static final PrivateClasses<Container> ContainerCreative = new PrivateClasses(ObfuscationMapping.ContainerCreative);

}

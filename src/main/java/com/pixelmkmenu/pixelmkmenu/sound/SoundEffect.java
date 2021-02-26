package com.pixelmkmenu.pixelmkmenu.sound;

import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class SoundEffect extends PositionedSound{
	public SoundEffect(ResourceLocation soundLocation, float volume, float pitch) {
		super(soundLocation, SoundCategory.MUSIC);
		this.volume = volume;
		this.pitch = pitch;
		this.xPosF = 0.0f;
		this.yPosF = 0.0f;
		this.zPosF = 0.0f;
		this.repeat = false;
		this.repeatDelay = 0;
		this.attenuationType = AttenuationType.NONE;
	}
}

package com.pixelmkmenu.pixelmkmenu.util;

import java.lang.reflect.Field;

import com.pixelmkmenu.pixelmkmenu.ModUtil;

public class Reflection {
	private static Field MODIFIERS = null;
	
	static {
		try {
			MODIFIERS = Field.class.getDeclaredField("modifiers");
			MODIFIERS.setAccessible(true);
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void setPrivateValue(Class<?> instanceClass, Object instance, String fieldName, String obfuscatedFieldName,
			String seargeName, Object value) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		setPrivateValueRaw(instanceClass, instance, ModUtil.getObfuscatedFieldName(fieldName, obfuscatedFieldName, seargeName), value);
	}
	
	public static void setPrivateValue(Class<?> instanceClass, Object instance, String fieldName, 
			Object value) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		setPrivateValueRaw(instanceClass, instance, fieldName, value);
	}
	
	public static <T> T getPrivateValue(Class<?> instanceClass, Object instance, String fieldName, String obfuscatedFieldName,
			String seargeName) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		return (T)getPrivateValueRaw(instanceClass, instance, ModUtil.getObfuscatedFieldName(fieldName, obfuscatedFieldName, seargeName));
	}
	
	public static <T> T getPrivateValue(Class<?> instanceClass, Object instance, String fieldName) throws IllegalArgumentException, 
	SecurityException, NoSuchFieldException {
		return (T)getPrivateValueRaw(instanceClass, instance, fieldName);
	}
	
	private static void setPrivateValueRaw(Class<?> instanceClass, Object instance, String fieldName, 
			Object value) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		try {
			Field field = instanceClass.getDeclaredField(fieldName);
			int modifiers = MODIFIERS.getInt(field);
			if((modifiers & 0x10) != 0) MODIFIERS.setInt(field, modifiers & 0xFFFFFFEF);
			field.setAccessible(true);
			field.set(instance, value);
		} catch (IllegalAccessException ex) {}
	} 
	
	public static Object getPrivateValueRaw(Class<?> instanceClass, Object instance, String fieldName) throws IllegalArgumentException,
	SecurityException, NoSuchFieldException{
		try {
			Field field = instanceClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(instance);
		} catch (IllegalAccessException illegalaccessexception) {
			return null;
		}
	}

}

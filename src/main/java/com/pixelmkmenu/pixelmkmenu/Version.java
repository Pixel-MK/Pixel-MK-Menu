package com.pixelmkmenu.pixelmkmenu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version implements Comparable<Version>{
	private static final Pattern versionPattern = Pattern.compile("^([0-9]+)(\\.([0-9]+))?(\\.([0-9]+))?(\\.([0-9]+))?$");
	private int value = 0;
	  
	private int major = 0;
	  
	private int minor = 0;
	  
	private int revision = 0;
		
	private int dev = 0;
		
	public Version(int major, int minor, int revision, int dev) {
		setValue(major, minor, revision, dev);
	}
		
	public Version(String version) {
	    Matcher versionMatcher = versionPattern.matcher(version.trim());
	    if (versionMatcher.matches()) {
	    	int major = Integer.parseInt(versionMatcher.group(1));
	    	int minor = (versionMatcher.group(3) != null) ? Integer.parseInt(versionMatcher.group(3)) : 0;
	    	int revision = (versionMatcher.group(5) != null) ? Integer.parseInt(versionMatcher.group(5)) : 0;
	    	int dev = (versionMatcher.group(7) != null) ? Integer.parseInt(versionMatcher.group(7)) : 0;
	    	setValue(major, minor, revision, dev);
		} 
	}
		
	private void setValue(int major, int minor, int revision, int dev) {
		this.major = major;
		this.minor = minor;
		this.revision = revision;
		this.dev = dev;
	}
		
	public String toString() {
	    return String.format("%d.%d.%d.%d", new Object[] { Integer.valueOf(this.major), Integer.valueOf(this.minor), Integer.valueOf(this.revision), Integer.valueOf(this.dev) });
	}
		
	public boolean isGreaterThan(Version other) {
	    return (this.value > other.value);
	}
		
	@Override
	public int compareTo(Version other) {
		if (other == null) return 1;
		return this.value - other.value;
	}
		
	public Version() {}
}

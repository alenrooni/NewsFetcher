package com.textylon.storage;

import java.awt.Image;
import java.net.URL;
import java.util.List;

public class WebPage {
	public URL url;
	public String title;
	public List<URL> outlinks;
	public String content;
	public String text;
	public String summary;
	public Image image;
	public PageStatus status;
	public long lastFetchTime;
	public boolean isSeed;
	public String domain;
}

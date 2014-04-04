package com.textylon.util;

import java.net.IDN;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class URLUtil {
	/**
	   * Returns the lowercased hostname for the url or null if the url is not well
	   * formed.
	   * 
	   * @param url The url to check.
	   * @return String The hostname for the url.
	   */
	  public static String getHost(String url) {
	    try {
	      return new URL(url).getHost().toLowerCase();
	    }
	    catch (MalformedURLException e) {
	      return null;
	    }
	  }
	  
	  public static String getHost(URL url){
		  return url.getHost().toLowerCase();
	  }
	  
	  /**
	   * Returns the page for the url.  The page consists of the protocol, host,
	   * and path, but does not include the query string.  The host is lowercased
	   * but the path is not.
	   * 
	   * @param url The url to check.
	   * @return String The page for the url.
	   */
	  public static String getPage(String url) {
	    try {
	      // get the full url, and replace the query string with and empty string
	      url = url.toLowerCase();
	      String queryStr = new URL(url).getQuery();
	      return (queryStr != null) ? url.replace("?" + queryStr, "") : url;
	    }
	    catch (MalformedURLException e) {
	      return null;
	    }
	  }
	  
	  public static String getProtocol(String url) {
		    try {
		      return getProtocol(new URL(url));
		    } catch (Exception e) {
		      return null;
		    }
		  }
		  
		  public static String getProtocol(URL url) {
		    return url.getProtocol();
		  }

		  public static String toASCII(String url) {
		    try {
		      URL u = new URL(url);
		      URI p = new URI(u.getProtocol(),
		        u.getUserInfo(),
		        IDN.toASCII(u.getHost()),
		        u.getPort(),
		        u.getPath(),
		        u.getQuery(),
		        u.getRef());

		      return p.toString();
		    }
		    catch (Exception e) {
		      return null;
		    }
		  }

		  public static String toUNICODE(String url) {
		    try {
		      URL u = new URL(url);
		      StringBuilder sb = new StringBuilder();
		      sb.append(u.getProtocol());
		      sb.append("://");
		      if (u.getUserInfo() != null) {
		        sb.append(u.getUserInfo());
		        sb.append('@');
		      }
		      sb.append(IDN.toUnicode(u.getHost()));
		      if (u.getPort() != -1) {
		        sb.append(':');
		        sb.append(u.getPort());
		      }
		      sb.append(u.getFile()); // includes query
		      if (u.getRef() != null) {
		        sb.append('#');
		        sb.append(u.getRef());
		      }

		      return sb.toString();
		    }
		    catch (Exception e) {
		      return null;
		    }
		  }
		  
}

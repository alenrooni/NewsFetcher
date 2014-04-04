package com.textylon.fetcher;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;






import com.textylon.storage.WebPage;

/**
 * This class described the item to be fetched.
 */
public class FetchItem {
  public static final Logger LOG = LoggerFactory.getLogger(FetchItem.class);
  int outlinkDepth = 0;
  String queueID;
  String url;
  URL u;
  WebPage datum;

  public FetchItem(String url, URL u, WebPage datum, String queueID) {
    this(url, u, datum, queueID, 0);
  }

  public FetchItem(String url, URL u, WebPage datum, String queueID, int outlinkDepth) {
    this.url = url;
    this.u = u;
    this.datum = datum;
    this.queueID = queueID;
    this.outlinkDepth = outlinkDepth;
  }

  /** Create an item. Queue id will be created based on <code>host</code>
   * argument, either as a protocol + hostname pair, protocol + IP
   * address pair or protocol+domain pair.
   */
  public static FetchItem create(String url, WebPage datum,  String queueMode) {
    return create(url, datum, queueMode, 0);
  }

  public static FetchItem create(String url, WebPage datum,  String queueMode, int outlinkDepth) {
    String queueID;
    URL u = null;
    try {
      u = new URL(url.toString());
    } catch (Exception e) {
      LOG.warn("Cannot parse url: " + url, e);
      return null;
    }
    final String proto = u.getProtocol().toLowerCase();
    String key;
    
    key = u.getHost();
    if (key == null) {
       LOG.warn("Unknown host for url: " + url + ", using URL string as key");
       key=u.toExternalForm();
    }
    
    queueID = proto + "://" + key.toLowerCase();
    return new FetchItem(url, u, datum, queueID, outlinkDepth);
  }

  public WebPage getDatum() {
    return datum;
  }

  public String getQueueID() {
    return queueID;
  }

  public String getUrl() {
    return url;
  }

  public URL getURL2() {
    return u;
  }
}

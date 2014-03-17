package com.textylon.fetcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.FeedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RSSFetcher {

	/**
	 * @param args
	 * @throws FetcherException 
	 * @throws FeedException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws IllegalArgumentException 
	 */
	private ConcurrentHashMap <URL, FeedFetcherCache> urlQ;
	private ConcurrentHashMap<URL, SyndFeed> contentQ;
	private boolean started = false;
	final Logger logger = LoggerFactory.getLogger(RSSFetcher.class);
	private int numFetcherThreads = 1;
	private int numContentThreads = 1;
	private ArrayList<FetcherThread> fetcherThreads;
	private ArrayList<ContentThread> contentThreads;
	
	
	public RSSFetcher(int numFetcherThreads, int numContentThreads){
		urlQ = new ConcurrentHashMap <URL, FeedFetcherCache>();
		contentQ = new ConcurrentHashMap<URL, SyndFeed>();
		this.numFetcherThreads = numFetcherThreads;
		this.numContentThreads = numContentThreads;
		fetcherThreads = new ArrayList<FetcherThread>();
		contentThreads = new ArrayList<ContentThread>();
	}
	public void start(){
		if(started){
			logger.warn("Trying to start a fetcher which is already started. Doing nothing.");
			return;
		}else{
			
			logger.info("creating " + this.numFetcherThreads + " fetcher threads.");
			for(int i = 0; i < numFetcherThreads; i++){
				FetcherThread fetcher = new FetcherThread();
				fetcherThreads.add(fetcher);
				fetcher.start();
			}
			
			logger.info("creating " + this.numContentThreads + " content threads.");
			for (int j = 0; j < numContentThreads ; j++){
				ContentThread content = new ContentThread();
				contentThreads.add(content);
				content.start();
			}
		}
	}
	public static void main(String[] args) throws IllegalArgumentException, MalformedURLException, IOException, FeedException, FetcherException {
		// TODO Auto-generated method stub
		FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance();
		FeedFetcher feedFetcher = new HttpURLFeedFetcher(feedInfoCache);
		SyndFeed feed = feedFetcher.retrieveFeed(new URL("http://downloads.bbc.co.uk/podcasts/worldservice/jjn/rss.xml"));
		System.out.println(feed);
	}

}

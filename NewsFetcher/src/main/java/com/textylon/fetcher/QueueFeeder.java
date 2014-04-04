package com.textylon.fetcher;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.textylon.storage.WebPage;



/**
 * This class feeds the queues with input items, and re-fills them as
 * items are consumed by FetcherThread-s.
 */
public  class QueueFeeder extends Thread {
  public static final Logger LOG = LoggerFactory.getLogger(QueueFeeder.class);
  private String reader;
  private FetchItemQueues queues;
  private int size;
  private long timelimit = -1;

  public QueueFeeder(String reader,
      FetchItemQueues queues, int size) {
    this.reader = reader;
    this.queues = queues;
    this.size = size;
    this.setDaemon(true);
    this.setName("QueueFeeder");
  }

  public void setTimeLimit(long tl) {
    timelimit = tl;
  }

  public void run() {
    boolean hasMore = true;
    int cnt = 0;
    int timelimitcount = 0;
    while (hasMore) {
      if (System.currentTimeMillis() >= timelimit && timelimit != -1) {
        String url = new String();
          WebPage datum = new WebPage();
          //hasMore = reader.next(url, datum);
          timelimitcount++;
        continue;
      }
      int feed = size - queues.getTotalSize();
      if (feed <= 0) {
        // queues are full - spin-wait until they have some free space
        try {
          Thread.sleep(1000);
        } catch (Exception e) {};
        continue;
      } else {
        LOG.debug("-feeding " + feed + " input urls ...");
        while (feed > 0 && hasMore) {
          String url = new String();
		WebPage datum = new WebPage();
		//hasMore = reader.next(url, datum);
		if (hasMore) {
		  queues.addFetchItem(url, datum);
		  cnt++;
		  feed--;
		}
        }
      }
    }
    LOG.info("QueueFeeder finished: total " + cnt + " records + hit by time limit :"
        + timelimitcount);
  }
}

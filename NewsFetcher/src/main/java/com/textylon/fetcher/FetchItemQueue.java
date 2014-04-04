package com.textylon.fetcher;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.nutch.fetcher.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FetchItemQueue {
	public static final Logger LOG = LoggerFactory.getLogger(FetchItemQueue.class);
	List<FetchItem> queue = Collections.synchronizedList(new LinkedList<FetchItem>());
    Set<FetchItem>  inProgress = Collections.synchronizedSet(new HashSet<FetchItem>());
    AtomicLong nextFetchTime = new AtomicLong();
    AtomicInteger exceptionCounter = new AtomicInteger();
    long crawlDelay;
    long minCrawlDelay;
    int maxThreads;
    Configuration conf;

    public FetchItemQueue(Configuration conf, int maxThreads, long crawlDelay, long minCrawlDelay) {
      this.conf = conf;
      this.maxThreads = maxThreads;
      this.crawlDelay = crawlDelay;
      this.minCrawlDelay = minCrawlDelay;
      // ready to start
      setEndTime(System.currentTimeMillis() - crawlDelay);
    }
    public synchronized int emptyQueue() {
        int presize = queue.size();
        queue.clear();
        return presize;
      }

      public int getQueueSize() {
        return queue.size();
      }

      public int getInProgressSize() {
        return inProgress.size();
      }

      public int incrementExceptionCounter() {
        return exceptionCounter.incrementAndGet();
      }

      public void finishFetchItem(FetchItem it, boolean asap) {
        if (it != null) {
          inProgress.remove(it);
          setEndTime(System.currentTimeMillis(), asap);
        }
      }

      public void addFetchItem(FetchItem it) {
        if (it == null) return;
        queue.add(it);
      }

      public void addInProgressFetchItem(FetchItem it) {
        if (it == null) return;
        inProgress.add(it);
      }

      public FetchItem getFetchItem() {
        if (inProgress.size() >= maxThreads) return null;
        long now = System.currentTimeMillis();
        if (nextFetchTime.get() > now) return null;
        FetchItem it = null;
        if (queue.size() == 0) return null;
        try {
          it = queue.remove(0);
          inProgress.add(it);
        } catch (Exception e) {
          LOG.error("Cannot remove FetchItem from queue or cannot add it to inProgress queue", e);
        }
        return it;
      }

      public synchronized void dump() {
        LOG.info("  maxThreads    = " + maxThreads);
        LOG.info("  inProgress    = " + inProgress.size());
        LOG.info("  crawlDelay    = " + crawlDelay);
        LOG.info("  minCrawlDelay = " + minCrawlDelay);
        LOG.info("  nextFetchTime = " + nextFetchTime.get());
        LOG.info("  now           = " + System.currentTimeMillis());
        for (int i = 0; i < queue.size(); i++) {
          FetchItem it = queue.get(i);
          LOG.info("  " + i + ". " + it.url);
        }
      }

      private void setEndTime(long endTime) {
        setEndTime(endTime, false);
      }

      private void setEndTime(long endTime, boolean asap) {
        if (!asap)
          nextFetchTime.set(endTime + (maxThreads > 1 ? minCrawlDelay : crawlDelay));
        else
          nextFetchTime.set(endTime);
      }
    }



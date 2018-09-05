package com.samule.example.elastic.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ThreadConfig {

	@Value("${thread.core.pool.size}")
	private int corePoolSize;

	@Value("${thread.maximum.pool.size}")
	private int maximumPoolSize;

	@Bean
	public ThreadPoolExecutor threadPoolExecutor() {
		return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60000, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<>(maximumPoolSize), new BlockingRejectedExecutionHandler());
	}

	private class BlockingRejectedExecutionHandler implements RejectedExecutionHandler {
		private Logger logger = LoggerFactory.getLogger(getClass());

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			try {
				if (executor.isShutdown())
					throw new RejectedExecutionException();
				else
					executor.getQueue().put(r);
			} catch (InterruptedException e) {
				logger.warn("working thread was interrupted");
				Thread.currentThread().interrupt();
			}
		}
	}

}

package com.yago.texscanner.model;

import com.yago.texscanner.Utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RenderHandler {

	private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
	private final ThreadPoolExecutor executor = new ThreadPoolExecutor(Utils.NUMBER_OF_CORES, Utils.NUMBER_OF_CORES, 1, TimeUnit.SECONDS, queue);

	public ThreadPoolExecutor getExecutor() {
		return executor;
	}

	public void cancelAll() {
		Runnable[] tasks = new Runnable[queue.size()];
		queue.toArray(tasks);

		synchronized (this) {
			for (Runnable task : tasks) {
				Thread thread = null;
				if (task instanceof RenderTask) {
					thread = ((RenderTask) task).getThread();
				}
				if (thread != null) {
					thread.interrupt();
				}
			}
		}
	}

	public void render(RenderTask task) {
		executor.execute(task);
	}
}

package com.yago.texscanner.model;

import android.graphics.Bitmap;
import android.os.Process;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class RenderTask implements Runnable {

	private final Bitmap img;
	private final ImageView holder;
	private final MapConfig config;
	private Thread thread;

	public RenderTask(ImageView holder, MapConfig config, Bitmap base) {
		this.holder = holder;
		this.config = config;
		this.img = base;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	@Override
	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		setThread(Thread.currentThread());
		List<Thread> threads = new ArrayList<>();
		Bitmap out = Bitmap.createBitmap(img);

		for (int i = 0; i < 4; i++) {
			int finalI = i;
			threads.add(new Thread(() -> {
				Bitmap sample = Bitmap.createBitmap(config.render(out), 0, (out.getHeight() / 4) * finalI, out.getWidth(), out.getHeight() / 4);
				int[] pixels = new int[sample.getWidth() * sample.getWidth()];
				sample.getPixels(pixels, 0, sample.getWidth(), 0, 0, sample.getWidth(), sample.getHeight());
				out.setPixels(pixels, 0, sample.getWidth(), 0, sample.getHeight() * finalI, sample.getWidth(), sample.getHeight());
			}));
		}

		for (Thread t : threads) {
			t.start();
		}

		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		holder.post(() -> holder.setImageBitmap(out));
		config.setBuffer(out);
	}
}

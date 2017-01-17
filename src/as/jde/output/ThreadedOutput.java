package as.jde.output;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import fjs.jde.graph.CallClazzGraph;

import as.jde.graph.CallGraph;

public class ThreadedOutput {
	private XMLOutput fOut;
	private BlockingQueue<CallGraph> fQueue;
	private BlockingQueue<CallClazzGraph> fQueueC;
	private boolean fStop;
	private QueueWorker fWorker = new QueueWorker();
	
	public ThreadedOutput(XMLOutput output) {
		fOut = output;
		fQueue = new ArrayBlockingQueue<CallGraph>(1000);
		fQueueC = new ArrayBlockingQueue<CallClazzGraph>(1000);
	}
	
	public ThreadedOutput(XMLOutput output, int queueSize) {
		fOut = output;
		if (queueSize != -1) {
			fQueue = new ArrayBlockingQueue<CallGraph>(queueSize);
			fQueueC = new ArrayBlockingQueue<CallClazzGraph>(queueSize);
		} else {
			fQueue = new LinkedBlockingQueue<CallGraph>();
			fQueueC = new LinkedBlockingQueue<CallClazzGraph>();
		}
	}
	
	public void add(CallGraph cg) throws InterruptedException {
		while (!fQueue.offer(cg,1000,TimeUnit.MILLISECONDS)) {}
	}
	
	public void add(CallClazzGraph ccg) throws InterruptedException {
		while (!fQueueC.offer(ccg,1000,TimeUnit.MILLISECONDS)) {}
	}
	
	public void start(String projectName) throws IOException {
		fOut.startOutput(projectName);
		fStop = false;
		fWorker.start();
	}
	
	public void stop() throws IOException, InterruptedException {
		fStop = true;
		fWorker.join();
		fOut.stopOutput();
	}
	
	class QueueWorker extends Thread {
		public void run() {
			while (!fStop || !fQueue.isEmpty()) {
				try {
					CallGraph cg = fQueue.poll(100, TimeUnit.MILLISECONDS);
					if (cg != null) {
						fOut.output(cg);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (TransformerException e) {
					e.printStackTrace();
				}				
			}
		}
	}
}

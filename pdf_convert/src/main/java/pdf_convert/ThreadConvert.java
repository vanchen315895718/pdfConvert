package pdf_convert;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CountDownLatch;

import pdf_convert.controller.ConvertController;

/**
 * @author TC20006
 * 多线程处理 转化pdf
 */
public class ThreadConvert {
	/**
	* @Title: handlMap 
	* @Description: TODO 
	* @param data >>>待处理的map集合
	* @param threadNum >>>启动线程数
	* @author TC20006
	 * @throws InterruptedException 
	* @date 2018年9月29日下午2:29:56
	 */
	public void handlList(List<String> data,int threadNum) throws InterruptedException{
		int length = data.size();
		int dealLength = length % threadNum == 0 ? length / threadNum :(length / threadNum + 1);
		CountDownLatch countDownLatch = new CountDownLatch(threadNum);
		CountDownLatch sizeDate = new CountDownLatch(data.size()); //处理计数
		for (int i = 0; i < threadNum; i++) {
			int end = (i+1) * dealLength;
			HandleThread thread = new HandleThread("线程["+(i+1)+"]", data, i * dealLength, end > length ? length : end,countDownLatch,sizeDate);
			thread.start();
		}
		countDownLatch.await();
		sizeDate.await();
	}
	
	
	
	class HandleThread extends Thread {
		private CountDownLatch countDownLatch = null;
		private CountDownLatch sizeDate = null;
		private String threadName;
		private List<String> data;
		private int start;
		private int end;

		public HandleThread(String threadName, List<String> data, int start, int end,CountDownLatch countDownLatch,CountDownLatch sizeDate) {
			this.threadName = threadName;
			this.data = data;
			this.start = start;
			this.end = end;
			this.countDownLatch = countDownLatch;
			this.sizeDate = sizeDate;
		}

		public void run() {
				System.out.println(this.threadName+":"+System.currentTimeMillis());
				List<String> subList = data.subList(start, end);
				for (String fileUrl : subList) {
					try {
						ConvertMain.convertPDF2JPG(fileUrl);
						sizeDate.countDown();
						System.out.println("处理剩余"+sizeDate.getCount());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("转化异常:"+fileUrl);
						e.printStackTrace();
					} catch (Exception e){
						System.out.println("其他异常:"+fileUrl);
						e.printStackTrace();
					}
				}
				countDownLatch.countDown();
				System.out.println(threadName + "处理了" + subList.size() + "条！");
		}

	}

}

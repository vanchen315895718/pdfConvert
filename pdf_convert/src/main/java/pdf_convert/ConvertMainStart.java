package pdf_convert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;

/**
 * @author TC20006
 *
 */
@SpringBootApplication
@PropertySource("classpath:conf/application.properties")
public class ConvertMainStart {
	public static List<String> pdfCache = Collections.synchronizedList(new LinkedList());
	
	public static void main(String[] args) throws IOException {
		long begin = System.currentTimeMillis();
		ConfigurableApplicationContext  context=SpringApplication.run(ConvertMainStart.class, args);
		String url=context.getEnvironment().getProperty("convertpdf2jpg.url");
		System.out.println("扫描目录："+url+" 下的pdf文件开始");
		getPdfUrl(url);
		System.out.println("扫描到pdf文件共："+pdfCache.size()+"条");
		System.out.println("转化执行开始>>>>>>>>>>>>>>>>>>>>>>>>>> start");
		
		ThreadConvert convert = new ThreadConvert();
		convert.handlList(pdfCache, 3);
		
        System.out.println("转化执行结束>>>>>>>>>>>>>>>>>>>>>>>>>> end");
        long end = System.currentTimeMillis();
        System.out.println(">>>>>共耗费:"+(end-begin));
    }
	
	public static void getPdfUrl(String url) throws IOException{
		File file = new File(url);
		File[] fs = file.listFiles();
		for(int i = 0; i<fs.length;i++){	
			if(fs[i].isDirectory()){	
				getPdfUrl(fs[i].toString());       //递归操作，逐一遍历各文件夹内的文件			
				}else {
					if(fs[i].getPath().endsWith("pdf")||fs[i].getPath().endsWith("PDF")){
						pdfCache.add(fs[i].getPath());
						//ConvertMain.convertPDF2JPG(fs[i].getPath());
					}
				}		
		}
	}
	
	
}

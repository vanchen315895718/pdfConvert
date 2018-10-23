package pdf_convert.controller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import pdf_convert.util.MessageReader;

/**
 * @author TC20006
 *
 */
@Component
public class StartExecution implements CommandLineRunner{

	/* (non Javadoc) 
	 * @Title: run
	 * @Description: 启动执行
	 * @param args
	 * @throws Exception 
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[]) 
	 */ 
	@Override
	public void run(String... args) throws Exception {
		System.out.println("【MAX_CONVERT_PAGE】pdf转图片最大页数:"+MessageReader.getMessage("convert.maxpage"));
		System.out.println("【PIC_DEFINITION_DPI】pdf转图片清晰度:"+MessageReader.getMessage("convert.dpi"));
		
	}

}

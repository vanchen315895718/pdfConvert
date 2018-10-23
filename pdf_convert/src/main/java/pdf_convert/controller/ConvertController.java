package pdf_convert.controller;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import pdf_convert.ConvertMain;
import pdf_convert.ThreadConvert;
import pdf_convert.util.FileAppend;
import pdf_convert.util.MessageReader;



/**
 * @author TC20006
 *
 */
@Controller
public class ConvertController {
	public static List<String> pdfCache = Collections.synchronizedList(new LinkedList());
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request) {
		return "convert";
	}
	
	
	@RequestMapping("/convert")
	public void convertMain(HttpServletRequest request,HttpServletResponse reponse) throws Exception {
		String path = request.getParameter("convertPath");
		System.out.println("convertPath->"+path);
		if(path.endsWith(".pdf")||path.endsWith(".PDF")){//单个
			try {
				ConvertMain.convertPDF2JPG(path);
			} catch (IOException e) {
				System.out.println("转化异常:"+path);
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("其他异常:"+path);
				e.printStackTrace();
			}
		}else{//批量
			FileAppend.appendMethodA(MessageReader.getMessage("convert.scanLogurl"), "扫描时间："+new Date());
			FileAppend.appendMethodA(MessageReader.getMessage("convert.scanLogurl"), "所有PDF文件:");
			pdfCache.clear();
			getPdfUrl(path,MessageReader.getMessage("convert.scanLogurl"));
			FileAppend.appendMethodA(MessageReader.getMessage("convert.scanLogurl"), "共扫描文件"+pdfCache.size()+"个");
			ThreadConvert convert = new ThreadConvert();
			convert.handlList(pdfCache, 5);
		}
		System.out.println("处理完成");
	}
	
	
	
	
	
	public static void getPdfUrl(String url,String scanUrl) throws IOException{
			File file = new File(url);
			File[] fs = file.listFiles();
			for(int i = 0; i<fs.length;i++){	
				if(fs[i].isDirectory()){	
					getPdfUrl(fs[i].toString(),scanUrl);       //递归操作，逐一遍历各文件夹内的文件			
				}else {
					if(fs[i].getPath().endsWith("pdf")||fs[i].getPath().endsWith("PDF")){
						pdfCache.add(fs[i].getPath());
						FileAppend.appendMethodA(scanUrl , fs[i].getPath());
					}
				}		
			}
	}
}

package pdf_convert.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * @author TC20006
 *
 */
public class FileAppend {
	   public static void appendMethodA(String fileName, String content) {
		   try {
			   	File file = new File(fileName);
			   	if(!file.exists()){
			   		file.createNewFile();
			   	}
			   	FileWriter writer = new FileWriter(fileName, true);
		   		writer.write(content+"\r\n");
		   		writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

}

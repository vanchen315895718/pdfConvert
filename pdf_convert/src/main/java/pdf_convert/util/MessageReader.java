package pdf_convert.util;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author TC20006
 *
 */
public class MessageReader {
	private static final String PROPERTIES_NAME = "/var/www/webapps/config/crm-pdf/conf.properties"; //pro
	//private static final String PROPERTIES_NAME = "E:/config/conf.properties"; //local
	
	public static String getMessage(String key){
		Properties properties = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(PROPERTIES_NAME);
			properties.load(in);
			return (String) properties.get(key);
		} catch (Exception e) {
			System.out.println("配置文件读取异常");
			e.printStackTrace();
		}
		return "";
	}
	
}

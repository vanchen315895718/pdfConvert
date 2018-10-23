package pdf_convert;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.lowagie.text.pdf.PdfReader;

import pdf_convert.util.FileAppend;
import pdf_convert.util.MessageReader;

/**
 * @author TC20006
 */
public class ConvertMain {
	
	public final static String MAX_CONVERT_PAGE = MessageReader.getMessage("convert.maxpage"); //pdf转png最大页数
	public final static String PIC_DEFINITION_DPI = MessageReader.getMessage("convert.dpi"); //图片清晰度dpi，数值越大图片越清晰，占用内存越高。 
	
	public static String convertPDF2JPG(String fileUrl) throws IOException{
			File file = new File(fileUrl);
			PDDocument pdDocument = null;
			try {
				String imgPDFPath = file.getParent();
				int dot = file.getName().lastIndexOf('.');
				String imagePDFName = file.getName().substring(0, dot); // 获取图片文件名
				String imgFolderPath = null;
				imgFolderPath = imgPDFPath + File.separator;// 获取图片存放的文件夹路径			
				pdDocument = PDDocument.load(file);
				PDFRenderer renderer = new PDFRenderer(pdDocument);
				/* dpi越大转换后越清晰，相对转换速度越慢 */
				PdfReader reader = new PdfReader(fileUrl);
				int pages = reader.getNumberOfPages();
				if(pages>Integer.valueOf(MAX_CONVERT_PAGE)){
					System.out.println(new Date() + "【WARM】 未转化PDF文件："+fileUrl+"页数："+pages);
					FileAppend.appendMethodA(MessageReader.getMessage("convert.failLogurl") , new Date()+"【WARM】 未转化PDF文件："+fileUrl+"页数："+pages);
					return "";
				}
				StringBuffer imgFilePath = null;
				BufferedImage image = null;
				String imgFilePathPrefix = imgFolderPath + File.separator + imagePDFName;
				imgFilePath = new StringBuffer();
				imgFilePath.append(imgFilePathPrefix);
				imgFilePath.append(".png");
				File dstFile = new File(imgFilePath.toString());
				for (int i = 0; i < pages; i++) {
					BufferedImage imageBim = renderer.renderImageWithDPI(i, Long.parseLong(PIC_DEFINITION_DPI),ImageType.RGB);
					if (i == 0) {
						image = imageBim;
					} else {
						image = merge(image, imageBim);
					}
				}
				ImageIO.write(image, "png", dstFile);
				System.out.println("PDF文档转PNG图片成功！"+fileUrl);
				FileAppend.appendMethodA(MessageReader.getMessage("convert.successLogurl") , new Date()+"PDF文档转PNG图片成功！"+fileUrl);
			}catch(FileNotFoundException e){
				System.out.println("未发现文件："+fileUrl);
			}catch (Exception e) {
				System.out.println("转化失败:"+fileUrl);
				FileAppend.appendMethodA(MessageReader.getMessage("convert.failLogurl") , new Date()+"【WARM】 未转化PDF文件："+fileUrl);
				e.printStackTrace();
			} finally {
				try {
					pdDocument.close();
				} catch (Exception e2) {
					FileAppend.appendMethodA(MessageReader.getMessage("convert.failLogurl"), "pdDocument为空，关闭异常");
				}
			}
		return "";
	}
	
	
    private static BufferedImage merge(BufferedImage image1, BufferedImage image2) {
        BufferedImage combined = new BufferedImage(
                image1.getWidth(),
                image1.getHeight() + image2.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics g = combined.getGraphics();
        g.drawImage(image1, 0, 0, null);
        g.drawImage(image2, 0, image1.getHeight(), null);
        g.dispose();
        return combined;
    }

}

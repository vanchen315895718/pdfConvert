package pdf_convert;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.lowagie.text.pdf.PdfReader;

/**
 * @author TC20006
 */
public class ConvertMain {
	
	public final static int MAX_CONVERT_PAGE = 5; //pdf转png最大页数
	public final static float PIC_DEFINITION_DPI = 300f; //图片清晰度dpi，数值越大图片越清晰，占用内存越高。 
	
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
				if(pages>MAX_CONVERT_PAGE){
					System.out.println("【WARM】 未转化PDF文件："+fileUrl+"页数："+pages);
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
					BufferedImage imageBim = renderer.renderImageWithDPI(i, PIC_DEFINITION_DPI,ImageType.RGB);
					if (i == 0) {
						image = imageBim;
	                } else {
	                	image = merge(image, imageBim);
	                }
				}
				ImageIO.write(image, "png", dstFile);
				System.out.println("PDF文档转PNG图片成功！"+fileUrl);
		} catch (Exception e) {
			System.out.println("转化失败:"+fileUrl);
			e.printStackTrace();
		} finally {
			pdDocument.close();
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

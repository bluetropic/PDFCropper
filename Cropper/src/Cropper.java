
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.util.ImageIOUtil;

/**
 * Crop an rectangle area in a PDF document to an image.
 *
 * @author <a href="dongzhaoan@163.com">Zhaoan Dong</a>
 * @version $Revision: 1.0 $
 */
public class Cropper {
	int DEFAULT_USER_SPACE_UNIT_DPI = 72;

	public void crop(String fileName, int pageNum, PDRectangle rec,
			String outPath) {
		String saveFileName = outPath + getNameStr(fileName) + "_"
				+ String.valueOf(pageNum) + ".pdf";
		String imageName = outPath + getNameStr(fileName) + "_"
				+ String.valueOf(pageNum) + ".png";

		try {
			PDDocument document = PDDocument.load(fileName);
			PDDocument saveFile = new PDDocument();
			@SuppressWarnings("unchecked")
			List<PDPage> pages = document.getDocumentCatalog().getAllPages();
			PDPage page = (PDPage) pages.get(pageNum - 1);
			setBox(page, rec);
			saveFile.addPage(page);

			boolean covImgOK = convertToImg(page, imageName,
					BufferedImage.TYPE_INT_RGB, 2 * DEFAULT_USER_SPACE_UNIT_DPI);

			if (covImgOK) {
				System.out.println("page_" + String.valueOf(pageNum) + " of "
						+ getNameStr(fileName) + " is converted to image. OK!");
			} else {
				System.out.println("page " + String.valueOf(pageNum)
						+ " is fail to be converted into image. Failed");
			}
			try {
				saveFile.save(saveFileName);
				saveFile.close();
			} catch (COSVisitorException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public PDRectangle getRectangle(float LowerLeftX, float LowerLeftY,
			float UpperRightX, float UpperRightY) {
		PDRectangle rec = new PDRectangle();
		rec.setLowerLeftX(LowerLeftX);
		rec.setLowerLeftY(LowerLeftY);
		rec.setUpperRightX(UpperRightX);
		rec.setUpperRightY(UpperRightY);
		return rec;
	}

	private void setBox(PDPage page, PDRectangle rec) {
		page.setMediaBox(rec);
		page.setCropBox(rec);
		page.setArtBox(rec);
	}

	private boolean convertToImg(PDPage page, String fileName, int imageType,
			int resolution) {
		boolean bSuccess = true;

		try {
			BufferedImage image = page.convertToImage(imageType, resolution);
			bSuccess &= ImageIOUtil.writeImage(image, fileName, resolution);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bSuccess;
	}

	private String getNameStr(String fileName) {
		int start = fileName.lastIndexOf("\\");
		int end = fileName.lastIndexOf(".pdf");
		return fileName.substring(start + 1, end);
	}
}

import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		float LowerLeftX = 95;
		float LowerLeftY = 552;
		float UpperRightX = 510;
		float UpperRightY = 740;
		String fileName = "C:\\test\\test01.pdf";
		int pageNum = 3;
		String outPath = "C:\\test\\";
		Cropper cp = new Cropper();

		PDRectangle rec = cp.getRectangle(LowerLeftX, LowerLeftY, UpperRightX,
				UpperRightY);
		cp.crop(fileName, pageNum, rec, outPath);
	}

}

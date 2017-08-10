package marriage.sitdown.generator;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class Convert {

	public static void main(String[] args) {

		File base = new File("base.pdf");
		File teszt = new File("teszt.pdf");

		try {
			PDDocument basePdDoc = PDDocument.load(base);
			System.out.println(basePdDoc.getNumberOfPages());
			PDDocument tesztPdDoc = PDDocument.load(teszt);
			
			PDPage page0 = basePdDoc.getPage(0);
			PDPage page1 = tesztPdDoc.getPage(1);
			
			
			
			PDDocument basePdDocNew = new PDDocument();
			basePdDocNew.addPage(page0);
			basePdDocNew.addPage(page1);
			
			for(int i = 2; i < basePdDoc.getNumberOfPages(); i++) {
				basePdDocNew.addPage(basePdDoc.getPage(i));
			}
			
			basePdDocNew.save("out.pdf");
			basePdDocNew.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// PDDocument document = new PDDocument();
		// for (String pdfFile: pdfFiles) {
		// PDDocument part = PDDocument.load(pdfFile);
		// List<PDPage> list = part.getDocumentCatalog().getAllPages();
		// for (PDPage page: list) {
		// document.addPage(page);
		// }
		// part.close();
		// }
		// document.save("merged.pdf");
		// document.close();
	}

}

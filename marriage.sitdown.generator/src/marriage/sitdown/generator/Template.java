package marriage.sitdown.generator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
import org.apache.pdfbox.pdmodel.font.encoding.Type1Encoding;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

public class Template {

	private static float realMargin = 5;
	private static float margin;
	private static final float w = 210;
	private static final float h = 297;

	// private static final Color c = new Color(228, 218, 217);
	private static final Color c = new Color(44, 84, 45);
	private static final Color bordo = new Color(108, 5, 25);

	private static float height = PDRectangle.A4.getHeight();
	private static float width = PDRectangle.A4.getWidth();

	private static final int numberOfCol = 2;
	private static final int numberOfRow = 3;

	private static final float fontSize = 30;

	private static final float offsetLeft = 100;
	private static final float offsetRight = 40;

	private static final float verticalOffset = 30;

	private static float startPoint;

	private static boolean debug = false;

	public static PDDocument create(List<String> nameList) {

		System.out.println("width: " + width);
		System.out.println("height: " + height);

		float wScale = width / w;
		float hScale = height / h;

		margin = realMargin * wScale;

		width -= 2 * margin;
		height -= 2 * margin;
		startPoint = margin;

		float widthPart = width / numberOfCol;
		float heightPart = height / numberOfRow;

		PDDocument doc = new PDDocument();

		PDTrueTypeFont font = null;
		try {

			// PDSimpleFont fontLoaded = PDTrueTypeFont.loadTTF(doc, new
			// File("C:\\Windows\\Fonts\\Gabriola.ttf"));
			// PDSimpleFont fontLoaded = PDTrueTypeFont.loadTTF(doc, new
			// File("C:\\Windows\\Fonts\\arial.ttf"));
			PDTrueTypeFont fontLoaded = PDTrueTypeFont.loadTTF(doc, new File("ArchitectsDaughter.ttf"));
			
			font = fontLoaded;

			PDImageXObject ximageBase = PDImageXObject.createFromFile("salamon.png", doc);
			PDImageXObject ximageRotated = PDImageXObject.createFromFile("salamon.png", doc);

			float div = nameList.size() / 6.0f;
			double ceil = Math.ceil(div);
			int pageNumber = ((Double) ceil).intValue();
			int counter = 0;
			float half = heightPart / 2f;
			System.out.println("half: " + half);

			for (int p = 0; p < pageNumber; p++) {
				PDPage page = new PDPage(PDRectangle.A4);

				PDPageContentStream cos = new PDPageContentStream(doc, page);

				for (int i = 0; i < numberOfCol; i++) {

					for (int j = 0; j < numberOfRow; j++) {
						float posX = startPoint + i * widthPart;
						float posY = startPoint + j * heightPart;

						System.out.println("(" + posX + "," + posY + ")");

						paintImage(posX, posY, ximageBase, cos, widthPart, half);

						paintImage(posX, posY + half, ximageRotated, cos, widthPart, half);

						PDRectangle rect = new PDRectangle(widthPart, half);
						rect.setLowerLeftX(posX + offsetLeft);
						rect.setLowerLeftY(posY + 2 * verticalOffset);
						rect.setUpperRightX(posX + widthPart - offsetRight);
						rect.setUpperRightY(posY + half - verticalOffset);

						if (counter < nameList.size()) {
							if (debug) {
								drawRectangle(rect, cos);
							}
							writeText(font, fontSize, nameList.get(counter), cos, rect, false);

							rect.setLowerLeftY(rect.getLowerLeftY() + half - 2 * verticalOffset);
							rect.setUpperRightY(rect.getUpperRightY() + half - verticalOffset / 2);

							if (debug) {
								drawRectangle(rect, cos);
							}
							writeText(font, fontSize, nameList.get(counter), cos, rect, true);
						}
						counter++;
						cos.setStrokingColor(bordo);
						cos.setNonStrokingColor(bordo);
						cos.drawLine(posX, posY, posX + 20, posY);
						cos.drawLine(posX + widthPart - 20, posY, posX + widthPart, posY);

						// vizszintes vonal
						cos.setLineDashPattern(new float[] { 3 }, 0);
						cos.drawLine(posX, posY + half, posX + widthPart, posY + half);
						cos.setLineDashPattern(new float[] { 0 }, 0);
					}

				}

				cos.setStrokingColor(c);
				cos.setNonStrokingColor(c);
				// fuggoleges vonal
				cos.drawLine(width / 2, 0, width / 2, 20);
				cos.drawLine(width / 2, height - 20, width / 2, height);

				cos.close();
				doc.addPage(page);

				PDFRenderer pdfRenderer = new PDFRenderer(doc);
				BufferedImage bim = pdfRenderer.renderImageWithDPI(p, 300, ImageType.RGB);
				ImageIOUtil.writeImage(bim, "page_salamon" + p + ".png", 300);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// TODO do there the document;

		return doc;
	}

	private static void writeText(PDFont font, float fontSize, String text, PDPageContentStream cos, PDRectangle rect,
			boolean rotated) throws IOException {
		// drawRectangle(rect, cos);

		text = text.replaceAll("õ", "o");
		text = text.replaceAll("Õ", "O");
		text = text.replaceAll("û", "u");
		text = text.replaceAll("Û", "U");
		float textWidth = font.getStringWidth(text) / 1000 * fontSize;

		while (textWidth > rect.getWidth()) {
			fontSize -= 1f;
			textWidth = font.getStringWidth(text) / 1000 * fontSize;
		}

		float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

		float centerX = rect.getLowerLeftX() + (rect.getWidth() / 2f);
		float centerY = rect.getLowerLeftY() + (rect.getHeight() / 2f);

		float x = centerX - (textWidth / 2f);
		float y = centerY - (textHeight / 2f);

		if (debug) {
			cos.drawLine(x, y, x + textWidth, y);
			cos.drawLine(x, y, x, y + textHeight);
		}

		cos.beginText();
		cos.setFont(font, fontSize);
		cos.setNonStrokingColor(bordo);

		if (rotated) {
			cos.moveTextPositionByAmount(x, y);
			cos.setTextRotation(Math.PI, centerX + textWidth / 2, centerY + textHeight / 2);
		} else {
			cos.moveTextPositionByAmount(x, y);
		}
		cos.showText(text);
		// System.out.println(text);
		cos.endText();

		if (debug) {
			cos.drawLine(centerX, centerY, rect.getUpperRightX(), rect.getUpperRightY());
		}
	}

	private static void paintImage(float x, float y, PDImageXObject ximage, PDPageContentStream cos, float width,
			float height) throws IOException {

		cos.drawImage(ximage, x, y, width, height);
	}

	private static void drawRectangle(PDRectangle rect, PDPageContentStream cos) throws IOException {

		cos.setStrokingColor(bordo);
		cos.drawLine(rect.getLowerLeftX(), rect.getLowerLeftY(), rect.getUpperRightX(), rect.getUpperRightY());
		cos.drawLine(rect.getLowerLeftX(), rect.getLowerLeftY() + rect.getHeight(), rect.getUpperRightX(),
				rect.getUpperRightY() - rect.getHeight());

	}

}

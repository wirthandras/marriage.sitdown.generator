package marriage.sitdown.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

public class Generator {

	public static final String pdfExtension = ".pdf";

	public static void main(String[] args) {

		List<String> names = getNames();

		PDDocument doc = Template.create(names);
		String filename = "ultetesSalamonV1";

		save(doc, filename);
	}

	private static void save(PDDocument doc, String filename) {
		try {

			doc.save(filename + pdfExtension);
			doc.close();
		} catch (Exception io) {
			System.out.println(io);
		}
	}

	private static List<String> getNames() {
		File file = new File("list3");
		List<String> names = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
			String line;
			while ((line = br.readLine()) != null) {
				names.add(line);
			}
			br.close();
			return names;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return names;
	}
}

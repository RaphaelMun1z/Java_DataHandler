package main.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.Ruling;
import technology.tabula.Table;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

public class TableExtractor {
	public static List<Table> extractTableFromPDF(String filePath, int startPage) throws IOException {
		try (PDDocument document = Loader.loadPDF(FileHandler.getPdfFile(filePath));
				ObjectExtractor extractor = new ObjectExtractor(document)) {

			SpreadsheetExtractionAlgorithm spreadsheetExtractor = new SpreadsheetExtractionAlgorithm();
			PageIterator pages = extractor.extract();
			List<Table> tables = new ArrayList<>();

			while (pages.hasNext()) {
				Page page = pages.next();

				if (page.getPageNumber() < startPage)
					continue;

				List<Ruling> existingRulings = page.getRulings();
				List<Ruling> newVerticalRulings = new ArrayList<>();

				List<Float> columnPositions = Arrays.asList(50f, 100f, 150f, 200f, 250f, 300f, 350f, 400f, 450f, 500f,
						550f, 600f, 650f);

				for (float x : columnPositions) {
					newVerticalRulings.add(new Ruling(x, 0, x, (float) page.getHeight()));
				}

				existingRulings.addAll(newVerticalRulings);
				tables.addAll(spreadsheetExtractor.extract(page));
			}

			return tables;
		}
	}
}
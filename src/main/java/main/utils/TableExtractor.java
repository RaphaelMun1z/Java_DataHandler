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
	public static List<Table> extractTableFromPDF(String filePath, int startPage) {
		System.out.println("Início do processo de extração de dados do arquivo PDF...");

		List<Table> tables = new ArrayList<>();

		// Tenta abrir e processar o PDF
		try (PDDocument document = Loader.loadPDF(FileHandler.getFileWithExtension(filePath, ".pdf"));
				ObjectExtractor extractor = new ObjectExtractor(document)) {

			// Algoritmo para extração de tabelas no formato de planilha
			SpreadsheetExtractionAlgorithm spreadsheetExtractor = new SpreadsheetExtractionAlgorithm();

			// Iterator para percorrer as páginas do PDF
			PageIterator pages = extractor.extract();

			// Percorre todas as páginas do PDF
			while (pages.hasNext()) {
				Page page = pages.next();

				if (page.getPageNumber() < startPage)
					continue;

				// Linhas de separação existentes
				List<Ruling> existingRulings = page.getRulings();

				// Novas linhas de separação
				List<Ruling> newVerticalRulings = new ArrayList<>();

				List<Float> columnPositions = Arrays.asList(50f, 100f, 150f, 200f, 250f, 300f, 350f, 400f, 450f, 500f,
						550f, 600f, 650f);

				for (float x : columnPositions)
					newVerticalRulings.add(new Ruling(x, 0, x, (float) page.getHeight()));

				existingRulings.addAll(newVerticalRulings);
				tables.addAll(spreadsheetExtractor.extract(page));
			}

			System.out.println("Extração concluída com sucesso!");
		} catch (IOException e) {
			System.err.println("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return tables;
	}
}
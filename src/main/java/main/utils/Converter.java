package main.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;

public class Converter {
	public static void convertTableToCsv(List<Table> tables, String outputPath) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
			writer.println(
					"\"PROCEDIMENTO\",\"RN (alteração)\",\"VIGÊNCIA\",\"OD\",\"AMB\",\"HCO\",\"HSO\",\"REF\",\"PAC\",\"DUT\",\"SUBGRUPO\",\"GRUPO\",\"CAPÍTULO\"");

			for (Table table : tables) {
				for (List<RectangularTextContainer> row : table.getRows()) {
					if (!row.isEmpty() && row.get(0).getText().equals("PROCEDIMENTO"))
						continue;

					StringBuilder csvLine = new StringBuilder();
					for (RectangularTextContainer<?> cell : row) {
						String text = cell.getText().replace("\"", "\"\"").replace("\n", " ").replace("\r", " ").trim();
						csvLine.append("\"").append(text).append("\",");
					}

					if (csvLine.length() > 0)
						writer.println(csvLine.substring(0, csvLine.length() - 1));
				}
			}
		} catch (IOException e) {
			System.out.println("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
}
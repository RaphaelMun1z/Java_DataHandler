package main.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class CsvHandler {
	public static void updateCSV(String inputCsv) throws IOException, CsvValidationException {
		final String OD_label = "Seg. Odontológica";
		final String AMB_label = "Seg. Ambulatorial";

		String tempCsv = "Teste_Raphael_Muniz_Varela_temp.csv";

		try (Reader reader = Files.newBufferedReader(Paths.get(inputCsv));
				CSVReader csvReader = new CSVReader(reader);
				Writer writer = Files.newBufferedWriter(Paths.get(tempCsv));
				CSVWriter csvWriter = new CSVWriter(writer)) {
			String[] header = csvReader.readNext();
			if (header != null) {
				for (int ii = 0; ii < header.length; ii++) {
					if (header[ii].equals("OD")) {
						header[ii] = OD_label;
					} else if (header[ii].equals("AMB")) {
						header[ii] = AMB_label;
					}
				}
				csvWriter.writeNext(header);
			}

			String[] row;
			while ((row = csvReader.readNext()) != null) {
				csvWriter.writeNext(row);
			}
		}

		Files.move(Paths.get(tempCsv), Paths.get(inputCsv), StandardCopyOption.REPLACE_EXISTING);
		System.out.println("Arquivo CSV atualizado com sucesso!");
	}
}

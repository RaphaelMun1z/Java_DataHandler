package main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.opencsv.exceptions.CsvValidationException;

import main.utils.Compactor;
import main.utils.Converter;
import main.utils.CsvHandler;
import main.utils.FileHandler;
import main.utils.TableExtractor;
import technology.tabula.Table;

public class Main {
	public static void main(String[] args) {
		System.out.println("Iniciando processo de automação.");

		final String basePath = "C:\\PDFs";

		final String docsZipPath = basePath + "\\Anexos.zip";
		final String pdfFileName = "Anexo_I_Rol_2021RN_465.2021_RN627L.2024.pdf";
		final String docToExtractPath = basePath + "\\" + pdfFileName;

		final String csvFileName = "Teste_Raphael_Muniz_Varela.csv";
		final String csvPath = basePath + "\\" + csvFileName;
		final String csvZipPath = basePath + "\\Teste_Raphael_Muniz_Varela.zip";

		final int docStartPage = 3;

		try {
			System.out.println("Início do processo de extração do arquivo PDF...");
			FileHandler.extractFile(docsZipPath, pdfFileName, basePath);
			System.out.println("Arquivo PDF extraído com sucesso!");
		} catch (IOException e) {
			System.err.println("Erro ao extrair arquivo: " + e.getMessage());
		}

		try {
			System.out.println("Início do processo de extração de dados do arquivo PDF...");
			List<Table> tables = TableExtractor.extractTableFromPDF(docToExtractPath, docStartPage);
			Converter.convertTableToCsv(tables, csvPath);
			System.out.println("Extração concluída com sucesso!");
		} catch (IOException e) {
			System.err.println("Erro durante a extração: " + e.getMessage());
		}

		Compactor csvCompactor = new Compactor(csvZipPath);
		File csvFile = FileHandler.getCsvFile(csvPath);
		csvCompactor.addFileToZipFolder(csvFile);
		FileHandler.deleteFile(docToExtractPath);

		try {
			System.out.println("Início do processo de extração do arquivo CSV...");
			FileHandler.extractFile(csvZipPath, csvFileName, basePath);
			System.out.println("Arquivo CSV extraído com sucesso!");
		} catch (IOException e) {
			System.err.println("Erro ao extrair arquivo: " + e.getMessage());
		}

		try {
			CsvHandler.updateCSV(csvPath);
			File newCsvFile = FileHandler.getCsvFile(csvPath);
			csvCompactor.addFileToZipFolder(newCsvFile);
		} catch (IOException e) {
			System.err.println("Erro ao atualizar o arquivo CSV: " + e.getMessage());
		} catch (CsvValidationException e) {
			System.err.println("Erro ao atualizar o arquivo CSV: " + e.getMessage());
		}

		System.out.println("Finalizando processo de automação.");
	}
}
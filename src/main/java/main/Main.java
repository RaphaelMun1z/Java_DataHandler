package main;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

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

		final String docsZipPath = Paths.get(basePath, "Anexos.zip").toString();
		final String pdfFileName = "Anexo_I_Rol_2021RN_465.2021_RN627L.2024.pdf";
		final String docToExtractPath = Paths.get(basePath, pdfFileName).toString();

		final String csvFileName = "Teste_Raphael_Muniz_Varela.csv";
		final String csvPath = Paths.get(basePath, csvFileName).toString();
		final String csvZipPath = Paths.get(basePath, "Teste_Raphael_Muniz_Varela.zip").toString();

		final int docStartPage = 3;

		// Extrai o PDF 'Anexo I' do ZIP
		FileHandler.extractFile(docsZipPath, pdfFileName, basePath);

		// Extrai os dados da tabela do 'Anexo I'
		List<Table> pdfTables = TableExtractor.extractTableFromPDF(docToExtractPath, docStartPage);

		// Converte os dados da tabela para o modelo CSV
		Converter.convertTableToCsv(pdfTables, csvPath);

		// Captura o arquivo CSV
		File csvFile = FileHandler.getFileWithExtension(csvPath, ".csv");

		// Transfere o CSV para o ZIP
		Compactor.addFileToZipFolder(csvFile, csvZipPath);

		// Deleta a cópia do 'Anexo I'
		FileHandler.deleteFile(docToExtractPath);

		// Extrai o CSV do ZIP
		FileHandler.extractFile(csvZipPath, csvFileName, basePath);

		// Atualiza o header do CSV
		CsvHandler.updateCSV(csvPath);

		// Captura o arquivo CSV
		File newCsvFile = FileHandler.getFileWithExtension(csvPath, ".csv");

		// Transfere o CSV para o ZIP
		Compactor.addFileToZipFolder(newCsvFile, csvZipPath);

		System.out.println("Finalizando processo de automação.");
	}
}
package main.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileHandler {
	public static void extractFile(String zipFilePath, String targetFile, String destinationDir) throws IOException {
		Path destDir = Paths.get(destinationDir);
		if (!Files.exists(destDir))
			Files.createDirectories(destDir);

		try (ZipFile zipFile = new ZipFile(zipFilePath)) {
			ZipEntry entry = zipFile.getEntry(targetFile);
			if (entry == null)
				throw new FileNotFoundException("Arquivo não encontrado na pasta compactada!");

			Path destPath = destDir.resolve(entry.getName());
			try (InputStream is = zipFile.getInputStream(entry)) {
				Files.copy(is, destPath, StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}

	public static File getPdfFile(String filePath) {
		if (filePath == null || filePath.trim().isEmpty())
			throw new IllegalArgumentException("O caminho informado é inválido!");

		File targetFile = new File(filePath);

		if (!targetFile.exists())
			throw new IllegalArgumentException("O arquivo não foi encontrado!");

		if (!targetFile.getName().endsWith(".pdf"))
			throw new IllegalArgumentException("O arquivo não é um PDF");

		return targetFile;
	}

	public static File getCsvFile(String filePath) {
		if (filePath == null || filePath.trim().isEmpty())
			throw new IllegalArgumentException("O caminho informado é inválido!");

		File targetFile = new File(filePath);

		if (!targetFile.exists())
			throw new IllegalArgumentException("O arquivo não foi encontrado!");

		if (!targetFile.getName().endsWith(".csv"))
			throw new IllegalArgumentException("O arquivo não é um CSV");

		return targetFile;
	}

	public static void deleteFile(String filePath) {
		if (filePath == null || filePath.trim().isEmpty())
			throw new IllegalArgumentException("O caminho informado é inválido!");

		File targetFile = new File(filePath);

		if (!targetFile.exists())
			throw new IllegalArgumentException("O arquivo não foi encontrado!");

		if (targetFile.delete())
			System.out.println("Arquivo " + targetFile.getName() + " removido com sucesso!");
		else
			throw new IllegalArgumentException("Não foi possível remover o arquivo!");
	}
}

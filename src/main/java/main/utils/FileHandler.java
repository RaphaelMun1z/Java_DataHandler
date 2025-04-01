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
	public static void extractFile(String zipFilePath, String targetFile, String destinationDir) {
		try {
			System.out.println("Início do processo de extração do arquivo: " + targetFile);

			// Diretório destino
			Path destDir = Paths.get(destinationDir);

			// Verifica se o diretório ainda não existe
			if (!Files.exists(destDir))
				Files.createDirectories(destDir); // Se ainda não existir, ele é criado

			// Acessa o ZIP
			try (ZipFile zipFile = new ZipFile(zipFilePath)) {
				// Tenta capturar o arquivo dentro do ZIP
				ZipEntry entry = zipFile.getEntry(targetFile);

				// Quando não acha o arquivo no ZIP, o valor de 'entry' é null
				if (entry == null)
					throw new FileNotFoundException("Arquivo não encontrado na pasta compactada!");

				// Cria o caminho para o arquivo extraído
				Path destPath = destDir.resolve(entry.getName());
				try (InputStream is = zipFile.getInputStream(entry)) {
					Files.copy(is, destPath, StandardCopyOption.REPLACE_EXISTING);
					System.out.println("Arquivo extraído com sucesso!");
				}
			}
		} catch (IOException e) {
			System.err.println("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public static File getFileWithExtension(String filePath, String extension) {
		File targetFile = getTargetFile(filePath);

		if (!targetFile.getName().toLowerCase().endsWith(extension.toLowerCase()))
			throw new IllegalArgumentException("O arquivo não é um " + extension.toUpperCase());

		return targetFile;
	}

	public static void deleteFile(String filePath) {
		try {
			File targetFile = getTargetFile(filePath);

			if (targetFile.delete())
				System.out.println("Arquivo " + targetFile.getName() + " removido com sucesso!");
			else
				System.out.println("Erro: Não foi possível remover o arquivo " + targetFile.getName());
		} catch (Exception e) {
			System.out.println("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	private static File getTargetFile(String filePath) {
		if (filePath == null || filePath.trim().isEmpty())
			throw new IllegalArgumentException("O caminho informado é inválido!");

		File targetFile = new File(filePath);

		if (!targetFile.exists())
			throw new IllegalArgumentException("O arquivo não foi encontrado!");

		return targetFile;
	}
}

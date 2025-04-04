package main.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class Compactor {
	public static void addFileToZipFolder(File fileToBeTransferred, String zipFolderPath) {
		// Configurações do ZIP
		Map<String, String> zipConfig = new HashMap<>();
		// O ZIP vai ser criado caso não exista
		zipConfig.put("create", "true");
		zipConfig.put("encoding", "UTF-8");

		Path zipPath = Paths.get(zipFolderPath);
		URI zip_disk = URI.create("jar:" + zipPath.toUri());

		try (FileSystem zipfs = FileSystems.newFileSystem(zip_disk, zipConfig)) {
			Path originalFilePath = Paths.get(fileToBeTransferred.getAbsolutePath());
			Path zipFilePath = zipfs.getPath(fileToBeTransferred.getName());

			try {
				Files.copy(originalFilePath, zipFilePath, StandardCopyOption.REPLACE_EXISTING);
				fileToBeTransferred.delete();
				System.out.println("O arquivo " + fileToBeTransferred.getName()
						+ " foi transferido para a pasta compactada com sucesso!");
			} catch (IOException e) {
				System.out.println("Ocorreu um erro com o arquivo: " + e.getMessage());
				return;
			}
		} catch (Exception e) {
			System.out.println("Ocorreu um erro no sistema de compactar arquivos: " + e.getMessage());
			return;
		}
	}
}
package br.com.codigocomcafe.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value; // faltava import
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

@Service
public class UploadService {

    // Atributo especificando o diretório onde serão salvos os arquivos
    @Value("${file.upload-dir}")
    private String uploadDir; // corrigido de "uploaDir" para "uploadDir"

    // Método para remover todos os arquivos da pasta uploads ao iniciar o servidor
    @PostConstruct
    public void limparUploadsAoIniciar() {
        Path uploadPath = Paths.get(uploadDir);
        try {
            if (Files.exists(uploadPath)) {
                try (Stream<Path> files = Files.list(uploadPath)) { // corrigido a sintaxe do try com recurso
                    files.forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            System.err.println("Erro ao deletar arquivo: " + path.getFileName());
                        }
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao limpar pasta de uploads: " + e.getMessage());
        }
    }

    // Método para armazenar os arquivos
    public String armazenarArquivo(MultipartFile file) throws IOException { // corrigido o parâmetro de 'fille' para 'file'
        // Cria o diretório se não existir
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) { // corrigido os parênteses
            Files.createDirectories(uploadPath);
        }

        // Cria um nome único para o arquivo
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // Salva o arquivo no caminho
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING); // faltava os parênteses e vírgula

        // Retorna o nome do arquivo salvo
        return fileName;
    }

}


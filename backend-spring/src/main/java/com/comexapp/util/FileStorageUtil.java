package com.comexapp.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class FileStorageUtil {

    // Em produção configure um path absoluto (ex: /mnt/data/uploads) se necessário
    private static final String UPLOAD_DIR = "uploads";

    public String saveFiles(MultipartFile[] files) {
        if (files == null || files.length == 0) return null;
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            boolean ok = dir.mkdirs();
            if (!ok) throw new RuntimeException("Não foi possível criar diretório de upload");
        }

        return Arrays.stream(files)
                .map(f -> {
                    try {
                        String original = f.getOriginalFilename();
                        String safe = UUID.randomUUID().toString().substring(0,8) + "_" + sanitize(original);
                        Path dest = Paths.get(UPLOAD_DIR).resolve(safe);
                        // escreve o arquivo
                        Files.copy(f.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
                        return safe;
                    } catch (IOException e) {
                        throw new RuntimeException("Erro ao salvar arquivo: " + f.getOriginalFilename(), e);
                    }
                })
                .collect(Collectors.joining(","));
    }

    private String sanitize(String name) {
        if (name == null) return "file";
        return name.replaceAll("[^a-zA-Z0-9\\.\\-\\_]", "_");
    }
}

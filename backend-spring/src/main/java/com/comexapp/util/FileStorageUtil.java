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

    private static final String UPLOAD_DIR = "uploads/";

    public String saveFiles(MultipartFile[] files) {
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        return Arrays.stream(files)
                .map(f -> {
                    try {
                        String newName = UUID.randomUUID() + "_" + f.getOriginalFilename();
                        Path dest = Paths.get(UPLOAD_DIR + newName);
                        Files.copy(f.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
                        return newName;
                    } catch (IOException e) {
                        throw new RuntimeException("Erro ao salvar arquivo: " + f.getOriginalFilename(), e);
                    }
                })
                .collect(Collectors.joining(","));
    }
}

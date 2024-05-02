package com.example.puppet;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    public boolean needsUpdate(FileDefinition definition) throws IOException {
        Path localPath = Paths.get(definition.getLocalPath());
        if (!Files.exists(localPath)) {
            return true;
        }

        // Check if remote file has been modified (implement logic here)
        // For simplicity, assuming a check based on last modified time:
        long remoteLastModified = getRemoteLastModified(definition.getSourceUrl());
        long localLastModified = Files.getLastModifiedTime(localPath).toMillis();
        return remoteLastModified > localLastModified;
    }

    public void downloadAndWriteFile(FileDefinition definition) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(definition.getSourceUrl()))
                .GET()
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        if (response.statusCode() != 200) {
            throw new IOException("Failed to download file: " + definition.getSourceUrl());
        }

        Files.write(Paths.get(definition.getLocalPath()), response.body());
    }

    // Implement logic to get remote file's last modified time
    private long getRemoteLastModified(String url) throws IOException {
        // ... (replace with actual implementation)
        throw new UnsupportedOperationException("Remote file modification check not implemented");
    }
}

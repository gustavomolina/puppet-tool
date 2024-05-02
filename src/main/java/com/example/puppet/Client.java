package com.example.puppet;

import com.example.puppet.Configuration;
import com.example.puppet.FileManager;
import com.example.puppet.FileDefinition;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Client {

    private static final long POLL_INTERVAL = 5 * 60 * 1000; // 5 minutes in milliseconds
    private final String masterUrl;
    private final FileManager fileManager;

    public Client(String masterUrl, FileManager fileManager) {
        this.masterUrl = masterUrl;
        this.fileManager = fileManager;
    }

    public void start() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                fetchAndApplyConfiguration();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 0, POLL_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private void fetchAndApplyConfiguration() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(masterUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            System.out.println("Failed to fetch configuration: " + response.statusCode());
            return;
        }

        String configurationJson = response.body();

        System.out.println(configurationJson);


        ObjectMapper objectMapper = new ObjectMapper();

        Gson gson = new Gson();


        try {
            Configuration configuration = objectMapper.readValue(configurationJson, Configuration.class);
            System.out.println(configuration);
            // Apply configuration
            System.out.println("Received configuration:");
            System.out.println(configuration);
            applyFileDefinitions(configuration);
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void applyFileDefinitions(Configuration configuration) throws IOException, InterruptedException {
        System.out.println(configuration.getFileDefinitions().getFirst().getLocalPath());

        for (FileDefinition definition : configuration.getFileDefinitions()) {
            if (fileManager.needsUpdate(definition)) {
                fileManager.downloadAndWriteFile(definition);
                System.out.println("Updated file: " + definition.getLocalPath());
            } else {
                System.out.println("File up-to-date: " + definition.getLocalPath());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java -jar puppet-client.jar <master_url>");
            System.exit(1);
        }

        System.out.printf("1");

        String masterUrl = args[0];

        System.out.println('2');
        FileManager fileManager = new FileManager(); // Create FileManager instance
        Client client = new Client(masterUrl, fileManager);
        client.start();
    }
}
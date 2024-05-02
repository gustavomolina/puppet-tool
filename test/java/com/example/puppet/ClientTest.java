package com.example.puppet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ClientTest {

    private static Client client;
    private static FileManager mockFileManager;

    @BeforeAll // Setup before each test
    public static void setUp() throws Exception {
        mockFileManager = mock(FileManager.class);
        client = new Client("http://localhost:8080", mockFileManager);
    }

    @Test
    public void testFetchAndApplyConfiguration_successfulUpdate() throws IOException, InterruptedException {
        // Mock file manager behavior
        FileDefinition definition = new FileDefinition();
        definition.setLocalPath("/tmp/test.txt");
        definition.setSourceUrl("http://example.com/test.txt");
        when(mockFileManager.needsUpdate(definition)).thenReturn(true);


        // Verify file manager interactions
        verify(mockFileManager).needsUpdate(definition);
        verify(mockFileManager).downloadAndWriteFile(definition);
    }

    @Test
    public void testFetchAndApplyConfiguration_noUpdateNeeded() throws IOException, InterruptedException {
        // Mock file manager behavior
        FileDefinition definition = new FileDefinition();
        definition.setLocalPath("/tmp/test.txt");
        definition.setSourceUrl("http://example.com/test.txt");
        when(mockFileManager.needsUpdate(definition)).thenReturn(false); // No update needed

        // No interaction with downloadAndWriteFile expected
        verify(mockFileManager, times(0)).downloadAndWriteFile(definition);
    }


    private static <T> T any() {
        return null; // Any object, used with Mockito for flexible matching
    }
}

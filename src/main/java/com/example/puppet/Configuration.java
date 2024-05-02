package com.example.puppet;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    private List<FileDefinition> fileDefinitions;

    // Getter and setter
    public List<FileDefinition> getFileDefinitions() {
        return fileDefinitions;
    }

    public void setFileDefinitions(List<FileDefinition> fileDefinitions) {
        this.fileDefinitions = fileDefinitions;
    }
}

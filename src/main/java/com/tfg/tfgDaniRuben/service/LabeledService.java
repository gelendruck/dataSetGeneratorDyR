package com.tfg.tfgDaniRuben.service;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

@Data
@Component
public class LabeledService {

    public boolean registerImageForLabeled(String name, boolean good, File register) {
        String label;
        try {
            if (good) {
                label = name + " | " + "1\r\n";
            } else {
                label = name + " | " + "0\r\n";
            }
            Files.write(register.toPath(), label.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}


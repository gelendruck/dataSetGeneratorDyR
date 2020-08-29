package com.tfg.tfgDaniRuben.controller;

import com.tfg.tfgDaniRuben.Exceptions.ImageOutOfBoundsException;
import com.tfg.tfgDaniRuben.service.FileService;
import com.tfg.tfgDaniRuben.service.GeneratorService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/images")
@Slf4j
public class ImagesController {

    public static final int N_PREDETERMINED_CASES = 16;
    public static final String ETIQUETADO_TXT = "etiquetado.txt";
    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private FileService fileService;

    @GetMapping(path = "/wrong", produces = "application/zip")
    @ApiResponses({
            @ApiResponse(code = 200, message = "generated correctly"),
            @ApiResponse(code = 400, message = "Error while generating"),
    })
    public ResponseEntity<byte[]> generateRandomWrongCases(@RequestParam Integer quantity) throws IOException, ImageOutOfBoundsException {
        List<File> etiquetados = new ArrayList<>();
        List<BufferedImage> imagenes = new ArrayList<>();
        Random random = new Random();
        File labeled = new File(ETIQUETADO_TXT);
        for (int i = 0; i < quantity; i++) {
            Pair<List<BufferedImage>, File> imagen;
            imagen = generatorService.generateWrongCases(random.nextInt(4), random.nextInt(4), random.nextInt(4), 1);
            mergeFilesIntoFirstElement(Arrays.asList(labeled, imagen.getValue()));
            etiquetados.add(labeled);
            imagenes.add(imagen.getKey().get(0));
        }

        Pair<List<BufferedImage>, File> listFilePair = new Pair<>(imagenes, labeled);
        if (listFilePair == null) {
            return ResponseEntity.badRequest().build();
        }
        int count = generatorService.getImageCount();
        int totalSize = listFilePair.getKey().size();
        List<File> filesToZip = new ArrayList<>();
        for (int i = 0; i < totalSize; i++) {
            int numberName = count - totalSize + i;
            File outputfile = new File(numberName + ".png");
            ImageIO.write(listFilePair.getKey().get(i), "png", outputfile);
            filesToZip.add(outputfile);
        }
        filesToZip.add(listFilePair.getValue());
        byte[] bytes = fileService.zipArrayFiles(filesToZip);
        for (File fileToDelete : filesToZip) {
            fileToDelete.delete();
        }
        if (bytes != null) {
            return new ResponseEntity<>(bytes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/predetermined", produces = "application/zip")
    @ApiResponses({
            @ApiResponse(code = 200, message = "generated correctly"),
            @ApiResponse(code = 400, message = "Error while generating"),
    })
    public ResponseEntity<byte[]> generatePredeterminedCases(@RequestParam Integer quantity, @RequestParam Integer type) {
        try {
            Pair<List<BufferedImage>, File> listFilePair = generatorService.generatePredeterminedCases(quantity, type);
            if (listFilePair == null) {
                return ResponseEntity.badRequest().build();
            }
            int count = generatorService.getImageCount();
            int totalSize = listFilePair.getKey().size();
            List<File> filesToZip = new ArrayList<>();
            for (int i = 0; i < totalSize; i++) {
                int numberName = count - totalSize + i;
                File outputfile = new File(numberName + ".png");
                ImageIO.write(listFilePair.getKey().get(i), "png", outputfile);
                filesToZip.add(outputfile);
            }
            filesToZip.add(listFilePair.getValue());
            byte[] bytes = fileService.zipArrayFiles(filesToZip);
            for (File fileToDelete : filesToZip) {
                fileToDelete.delete();
            }
            if (bytes != null) {
                return new ResponseEntity<>(bytes, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ImageOutOfBoundsException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/predetermined/random", produces = "application/zip")
    @ApiResponses({
            @ApiResponse(code = 200, message = "generated correctly"),
            @ApiResponse(code = 400, message = "Error while generating"),
    })
    public ResponseEntity<byte[]> generatePredeterminedCasesRandom(@RequestParam Integer quantity) {
        try {
            int cases = quantity > N_PREDETERMINED_CASES ? N_PREDETERMINED_CASES : quantity;
            int x = quantity / cases;
            Pair<List<BufferedImage>, File> listFilePair;
            List<BufferedImage> key = new ArrayList<>();
            File toMerge = new File(ETIQUETADO_TXT);
            toMerge.createNewFile();

            listFilePair = generatorService.generatePredeterminedCases(x, 1);
            quantity = quantity - x;
            for (BufferedImage image : listFilePair.getKey()) {
                key.add(image);
            }
            FileCopyUtils.copy(listFilePair.getValue(), toMerge);
            for (int i = 2; i <= cases; i++) {
                if (i < cases) {
                    listFilePair = generatorService.generatePredeterminedCases(x, i);
                    mergeFilesIntoFirstElement(Arrays.asList(toMerge, listFilePair.getValue()));
                    for (BufferedImage image : listFilePair.getKey()) {
                        key.add(image);
                    }
                } else {
                    listFilePair = generatorService.generatePredeterminedCases(quantity, i);
                    mergeFilesIntoFirstElement(Arrays.asList(toMerge, listFilePair.getValue()));
                    for (BufferedImage image : listFilePair.getKey()) {
                        key.add(image);
                    }
                }
                quantity = quantity - x;
            }
            if (listFilePair == null) {
                return ResponseEntity.badRequest().build();
            }
            int count = generatorService.getImageCount();
            int totalSize = key.size();
            List<File> filesToZip = new ArrayList<>();
            for (int i = 0; i < totalSize; i++) {
                int numberName = count - totalSize + i;
                File outputfile = new File(numberName + ".png");
                ImageIO.write(key.get(i), "png", outputfile);
                filesToZip.add(outputfile);
            }
            int i = 0;
            filesToZip.add(toMerge);
            byte[] bytes = fileService.zipArrayFiles(filesToZip);
            for (File fileToDelete : filesToZip) {
                fileToDelete.delete();
            }
            if (bytes != null) {
                return new ResponseEntity<>(bytes, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ImageOutOfBoundsException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void mergeFilesIntoFirstElement(List<File> files) {

        FileWriter fstream = null;
        BufferedWriter out = null;
        try {
            fstream = new FileWriter(files.get(0), true);
            out = new BufferedWriter(fstream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        for (int i = 1; i < files.size(); i++) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(files.get(i));
                BufferedReader in = new BufferedReader(new InputStreamReader(fis));

                String aLine;
                while ((aLine = in.readLine()) != null) {
                    out.write(aLine);
                    out.newLine();
                }

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/generate")
@Slf4j
public class GeneratorController {

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private FileService fileService;

    @GetMapping(path = "/good", produces = "application/zip")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Generated correctly"),
            @ApiResponse(code = 400, message = "Error while generating"),
    })
    public ResponseEntity<byte[]> generateGoodCases(@RequestParam Integer actors, @RequestParam Integer arrows,
            @RequestParam Integer boxes, @RequestParam Integer quantity) {
        try {
            Pair<List<BufferedImage>, File> listFilePair = generatorService.generateGoodCases(actors, arrows, boxes, quantity);
            if (listFilePair == null) {
                return ResponseEntity.badRequest().build();
            }
            int count = generatorService.getImageCount();
            int totalSize = listFilePair.getKey().size();
            List <File> filesToZip = new ArrayList<>();
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
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ImageOutOfBoundsException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/wrong", produces = "application/zip")
    @ApiResponses({
            @ApiResponse(code = 200, message = "generated correctly"),
            @ApiResponse(code = 400, message = "Error while generating"),
    })
    public ResponseEntity<byte[]> generateWrongCases(@RequestParam Integer actors, @RequestParam Integer arrows,
            @RequestParam Integer boxes, @RequestParam Integer quantity) {
        try {
            Pair<List<BufferedImage>, File> listFilePair = generatorService.generateWrongCases(actors, arrows, boxes, quantity);
            if (listFilePair == null) {
                return ResponseEntity.badRequest().build();
            }
            int count = generatorService.getImageCount();
            int totalSize = listFilePair.getKey().size();
            List <File> filesToZip = new ArrayList<>();
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

    @GetMapping(path = "/random", produces = "application/zip")
    @ApiResponses({
            @ApiResponse(code = 200, message = "generated correctly"),
            @ApiResponse(code = 400, message = "Error while generating"),
    })
    public ResponseEntity<byte[]> generateRandomCases(@RequestParam Integer quantity) {
        try {
            Pair<List<BufferedImage>, File> listFilePair = generatorService.generateRandomCases(quantity);
            if (listFilePair == null) {
                return ResponseEntity.badRequest().build();
            }
            int count = generatorService.getImageCount();
            int totalSize = listFilePair.getKey().size();
            List <File> filesToZip = new ArrayList<>();
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
            List <File> filesToZip = new ArrayList<>();
            for (int i = 0; i < totalSize; i++) {
                int numberName = count - totalSize + i;
                File outputfile = new File( numberName + ".png");
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
            //TODO controlar cuando piden menos de 6 imagenes
            int x = quantity/6;
            Pair<List<BufferedImage>, File> listFilePair;
            List<BufferedImage> key = new ArrayList<>();
            List <File> labeleds = new ArrayList<>();
            for (int i = 1; i < 6; i++) {
                quantity = quantity - x;
                listFilePair = generatorService.generatePredeterminedCases(x, i);
                for ( BufferedImage image : listFilePair.getKey()) {
                    key.add(image);
                }
                labeleds.add(listFilePair.getValue());
            }

            listFilePair = generatorService.generatePredeterminedCases(quantity, 6);
            for (BufferedImage image : listFilePair.getKey()) {
                key.add(image);
            }

            Pair<List<BufferedImage>, List<File>> listListPair = new Pair<>(key, labeleds);
            if (listListPair == null) {
                return ResponseEntity.badRequest().build();
            }
            int count = generatorService.getImageCount();
            int totalSize = listListPair.getKey().size();
            List <File> filesToZip = new ArrayList<>();
            for (int i = 0; i < totalSize; i++) {
                int numberName = count - totalSize + i;
                File outputfile = new File( numberName + ".png");
                ImageIO.write(listListPair.getKey().get(i), "png", outputfile);
                filesToZip.add(outputfile);
            }
            int i = 0;
            mergeFilesIntoFirstElement(listListPair.getValue());
            filesToZip.add(listListPair.getValue().get(0));
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

    public void mergeFilesIntoFirstElement(List<File> files) {

        FileWriter fstream = null;
        BufferedWriter out = null;
        try {
            fstream = new FileWriter(files.get(0), true);
            out = new BufferedWriter(fstream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        for (int i = 1; i < files.size(); i++) {
            System.out.println("merging: " + files.get(i).getName());
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

package com.tfg.tfgDaniRuben.service;

import com.tfg.tfgDaniRuben.Exceptions.ImageOutOfBoundsException;
import javafx.util.Pair;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Data
@Component
public class GeneratorService {

    private final String textFilePath = "src/main/resources/labeled/labeled.txt";

    private final String actorPath = "src/main/resources/images/use_case_actor.png";

    private final String arrowPath = "src/main/resources/images/use_case_arrow.png";

    private final String boxPath = "src/main/resources/images/use_case_oval.png";

    private final String backgroundPath = "src/main/resources/images/background.png";

    private final String scenarioPath = "src/main/resources/images/predetermined_scenario";

    private Integer imageCount = 0;

    @Autowired
    private FileService fileService;

    @Autowired
    private LabeledService labeledService;

    @Autowired
    private TransformationService transformationService;

    public Pair<List<BufferedImage>, File> generateWrongCases(Integer actors, Integer arrows, Integer boxes, Integer quantity) throws IOException, ImageOutOfBoundsException {

        Pair<List<BufferedImage>, File> toReturn;
        File labeled = new File(textFilePath);
        if (labeled.exists()) {
            labeled.delete();
        }
        labeled.createNewFile();
        BufferedImage actor = fileService.loadImage(actorPath);
        BufferedImage arrow = fileService.loadImage(arrowPath);
        BufferedImage box = fileService.loadImage(boxPath);
        List<BufferedImage> imagesToReturn = new ArrayList<>();
        for (int i = imageCount; imageCount < i + quantity; imageCount++) {
            List<BufferedImage> imagesToBackground = new ArrayList<>();
            BufferedImage background = fileService.loadImage(backgroundPath);
            int backgroundHeight = background.getHeight();
            int backgroundWidth = background.getWidth();
            for (int j = 0; j < actors; j++) {
                Optional<BufferedImage> actorTrans = typeOfTransformation(backgroundHeight, backgroundWidth, actor);
                if (actorTrans.isPresent()) {
                    imagesToBackground.add(actorTrans.get());
                }
            }
            for (int j = 0; j < arrows; j++) {
                Optional<BufferedImage> arrowTrans = typeOfTransformation(backgroundHeight, backgroundWidth, arrow);
                if (arrowTrans.isPresent()) {
                    imagesToBackground.add(arrowTrans.get());
                }
            }
            for (int j = 0; j < boxes; j++) {
                Optional<BufferedImage> boxTrans = typeOfTransformation(backgroundHeight, backgroundWidth, box);
                if (boxTrans.isPresent()) {
                    imagesToBackground.add(boxTrans.get());
                }
            }
            Graphics2D g2d = background.createGraphics();
            for (BufferedImage element : imagesToBackground) {
                g2d.drawImage(element, 0, 0, null);
            }
            g2d.dispose();
            imagesToReturn.add(background);
            labeledService.registerImageForLabeled(imageCount.toString(), false, labeled);
        }
        toReturn = new Pair<>(imagesToReturn, labeled);
        return toReturn;
    }

    public Pair<List<BufferedImage>, File> generatePredeterminedCases(Integer quantity, Integer type) throws IOException, ImageOutOfBoundsException, IndexOutOfBoundsException {

        if (type < 1 || type > 16) {
            throw new IndexOutOfBoundsException();
        }
        Pair<List<BufferedImage>, File> toReturn;
        File labeled = new File(textFilePath);
        if (labeled.exists()) {
            labeled.delete();
        }
        labeled.createNewFile();
        BufferedImage scenario = fileService.loadImage(scenarioPath + "_" + type + ".png");
        List<BufferedImage> imagesToReturn = new ArrayList<>();
        for (int i = imageCount; imageCount < i + quantity; imageCount++) {
            final BufferedImage background = fileService.loadImage(backgroundPath);
            int backgroundHeight = background.getHeight();
            int backgroundWidth = background.getWidth();
            Optional<BufferedImage> scenarioTrans = typeOfTransformation(backgroundHeight, backgroundWidth, scenario);
            Graphics2D g2d = background.createGraphics();
            g2d.drawImage(scenarioTrans.get(), 0, 0, null);
            g2d.dispose();
            imagesToReturn.add(background);
            labeledService.registerImageForLabeled(imageCount.toString(), true, labeled);
        }
        toReturn = new Pair<>(imagesToReturn, labeled);
        return toReturn;
    }

    private Optional<BufferedImage> typeOfTransformation(int backgroundHeight, int backgroundWidth, BufferedImage img) throws ImageOutOfBoundsException {
        BufferedImage destinationImage = null;
        int option = new Random().nextInt(3);
        switch (option) {
            case (0): {
                destinationImage = transformationService.rotation(img, new Random().nextInt(359));
                destinationImage = transformationService.drawImage(backgroundWidth, backgroundHeight,
                        transformationService.makeColorTransparent(new Color(0, 0, 0), destinationImage));
                break;
            }
            case (1): {
                int positionY = new Random().nextInt(backgroundHeight - img.getHeight());
                int positionX = new Random().nextInt(backgroundWidth - img.getWidth());
                destinationImage = transformationService.traslation(img, backgroundWidth, backgroundHeight, positionX, positionY);
                break;
            }
            case (2): {
                destinationImage = transformationService.rotation(img, new Random().nextInt(359));

                int positionY = new Random().nextInt(backgroundHeight - destinationImage.getHeight());
                int positionX = new Random().nextInt(backgroundWidth - destinationImage.getWidth());
                destinationImage = transformationService.traslation(destinationImage, backgroundWidth, backgroundHeight, positionX, positionY);
                break;
            }
        }
        return Optional.of(destinationImage);
    }

    public int getImageCount() {
        return imageCount;
    }
}
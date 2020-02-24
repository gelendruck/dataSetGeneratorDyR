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

    private int imageCount = 0;

    @Autowired
    private FileService fileService;

    @Autowired
    private LabeledService labeledService;

    @Autowired
    private TransformationService transformationService;

    public Pair<List<BufferedImage>, File> generateGoodCases(Integer actors, Integer arrows, Integer boxes, Integer quantity)
            throws IOException, ImageOutOfBoundsException {

        Pair<List<BufferedImage>, File> toReturn;
        File labeled = new File(textFilePath);
        if (labeled.exists()) {
            boolean delete = labeled.delete();
        }
        labeled.createNewFile();
        if (goodValidation(actors, arrows, boxes)) {
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
                labeledService.registerImageForLabeled(imageCount + ".png", false, labeled);
            }
            toReturn = new Pair<>(imagesToReturn, labeled);
            return toReturn;
        }
        return null;
    }

    public Pair<List<BufferedImage>, File> generateWrongCases(Integer actors, Integer arrows, Integer boxes, Integer quantity) throws IOException, ImageOutOfBoundsException {

        Pair<List<BufferedImage>, File> toReturn;
        File labeled = new File(textFilePath);
        if (labeled.exists()) {
            boolean delete = labeled.delete();
        }
        labeled.createNewFile();
        if (!goodValidation(actors, arrows, boxes)) {
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
                labeledService.registerImageForLabeled(imageCount + ".png", false, labeled);
            }
            toReturn = new Pair<>(imagesToReturn, labeled);
            return toReturn;
        }
        return null;
    }

    public Pair<List<BufferedImage>, File> generateRandomCases(Integer quantity) throws IOException, ImageOutOfBoundsException {

        Pair<List<BufferedImage>, File> toReturn;
        File labeled = new File(textFilePath);
        if (labeled.exists()) {
            boolean delete = labeled.delete();
        }
        labeled.createNewFile();
        BufferedImage actor = fileService.loadImage(actorPath);
        BufferedImage arrow = fileService.loadImage(arrowPath);
        BufferedImage box = fileService.loadImage(boxPath);
        List<BufferedImage> imagesToReturn = new ArrayList<>();
        int actors;
        int arrows;
        int boxes;
        for (int i = imageCount; imageCount < i + quantity; imageCount++) {
            actors = new Random().nextInt(10);
            arrows = new Random().nextInt(10);
            boxes = new Random().nextInt(10);
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
            labeledService.registerImageForLabeled(imageCount + ".png", goodValidation(actors, arrows, boxes), labeled);
        }
        toReturn = new Pair<>(imagesToReturn, labeled);
        return toReturn;
    }

    public Pair<List<BufferedImage>, File> generatePredeterminedCases(Integer quantity, Integer type) throws IOException, ImageOutOfBoundsException, IndexOutOfBoundsException {

        if (type < 1 || type > 6) {
            throw new IndexOutOfBoundsException();
        }
        Pair<List<BufferedImage>, File> toReturn;
        File labeled = new File(textFilePath);
        if (labeled.exists()) {
            boolean delete = labeled.delete();
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
            labeledService.registerImageForLabeled(imageCount + ".png", true, labeled);
        }
        toReturn = new Pair<>(imagesToReturn, labeled);
        return toReturn;
    }

    private boolean goodValidation(Integer actors, Integer arrows, Integer boxes) {
        return actors > 0 && arrows > 0 && boxes > 0 && actors <= arrows && boxes <= arrows && arrows <= actors * boxes;
    }


    private Optional<BufferedImage> typeOfTransformation(int backgroundHeight, int backgroundWidth, BufferedImage img) throws ImageOutOfBoundsException {
//        for(int i = 0; i < 6; i++){
        BufferedImage destinationImage = null;
        int option = new Random().nextInt(3);
        switch (option) {
            case (0): {
                destinationImage = transformationService.rotation(img, new Random().nextInt(359));
                destinationImage = transformationService.drawImage(backgroundWidth, backgroundHeight,
                transformationService.makeColorTransparent(new Color(0, 0, 0), destinationImage));
                break;
            }
//            case (1): {
//                    TODO el demoni
//                    destinationImage = resize(img, destinationImage);
//                break;
//            }
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
//            case (4): {
//                //TODO
////                    destinationImage = moveAndResize(img, destinationImage);
//                break;
//            }
//            case (5): {
//                //TODO
////                    destinationImage = allTransformations(img, destinationImage);
//                break;
//            }
        }
        return Optional.of(destinationImage);
    }

    public int getImageCount() {
        return imageCount;
    }
}


//
//    BufferedImage backgroundImage;
//    TransparentTransformation transparent;
//    String pathName = "";
//
//    public void generateDataset() throws IOException {
//        System.out.println("Cargando Imagen ...");
//        final BufferedImage actor = ImageTransform.loadImage("src/main/resources/Actor.png");
//        int height = 0;
//        int width = 0;
//        int option;
//        BufferedImage destinationImage = null;
//
//        // Constructs a BufferedImage of one of the predefined image types.
//        BufferedImage bufferedImage = new BufferedImage(actor.getWidth(), actor.getHeight(), BufferedImage.TYPE_INT_RGB);
//
//        // Create a graphics which can be used to draw into the buffered image
//        Graphics2D g2d = bufferedImage.createGraphics();
//
//        // fill all the image with white
//        g2d.setColor(Color.white);
//        g2d.fillRect(0, 0, width, height);
//
//        backgroundImage = ImageTransform.loadImage("src/main/resources/background.png");
//        BufferedImage img;
////        for(int i = 0; i < ImagesToCreate; i++){
//
//
//
//
////        BufferedImage destinationImg = new BufferedImage(backgroundImage.getWidth(), backgroundImage.getHeight(), BufferedImage.TYPE_INT_ARGB); //suponiendo tamaño de imagen  y ARGB que soporta trasparencia
////
////        Graphics g = fusion.getGraphics();
////        g.drawImage(biUno, 0, 0, null); //se rellena con imagen uno
////        g.drawImage(biDos, 5, 5, null); //se rellena con imagen dos con  un supuesto margen de 5 (habria que ver cual es el real)
//
//
////        ImageIO.write(biResultado, "PNG", new File("resultado.png"));
//
//
//
//        for(int i = 0; i < 3; i++){
//            img = actor;
////            option = new Random().nextInt(6);
//            switch (i) {
//                case(0): {
//                    destinationImage = rotate(img, destinationImage);
//                    break;
//                }
////                case(1):{
////                    destinationImage = resize(img, destinationImage);
////                    break;
////                }
//                case(2):{
//                    destinationImage = move(img, destinationImage);
//                    break;
//                }
//                case(1):{
//                    destinationImage = moveAndRotate(img, destinationImage);
//                    break;
//                }
////                case(4):{
////                    destinationImage = moveAndResize(img, destinationImage);
////                    break;
////                }
////                case(5):{
////                    destinationImage = allTransformations(img, destinationImage);
////                    break;
////                }
//            }
//            // Save as PNG
//            System.out.println("Guardando Imagen: image" + i + ".png");
//            pathName = "src/main/resources/dataset/images" + i + ".png";
//            ImageTransform.saveImage(destinationImage, pathName);
//        }
//    }
//
//    private BufferedImage allTransformations(BufferedImage img, BufferedImage destinationImage) {
//        //resize
//        double randomResize = (new Random().nextInt(9999999) % 400);
//        randomResize = randomResize/100.0;
//        if (randomResize < 0.5) {
//            randomResize = 0.5;
//        }
//        img = ImageTransform.resize(img, randomResize);
//        //rotate
//        System.out.println("Rotando Imagen ...");
//        img = ImageTransform.rotacionImagen(img,  new Random().nextInt(359));
//        //move
//        int randomX = new Random().nextInt(9999999) % (backgroundImage.getWidth() - img.getWidth());
//        int randomY = new Random().nextInt(9999999) % (backgroundImage.getHeight() - img.getHeight());
//        transparent = new TransparentTransformation(img);
//        destinationImage = transparent.makeColorTransparent(new Color(0, 0, 0), backgroundImage.getWidth(), backgroundImage.getHeight(), randomX, randomY);
//        return destinationImage;
//    }
//
//    private BufferedImage move(BufferedImage img, BufferedImage destinationImage) {
//        int randomX = new Random().nextInt(9999999) % (backgroundImage.getWidth() - img.getWidth());
//        int randomY = new Random().nextInt(9999999) % (backgroundImage.getHeight() - img.getHeight());
//        transparent = new TransparentTransformation(img);
//        destinationImage = transparent.makeColorTransparent(new Color(0, 0, 0), backgroundImage.getWidth(), backgroundImage.getHeight(), randomX, randomY);
//        return destinationImage;
//    }
//
//    private BufferedImage moveAndRotate(BufferedImage img, BufferedImage destinationImage){
//        img = ImageTransform.rotacionImagen(img,  new Random().nextInt(359));
//        int randomX = new Random().nextInt(9999999) % (backgroundImage.getWidth() - img.getWidth());
//        int randomY = new Random().nextInt(9999999) % (backgroundImage.getHeight() - img.getHeight());
//        transparent = new TransparentTransformation(img);
//        destinationImage = transparent.makeColorTransparent(new Color(0, 0, 0), backgroundImage.getWidth(), backgroundImage.getHeight(), randomX, randomY);
//        return destinationImage;
//    }
//
//    private BufferedImage moveAndResize(BufferedImage img, BufferedImage destinationImage){
//        double randomResize = (new Random().nextInt(9999999) % 400);
//        randomResize = randomResize/100.0;
//        if (randomResize < 0.5) {
//            randomResize = 0.5;
//        }
//        img = ImageTransform.resize(img, randomResize);
//        int randomX = (int) (new Random().nextInt(9999999) % (backgroundImage.getWidth() - img.getWidth()));
//        int randomY = (int) (new Random().nextInt(9999999) % (backgroundImage.getHeight() - img.getHeight()));
//        transparent = new TransparentTransformation(img);
//        destinationImage = transparent.makeColorTransparent(new Color(0, 0, 0), backgroundImage.getWidth(), backgroundImage.getHeight(), randomX, randomY);
//        return destinationImage;
//    }
//
//    private BufferedImage rotate(BufferedImage img, BufferedImage destinationImage){
//        System.out.println("Rotando Imagen ...");
//        img = ImageTransform.rotacionImagen(img,  new Random().nextInt(359));
//        transparent = new TransparentTransformation(img);
//        destinationImage = transparent.makeColorTransparent(new Color(0, 0, 0),  backgroundImage.getWidth(), backgroundImage.getHeight(), 10, 10);
//        return destinationImage;
//    }
//
//    private BufferedImage resize(BufferedImage img, BufferedImage destinationImage){
//        double randomResize = (new Random().nextInt(9999999) % 400);
//        randomResize = randomResize/100.0;
//        if (randomResize < 0.5) {
//            randomResize = 0.5;
//        }
//        ImageTransform.resize(img, randomResize);
//        transparent = new TransparentTransformation(img);
//        destinationImage = transparent.makeColorTransparent(new Color(0, 0, 0),  backgroundImage.getWidth(), backgroundImage.getHeight(), 10, 10);
//        return destinationImage;
//    }
//
//}

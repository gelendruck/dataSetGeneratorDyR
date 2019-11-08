package com.tfg.tfgDaniRuben.service;

import com.tfg.tfgDaniRuben.Exceptions.ImageOutOfBoundsException;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.*;

@Component
public class TransformationService {

    public Image makeColorTransparent(final Color color, BufferedImage img) {
        ImageFilter filter = new RGBImageFilter() {
            // the color we are looking for... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFF000000;

            // filtro para aplicar transparencia a la imagen pasada en el método
            @Override
            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(img.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    public BufferedImage drawImage (int width, int height, Image img) {

        BufferedImage imgDestination = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dImg = imgDestination.createGraphics();
        g2dImg.drawImage(img, null, null);
        g2dImg.dispose();
        return imgDestination;
    }

    public BufferedImage traslation (BufferedImage origin, int backgroundWidth, int backgroundHeight, int positionX, int positionY) throws ImageOutOfBoundsException {

        if(validatePosition(backgroundWidth, backgroundHeight, positionX, positionY, origin.getWidth(), origin.getHeight())) {

            BufferedImage imageToReturn = new BufferedImage(backgroundWidth, backgroundHeight, BufferedImage.TYPE_INT_ARGB);
            BufferedImage originTrans = drawImage(origin.getWidth(), origin.getHeight(),
                    makeColorTransparent(new Color(0, 0, 0), origin));
            imageToReturn = drawImage(backgroundWidth, backgroundHeight,
                    makeColorTransparent(new Color(0, 0, 0), imageToReturn));

            Graphics2D g2dImg = imageToReturn.createGraphics();
            g2dImg.drawImage(originTrans, positionX, positionY, null);
            g2dImg.dispose();

            return imageToReturn;
        } else {
            throw new ImageOutOfBoundsException();
        }
    }


    public BufferedImage rotation(BufferedImage origin, double grados) {
        BufferedImage imgDestination;
        ImageTransform imTransform = new ImageTransform(origin.getHeight(), origin.getWidth());
        imTransform.rotate(grados);
        imTransform.findTranslation();
        AffineTransformOp ato = new AffineTransformOp(imTransform.getTransform(), AffineTransformOp.TYPE_BILINEAR);
        imgDestination = ato.createCompatibleDestImage(origin, origin.getColorModel());
        return ato.filter(origin, imgDestination);
    }

    //TODO hacer que esta mierda funcione
    public static BufferedImage resize(BufferedImage img, double randomResize) {

        int height = (int) (img.getHeight() * randomResize);
        int width = (int) (img.getWidth() * randomResize);

        // scales the input image to the output image
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(img, 10, 10, width, height, null);
        g2d.dispose();
        return img;
    }

    private boolean validatePosition(int backgroundX, int backgroundY, int positionX, int positionY, int width, int height){
        if (positionX + width > backgroundX) {
            return false;
        }
        if (positionY + height > backgroundY) {
            return false;
        }
        return true;
    }

}


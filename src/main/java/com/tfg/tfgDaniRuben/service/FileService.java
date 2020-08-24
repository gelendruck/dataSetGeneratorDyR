package com.tfg.tfgDaniRuben.service;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class FileService {

    public BufferedImage loadImage(String ruta) throws IOException {
        BufferedImage imagen = ImageIO.read(new File(ruta));

        BufferedImage source = new BufferedImage(imagen.getWidth(),
                imagen.getHeight(), BufferedImage.TYPE_INT_ARGB);
        source.getGraphics().drawImage(imagen, 0, 0, null);
        return source;
    }

    public byte[] zipArrayFiles(List<File> filesToZip) {
        try {
            //create byte buffer
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
            ZipOutputStream zout = new ZipOutputStream(bufferedOutputStream);
            for (int i = 0; i < filesToZip.size(); i++) {
                System.out.println("agregando : " + filesToZip.get(i).getName());
                FileInputStream fin = new FileInputStream(filesToZip.get(i));
                zout.putNextEntry(new ZipEntry(filesToZip.get(i).getName()));

                IOUtils.copy(fin, zout);

                fin.close();
                zout.closeEntry();
            }
            if (zout != null) {
                zout.finish();
                zout.flush();
                IOUtils.closeQuietly(zout);
            }
            System.out.println("El archivo comprimido ha sido creado!");
            IOUtils.closeQuietly(bufferedOutputStream);
            IOUtils.closeQuietly(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException ioe) {
            System.out.println("IOException :" + ioe);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


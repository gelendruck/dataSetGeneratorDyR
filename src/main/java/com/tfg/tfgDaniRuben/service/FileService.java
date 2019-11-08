package com.tfg.tfgDaniRuben.service;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class FileService {

    @Autowired
    private LabeledService labeledService;

//    private List<BufferedImage> processUniverseZipFile(MultipartFile file, String defaultVersion, boolean force, boolean validateFirst)
//            throws ArchiveException {
//        try {
//            final InputStream is = file.getInputStream();
//            try (final ZipArchiveInputStream debInputStream = (ZipArchiveInputStream) new ArchiveStreamFactory()
//                    .createArchiveInputStream("zip", is);) {
//                ZipArchiveEntry entry;
////                List<UniverseServiceModel> usmList = new LinkedList<>();
//                while ((entry = (ZipArchiveEntry) debInputStream.getNextEntry()) != null) {
//                    if (!entry.isDirectory()) {
//                        while (entry != null
//                                && entry.getName() != null && entry.getName().toLowerCase().endsWith(".json")) {
//                            processUniverseJsonFile(result, debInputStream, entry, defaultVersion, force)
//                                    .ifPresent(usmList::add);
//                            entry = debInputStream.getNextZipEntry();
//                        }
//                    }
//                }
//                if (!validateFirst || result.getError().isEmpty() )  {
//                    usmList.stream().forEach(m -> API.Try(() -> createService(m)).getOrNull());
//                } else {
//                    result.setAdded(Collections.emptyList());
//                }
//                return result;
//            }
//        } catch (IOException e) {
//            throw new ArchiveException("Unable to process zip file", e);
//        }
//    }
//        return null;
//    }

    public BufferedImage loadImage(String ruta) throws IOException {
        BufferedImage imagen = ImageIO.read(new File(ruta));

        BufferedImage source = new BufferedImage(imagen.getWidth(),
                imagen.getHeight(), BufferedImage.TYPE_INT_ARGB);
        source.getGraphics().drawImage(imagen, 0, 0, null);
        return source;
    }

//    public void saveImage(BufferedImage im, String pathName, boolean good) throws IOException {
//        String[] strings = pathName.split("/");
//        String name = strings[strings.length];
//        labeledService.registerImageForLabeled(name, good);
//        ImageIO.write(im, "png", new File(pathName));
//    }

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


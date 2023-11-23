package dev.sassine.simpleopenia.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class OCRService {

    @Value("${tessdata.prefix}")
    private String tessData;

    @Value("${tessdata.lang}")
    private String tessLang;

    public String performOCR(String filePath) throws TesseractException {
        File imageFile = new File(filePath);
        ITesseract instance = new Tesseract();
        instance.setDatapath(tessData);
        instance.setLanguage(tessLang);

        return instance.doOCR(imageFile);
    }
}
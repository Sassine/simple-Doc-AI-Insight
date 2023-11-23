package dev.sassine.simpleopenia;

import dev.sassine.simpleopenia.service.OCRService;
import dev.sassine.simpleopenia.service.OpenIAService;
import dev.sassine.simpleopenia.service.PDFService;
import jakarta.annotation.PostConstruct;

import net.sourceforge.tess4j.TesseractException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SimpleOpenIAApplication {


	@Autowired
	private PDFService pdfService;

	@Autowired
	private OCRService ocrService;

	@Autowired
	private OpenIAService openIAService;

	public static void main(String[] args) {
		SpringApplication.run(SimpleOpenIAApplication.class, args);
	}

	@PostConstruct
	public void init() {
		try {
			var pdfContent = pdfService.extractText("model.pdf");
			openIAService.gerarResposta(pdfContent,250);
			var ocrContent = ocrService.performOCR("model.png");
			openIAService.gerarResposta(ocrContent,250);
		} catch (RuntimeException | IOException | TesseractException e) {
			System.err.printf("Erro ao processar arquivos %s%n", e.getMessage());
		}
	}

}

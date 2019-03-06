package ru.bacca.bikerun.controller;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class PdfParserController {

    private static final String PDF_PATH = "D:\\response.pdf";
    private static final String DOCX_PATH = "D:\\test.docx";


    private static final Logger LOGGER = LogManager.getLogger(PdfParserController.class);

    @PostMapping("/upload")
    public ResponseEntity<HttpStatus> convertPdfToWord(@RequestParam("file") MultipartFile file){
        LOGGER.info("Start converting pdf to word");

        File docxFile = new File(DOCX_PATH);
        File pdfFile = new File(PDF_PATH);
        try (FileOutputStream fileOutputStream = new FileOutputStream(pdfFile)) {
            fileOutputStream.write(file.getBytes());

            IConverter converter = LocalConverter.make();
            converter.convert(pdfFile).as(DocumentType.PDF)
                    .to(docxFile).as(DocumentType.DOCX)
                    .execute();
        } catch (IOException e) {
            LOGGER.error("Convertation failed - %m", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOGGER.info("Converting pdf to word successful");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

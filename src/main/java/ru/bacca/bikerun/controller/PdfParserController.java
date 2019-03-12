package ru.bacca.bikerun.controller;

import com.itextpdf.text.DocumentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jodconverter.OfficeDocumentConverter;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.jodconverter.document.DocumentFormatRegistry;
import org.jodconverter.document.SimpleDocumentFormatRegistry;
import org.jodconverter.office.DefaultOfficeManagerBuilder;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class PdfParserController {

    private static final Logger LOGGER = LogManager.getLogger(PdfParserController.class);

    @PostMapping("/upload")
    public ResponseEntity<HttpStatus> convertPdfToWord(@RequestParam("file") MultipartFile file, ServletResponse servletResponse) throws IOException, DocumentException, DocumentException, OfficeException {
        LOGGER.info("Start converting pdf to word");

        // FIXME: 12.03.2019 
        DefaultOfficeManagerBuilder configuration = new DefaultOfficeManagerBuilder();
        configuration.setOfficeHome(new File("D:/Program Files/LibreOffice"));

        OfficeManager officeManager = configuration.build();
        officeManager.start();
        DocumentFormatRegistry formatRegistry = new SimpleDocumentFormatRegistry();
        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager, formatRegistry);

        try {
            converter.convert(inputFile, outputFile);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            officeManager.stop();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public String readTextFromWord() {
        /*try (FileInputStream fileInputStream = new FileInputStream(DOCX_PATH)) {
            XWPFDocument docxFile = new XWPFDocument(fileInputStream);

//            LOGGER.info(docxFile.getTables().get(0).getRow(1).getCell(1).getText());
            docxFile.getParagraphs().forEach(xwpfParagraph -> LOGGER.info(xwpfParagraph.getText()));
        } catch (IOException e) {
            e.printStackTrace();
            return "BAD";
        }*/
        return "OK";
    }
}

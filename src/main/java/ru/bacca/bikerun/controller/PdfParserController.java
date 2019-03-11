package ru.bacca.bikerun.controller;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Optional;
import java.util.concurrent.Executor;

@RestController
@RequestMapping("/file")
public class PdfParserController {

    private static final Logger LOGGER = LogManager.getLogger(PdfParserController.class);

    @PostMapping("/upload")
    public ResponseEntity<HttpStatus> convertPdfToWord(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        LOGGER.info("Start converting pdf to word");

        IConverter converter = LocalConverter.make();
        converter.convert(servletRequest.getInputStream()).as(DocumentType.PDF)
                .to(servletResponse.getOutputStream()).as(DocumentType.DOCX)
                .execute();
        servletResponse.getOutputStream().close();
        LOGGER.info("Converting pdf to word successful");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*public String readTextFromWord() {
        try (FileInputStream fileInputStream = new FileInputStream(DOCX_PATH)) {
            XWPFDocument docxFile = new XWPFDocument(fileInputStream);

//            LOGGER.info(docxFile.getTables().get(0).getRow(1).getCell(1).getText());
            docxFile.getParagraphs().forEach(xwpfParagraph -> LOGGER.info(xwpfParagraph.getText()));
        } catch (IOException e) {
            e.printStackTrace();
            return "BAD";
        }
        return "OK";
    }*/
}

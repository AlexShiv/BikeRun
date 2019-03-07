package ru.bacca.bikerun.controller;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.Optional;
import java.util.concurrent.Executor;

@RestController
@RequestMapping("/file")
public class PdfParserController {

    private static final Logger LOGGER = LogManager.getLogger(PdfParserController.class);
    private final Executor executor;

    @Autowired
    public PdfParserController(@Qualifier("threadPoolTaskExecutor") Executor executor) {
        this.executor = executor;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> convertPdfToWord(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        LOGGER.info("Start converting pdf to word");

        IConverter converter = LocalConverter.make();
        converter.convert(servletRequest.getInputStream()).as(DocumentType.PDF)
                .to(servletResponse.getOutputStream()).as(DocumentType.DOCX)
                .execute();
        LOGGER.info("Converting pdf to word is over");

        Optional<String> result = readTextFromWord(servletResponse.getOutputStream());

        return result.map(s -> new ResponseEntity<>(s, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    private Optional<String> readTextFromWord(OutputStream outputStream) {
        try (PipedInputStream in = new PipedInputStream()) {
            try (PipedOutputStream out = new PipedOutputStream(in)) {
                executor.execute(() -> {
                    try {

                    } catch (Exception e) {
                        Thread t = Thread.currentThread();
                        t.getUncaughtExceptionHandler().uncaughtException(t, e);
                    }
                });
            }

            XWPFDocument docxFile = new XWPFDocument(in);

            String text = docxFile.getTables().get(0)
                    .getRow(1)
                    .getCell(1).getText();
            return Optional.ofNullable(text);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}

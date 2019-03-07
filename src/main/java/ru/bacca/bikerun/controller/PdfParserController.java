package ru.bacca.bikerun.controller;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
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
    public ResponseEntity<String> convertPdfToWord(ServletRequest servletRequest) throws IOException, InterruptedException {
        LOGGER.info("Start converting pdf to word");

        Optional<String> result;
        try (PipedInputStream in = new PipedInputStream()) {
            try (PipedOutputStream out = new PipedOutputStream(in)) {
                executor.execute(() -> {
                    try {
                        IConverter converter = LocalConverter.make();
                        converter.convert(servletRequest.getInputStream()).as(DocumentType.PDF)
                                .to(out).as(DocumentType.DOCX)
                                .execute();
                        LOGGER.error("Converting...");
                    } catch (IOException e) {
                        Thread t = Thread.currentThread();
                        t.getUncaughtExceptionHandler().uncaughtException(t, e);
                        LOGGER.error("THREAD ERROR");
                    }
                });
                LOGGER.info("Converting pdf to word is over");
                String p = "";
                LOGGER.info("Reading document");
                XWPFDocument docxFile = new XWPFDocument(in);
                LOGGER.info("Reading document is over");
                for (XWPFParagraph paragraph : docxFile.getParagraphs()) {
                    p += paragraph.getText();
                }
                LOGGER.info(p);
                result = Optional.of(p);

            }
        }
        return result.map(s -> new ResponseEntity<>(s, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }
}

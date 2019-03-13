package ru.bacca.bikerun.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletResponse;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class PdfParserController {

    private static final Logger LOGGER = LogManager.getLogger(PdfParserController.class);

    @PostMapping("/upload")
    public ResponseEntity<HttpStatus> convertPdfToWord(@RequestParam("file") MultipartFile file, ServletResponse servletResponse) throws IOException {
        LOGGER.info("Start converting pdf to word");

        PDDocument document = PDDocument.load(file.getInputStream());
        PDPage page = document.getDocumentCatalog().getPages().get(0);
        PdfBoxFinder boxFinder = new PdfBoxFinder(page);
        boxFinder.processPage(page);

        PDFTextStripperByArea stripperByArea = new PDFTextStripperByArea();
        for (Map.Entry<String, Rectangle2D> entry : boxFinder.getRegions().entrySet()) {
            stripperByArea.addRegion(entry.getKey(), entry.getValue());
        }

        stripperByArea.extractRegions(page);
        List<String> names = stripperByArea.getRegions();
        names.sort(null);
        Map<String, String> table = new HashMap<>();
        for (String name : names) {
//            table.put(name, stripperByArea.getTextForRegion(name));
            LOGGER.info(name + "\t" + stripperByArea.getTextForRegion(name));
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

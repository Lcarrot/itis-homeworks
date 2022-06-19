package ru.itis.tyshenko.simpleconsumer.generator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.tyshenko.simpleconsumer.dto.UserGenerateForm;

@Component
public class SimplePdfUserGenerator {

  @Autowired
  private HtmlGenerator htmlGenerator;

  private final String EXTENSION = ".pdf";

  public File generatePdf(UserGenerateForm pdfUser, String fileName)
      throws IOException, DocumentException {
    String newFileName = fileName + EXTENSION;
    Document document = new Document();
    // Создаем writer для записи в pdf
    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(newFileName));
    // Открываем для чтения html страничку
    document.open();
    // Парсим её и записываем в PDF
    try (var inputStream = new ByteArrayInputStream(htmlGenerator.generateConfirmEmail(pdfUser).getBytes())) {
      XMLWorkerHelper.getInstance().parseXHtml(writer, document, inputStream);
    }
    document.close();
    return new File(newFileName);
  }
}

package ru.itis.tyshenko.harderconsumer.generator;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.tyshenko.harderconsumer.dto.UserGenerateForm;

@Component
public class HtmlGenerator {

  @Autowired
  freemarker.template.Configuration configuration;

  public String generateConfirmEmail(UserGenerateForm user) {
    Template template;
    try {
      template = configuration.getTemplate("pdf_pattern.ftlh");
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    Map<String, String> attributes = new HashMap<>();
    attributes.put("firstName", user.getFirstName());
    attributes.put("lastName", user.getLastName());

    StringWriter writer = new StringWriter();
    try {
      template.process(attributes, writer);
    } catch (TemplateException | IOException e) {
      throw new IllegalStateException(e);
    }
    return writer.toString();
  }
}

package de.adesso.kicker.email;

import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Component
@RequiredArgsConstructor
class EmailMessageBuilder {

    private final TemplateEngine templateEngine;

    String build(Map<String, Object> attributes, String template) {
        var context = new Context();
        context.setLocale(LocaleContextHolder.getLocale());
        context.setVariables(attributes);
        return templateEngine.process(template, context);
    }
}
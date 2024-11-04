package com.chis.trugarden.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    /**
     * Sends an email to activate user account
     * @param destinationEmail
     * @param username
     * @param confirmationUrl
     * @param activationCode
     */
    @Async
    public void sendActivateAccountEmail(
            String destinationEmail,
            String username,
            String confirmationUrl,
            String activationCode
    ) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED,
                    StandardCharsets.UTF_8.name()
            );
            messageHelper.setFrom("cristru8@gmail.com");
            final String templateName = EmailTemplates.ACTIVATE_ACCOUNT.getTemplate();

            Map<String, Object> variables = new HashMap<>();
            variables.put("username", username);
            variables.put("confirmation_url", confirmationUrl);
            variables.put("activation_code", activationCode);

            Context context = new Context();
            context.setVariables(variables);

            messageHelper.setSubject(EmailTemplates.ACTIVATE_ACCOUNT.getSubject());

            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);
            javaMailSender.send(mimeMessage);
            log.info(String.format(
                    "INFO - Email successfully sent to %s with template %s",
                    destinationEmail,
                    templateName)
            );
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send email to {}", destinationEmail);
        }
    }
}

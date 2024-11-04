package com.chis.trugarden.email;

import lombok.Getter;

/**
 * Each template contains the file path of the HTML email template and a subject for the email.
 */
@Getter
public enum EmailTemplates {

    ACTIVATE_ACCOUNT("activate_account.html", "Please activate your account");

    private final String template;
    private final String subject;

    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}

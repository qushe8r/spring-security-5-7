package me.qushe8r.studyspringsecurity5_7.security.common;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class FormAuthenticationDetails extends WebAuthenticationDetails {

    private final String secretKey;

    public FormAuthenticationDetails(HttpServletRequest request) {
        super(request);
        secretKey = request.getParameter("secret_key");
    }

    public String getSecretKey() {
        return secretKey;
    }
}

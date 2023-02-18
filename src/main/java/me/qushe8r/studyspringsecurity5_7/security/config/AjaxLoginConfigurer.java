package me.qushe8r.studyspringsecurity5_7.security.config;

import me.qushe8r.studyspringsecurity5_7.security.filter.AjaxLoginProcessingFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class AjaxLoginConfigurer<T extends HttpSecurityBuilder<T>> extends AbstractAuthenticationFilterConfigurer<T, AjaxLoginConfigurer<T>, AjaxLoginProcessingFilter> {
    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;
    private AuthenticationManager authenticationManager;

    public AjaxLoginConfigurer( ) {
        super(new AjaxLoginProcessingFilter(), null);
    }

    @Override
    public void init(T http) throws Exception {
        super.init(http);
    }

    @Override
    public void configure(T http) throws Exception {
        if (authenticationManager == null) {
            authenticationManager = http.getSharedObject(AuthenticationManager.class);
        }
        getAuthenticationFilter().setAuthenticationManager(authenticationManager);
        getAuthenticationFilter().setAuthenticationSuccessHandler(successHandler);
        getAuthenticationFilter().setAuthenticationFailureHandler(failureHandler);

        SessionAuthenticationStrategy sessionAuthenticationStrategy =
                http.getSharedObject(SessionAuthenticationStrategy.class);
        if (sessionAuthenticationStrategy != null) {
            getAuthenticationFilter().setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
        }

        RememberMeServices rememberMeServices =
                http.getSharedObject(RememberMeServices.class);
        if (rememberMeServices != null) {
            getAuthenticationFilter().setRememberMeServices(rememberMeServices);
        }

        http.setSharedObject(AjaxLoginProcessingFilter.class, getAuthenticationFilter());
        http.addFilterBefore(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    public AjaxLoginConfigurer<T> successHandlerAjax(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
        return this;
    }

    public AjaxLoginConfigurer<T> failureHandlerAjax(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
        return this;
    }

    public AjaxLoginConfigurer<T> setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        return this;
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    public static AjaxLoginConfigurer<HttpSecurity> AjaxDSL() {
        return new AjaxLoginConfigurer<>();
    }
}

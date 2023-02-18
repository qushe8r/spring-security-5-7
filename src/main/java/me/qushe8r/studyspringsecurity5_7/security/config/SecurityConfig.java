package me.qushe8r.studyspringsecurity5_7.security.config;

import lombok.RequiredArgsConstructor;
import me.qushe8r.studyspringsecurity5_7.security.common.AjaxLoginAuthenticationEntryPoint;
import me.qushe8r.studyspringsecurity5_7.security.filter.AjaxLoginProcessingFilter;
import me.qushe8r.studyspringsecurity5_7.security.handler.AjaxAccessDeniedHandler;
import me.qushe8r.studyspringsecurity5_7.security.handler.AjaxAuthenticationFailureHandler;
import me.qushe8r.studyspringsecurity5_7.security.handler.AjaxAuthenticationSuccessHandler;
import me.qushe8r.studyspringsecurity5_7.security.provider.AjaxAuthenticationProvider;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;

import static me.qushe8r.studyspringsecurity5_7.security.config.AjaxLoginConfigurer.AjaxDSL;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final AccessDeniedHandler accessDeniedHandler;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean       // 사용하면 [warn] You are asking Spring Security to ignore
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    @Order(0)
    public SecurityFilterChain ajaxFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));

        http
                .antMatcher("/api/**")
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/api/messages").hasRole("MANAGER")
                        .anyRequest().authenticated()
                );
        http
                .exceptionHandling()
                .authenticationEntryPoint(new AjaxLoginAuthenticationEntryPoint())
                .accessDeniedHandler(new AjaxAccessDeniedHandler());
        http
                .csrf().disable();
        http
                .apply(AjaxDSL())
                .successHandlerAjax(ajaxAuthenticationSuccessHandler())
                .failureHandlerAjax(ajaxAuthenticationFailureHandler())
                .setAuthenticationManager(authenticationManager)
                .loginProcessingUrl("/api/login");

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorization -> authorization
                        .antMatchers("/", "/users", "/login/**", "/login/*").permitAll()
                        .antMatchers("/mypage").hasRole("USER")
                        .antMatchers("/messages").hasRole("MANAGER")
                        .antMatchers("/config").hasRole("ADMIN")
                        .anyRequest().authenticated()
                );
        http
                .formLogin()
                .loginPage("/login")                // login 페이지 url
                .loginProcessingUrl("/login_proc")  // client가 post로 로그인 요청하는 url
                .authenticationDetailsSource(authenticationDetailsSource)
                .defaultSuccessUrl("/")             // login 성공시 redirect url
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll();                       // 로그인 페이지에 대한 권한 설정
        http
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
        http
                .csrf().disable();

        return http.build();
    }

    @Bean
    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter(AuthenticationManager authenticationManager) {
        AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManager);
        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler());
        ajaxLoginProcessingFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler());
        return ajaxLoginProcessingFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        ProviderManager authenticationManager = (ProviderManager) authenticationConfiguration.getAuthenticationManager();
        authenticationManager.getProviders().add(ajaxAuthenticationProvider());
        return authenticationManager;
    }

    @Bean
    public AjaxAuthenticationProvider ajaxAuthenticationProvider() {
        return new AjaxAuthenticationProvider(userDetailsService, passwordEncoder());
    }

    @Bean
    public AuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
        return new AjaxAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
        return new AjaxAuthenticationFailureHandler();
    }
}

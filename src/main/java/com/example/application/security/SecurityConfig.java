package com.example.application.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthProvider authProvider;

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    public static final String REGISTER_URL = "/register";
    private static final String LOGOUT_SUCCESS_URL = "/login";
    public static final String LOGIN_SUCCESS_URL = "/";
    public static final String ADMIN_URL = "/admin/**";
    public static final String LEVEL_URL = "/level";
    public static final String FIGURE_URL = "/figure";
    public static final String GRID_URL = "/grid";
    public static final String RATING_URL = "/rating";



    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity.formLogin().disable();
        // Vaadin handles CSRF internally
        httpSecurity.csrf().disable()

                // Register our CustomRequestCache, which saves unauthorized access attempts, so the user is redirected after login.
                .requestCache().requestCache(new CustomRequestCache())

                // Restrict access to our application.
                .and().authorizeRequests()

                // Allow all Vaadin internal requests.

                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                .antMatchers(REGISTER_URL, LOGIN_URL).permitAll()
                .antMatchers(ADMIN_URL).access("hasRole('ROLE_ADMIN')")

                // Allow all requests by logged-in users.
                .anyRequest().authenticated()

                // Configure the login page.
                .and().formLogin()
                .loginPage(LOGIN_URL).permitAll()
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                .failureUrl(LOGIN_FAILURE_URL)
                .defaultSuccessUrl(LOGIN_SUCCESS_URL)

                // Configure logout
                .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                // Client-side JS
                "/VAADIN/**",

                // the standard favicon URI
                "/favicon.ico",

                // the robots exclusion standard
                "/robots.txt",

                // web application manifest
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",

                // icons and images
                "/icons/**",
                "/images/**",
                "/styles/**",

                // (development mode) H2 debugging console
                "/h2-console/**");
    }
}

package com.example.spot.global.config;

import com.example.spot.global.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService userDetailsService;

    private final CustomAuthSuccessHandler authSuccessHandler;
    private final CustomAuthFailureHandler authFailureHandler;

    private final HttpLogoutSuccessHandler logoutSuccessHandler;

    private final DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter =
                new CustomAuthenticationFilter();

        filter.setFilterProcessesUrl("/login");
        filter.setAuthenticationManager(authenticationManager());
        filter.setUsernameParameter("email");
        filter.setPasswordParameter("password");
        filter.setRememberMeServices(rememberMeServices());
        filter.setAuthenticationSuccessHandler(authSuccessHandler);
        filter.setAuthenticationFailureHandler(authFailureHandler);

        return filter;
    }

    @Bean
    public AuthenticationProvider customAuthenticatonProvider() {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder());
    }

    @Bean
    public RememberMeAuthenticationFilter rememberMeAuthenticationFilter() throws Exception {
        return new RememberMeAuthenticationFilter(authenticationManager(), rememberMeServices());
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices tokenBasedRememberMeServices = new PersistentTokenBasedRememberMeServices("remember-me", userDetailsService, tokenRepository());

        tokenBasedRememberMeServices.setTokenValiditySeconds(60 * 60 * 24 * 7);
        tokenBasedRememberMeServices.setCookieName("rememberMe");
        tokenBasedRememberMeServices.setParameter("rememberMe");

        return tokenBasedRememberMeServices;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/h2-console/**",
                        "/favicon.ico"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .headers()
                .frameOptions()
                .sameOrigin();


        http.authorizeRequests()
                .mvcMatchers("/","/users", "/users/*", "/users/*/*", "/login").permitAll()
                //TODO:: RequestMapping 하위 중 HttpMethod.GET or POST 등으로 나눠서 허용 할 것 깔끔하게 정리하기

                .anyRequest().authenticated(); // 나머지 요청 인증 받아야함


        http.formLogin()
                .disable();

        http.addFilterBefore(new RememberMeCookieFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(rememberMeAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


        http.logout()
                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessHandler(logoutSuccessHandler)
                .invalidateHttpSession(true);

        http.rememberMe()
                .key("remember-me")
                .rememberMeServices(rememberMeServices());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticatonProvider());
    }

}

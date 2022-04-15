package br.com.mercadolivre.dnaanalyzer.security;

import br.com.mercadolivre.dnaanalyzer.security.filter.JwtAuthenticationFilter;
import br.com.mercadolivre.dnaanalyzer.security.handler.CustomAuthenticationEntryPoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(super.userDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Configuration
    @Order(1)
    public static class BasicSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

        @Override
        public void configure(final WebSecurity web) {
            web.ignoring().antMatchers(
                "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/actuator/health");
        }

        protected void configure(final HttpSecurity http) throws Exception {

            http
                .csrf()
                .disable()
                .requestMatchers()
                .antMatchers("/actuator/**")
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(this.customAuthenticationEntryPoint);
        }
    }

    @Configuration
    @Order(2)
    public static class ApiTokenSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

        @Value("${jwt.secretKey}")
        private String secretKey;

        @Override
        protected void configure(final HttpSecurity http) throws Exception {

            http
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(this.customAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/**")
                .authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), secretKey));
        }
    }

}

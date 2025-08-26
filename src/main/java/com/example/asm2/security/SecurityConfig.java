package com.example.asm2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http ) throws Exception{
        http.authorizeHttpRequests(configurer ->
                        configurer
                                .anyRequest().permitAll()
                )
                .formLogin(form ->
                        form
                                .loginPage("/user/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .defaultSuccessUrl("/home",true)
                                .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll());
        http.csrf(csrf -> csrf.disable());
        return http.build();


    }
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("select email,password,enable from user where email=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select u.email, r.name from user u join role r "
                               +"on u.role_id= r.id where u.email=?");
        return jdbcUserDetailsManager;
    }
}

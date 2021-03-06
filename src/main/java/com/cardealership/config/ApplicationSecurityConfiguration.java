package com.cardealership.config;

import com.cardealership.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public ApplicationSecurityConfiguration(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userService).passwordEncoder(this.bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/", "/users/login", "/users/register", "/css/**", "/js/**").permitAll()
                    .antMatchers("/customers/create", "/cars/create", "/suppliers/create", "/sales/create", "/parts/create", "/logs/all",
                            "customers/edit/**", "/suppliers/edit/**", "/suppliers/delete/**", "/parts/edit/**", "/parts/delete/**").hasAnyAuthority("ADMIN")
                    .antMatchers("/users/login", "/users/register").anonymous()
                    .antMatchers("/**").authenticated()
                .and()
                    .formLogin()
                    .loginPage("/users/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/")
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout"))
                    .permitAll()
                .and()
                    .rememberMe()
                    .rememberMeParameter("remember-me-new")
                    .key("48e07001-3043-4ae8-85b9-6d5b4767975b")
                    .userDetailsService(this.userService)
                    .rememberMeCookieName("SPRING_SECURITY_REMEMBER_ME_COOKIE")
                    .tokenValiditySeconds(2400);

    }
}

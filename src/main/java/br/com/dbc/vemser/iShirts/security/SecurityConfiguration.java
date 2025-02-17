package br.com.dbc.vemser.iShirts.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class  SecurityConfiguration {
    private final TokenService tokenService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable().and()
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests((authz) -> authz
                        .antMatchers("/","/auth/criar-cliente", "/auth/login").permitAll()
                        .antMatchers(HttpMethod.POST, "/produto/**").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .antMatchers(HttpMethod.DELETE, "/cargo/**").hasRole("ADMIN")
                        .antMatchers("/cargo/cadastro", "/cargo/{idCargo}", "/cargo").hasRole("ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/pedido/**").hasAnyRole("ADMIN", "CLIENTE", "FUNCIONARIO")
                        .antMatchers(HttpMethod.POST, "/pedido/**").hasAnyRole("ADMIN", "CLIENTE", "FUNCIONARIO")
                        .antMatchers(HttpMethod.PUT, "/pedido/**").hasAnyRole("ADMIN", "CLIENTE", "FUNCIONARIO")
                        .antMatchers(HttpMethod.POST, "/endereco/**").hasAnyRole("ADMIN", "CLIENTE", "FUNCIONARIO")
                        .antMatchers(HttpMethod.PUT, "/endereco/**").hasAnyRole("ADMIN", "CLIENTE", "FUNCIONARIO")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                        .accessDeniedHandler((request, response, accessDeniedException) -> response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
                );
        http.addFilterBefore(new TokenAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .exposedHeaders("Authorization");
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
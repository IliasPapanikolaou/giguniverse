package com.unipi.giguniverse.config;

import com.unipi.giguniverse.security.ApplicationUserRole;
import com.unipi.giguniverse.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.unipi.giguniverse.security.ApplicationUserPermission.VENUE_WRITE;
import static com.unipi.giguniverse.security.ApplicationUserRole.ATTENDANT;

@EnableWebSecurity
//@Configuration
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .cors() //TODO: Check CORS security policy
                .and()
                .csrf().disable() //protection for session connects, safe to disable with REST
                .authorizeRequests()
                .antMatchers("/","index","/css/*","/js/*").permitAll()
                .antMatchers("/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**").permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/ticket/validate/**").permitAll()
                .antMatchers("/api/concert/**").permitAll() //TODO: Allow all CRUD for now
                .antMatchers("/api/reservation/**").permitAll() //TODO: Allow all CRUD for now
//                .antMatchers("/api/venue/**").hasRole(ApplicationUserRole.OWNER.name())
                .antMatchers(HttpMethod.POST ,"/api/venue/**").hasAuthority(VENUE_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT ,"/api/venue/**").hasAuthority(VENUE_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE ,"/api/venue/**").hasAuthority(VENUE_WRITE.getPermission())
                .antMatchers(HttpMethod.GET, "/api/venue/**").hasAnyRole(ApplicationUserRole.OWNER.name(), ATTENDANT.name())
                .anyRequest()
                .authenticated();

        //Add Jwt filter
        httpSecurity
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    //Authentication manager
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

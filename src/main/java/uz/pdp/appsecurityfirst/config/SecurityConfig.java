package uz.pdp.appsecurityfirst.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .inMemoryAuthentication()
//                .withUser("director").password(passwordEncoder().encode("director")).roles("DIRECTOR")
//                .and()
//                .withUser("manager").password(passwordEncoder().encode("manager")).roles("MANAGER")
//                .and()
//                .withUser("user").password(passwordEncoder().encode("user")).roles("USER");

                //permission based users director1  can do everything, director2 everything except deleting

                .withUser("director1").password(passwordEncoder().encode("director1")).roles("DIRECTOR").authorities("READ_ALL_PRODUCT", "ADD_PRODUCT", "EDIT_PRODUCT", "DELETE_PRODUCT","READ_ONE_PRODUCT")
                .and()
                .withUser("director2").password(passwordEncoder().encode("director2")).roles("DIRECTOR").authorities("READ_ALL_PRODUCT", "ADD_PRODUCT", "EDIT_PRODUCT", "READ_ONE_PRODUCT");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    // role based authorization
                    // order is very important, least authorized comes first
//                    .antMatchers(HttpMethod.GET, "/api/product/**").hasAnyRole("USER", "MANAGER", "DIRECTOR")
//                    .antMatchers(HttpMethod.GET, "/api/product").hasAnyRole("DIRECTOR", "MANAGER")
//                    .antMatchers("/api/product/**").hasRole("DIRECTOR")

                    // permission based authorization
                    .antMatchers(HttpMethod.DELETE, "/api/product/*").hasAuthority("DELETE_PRODUCT")
                    .antMatchers("/api/product/**").hasAnyAuthority("READ_ALL_PRODUCT", "ADD_PRODUCT", "EDIT_PRODUCT", "READ_ONE_PRODUCT")

                    .anyRequest()
                    .authenticated()
                    .and()
                    .httpBasic();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

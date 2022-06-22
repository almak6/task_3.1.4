package ru.kata.spring.boot_security.demo.configs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final SuccessUserHandler successUserHandler;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl, SuccessUserHandler successUserHandler) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.successUserHandler = successUserHandler;
    }
    //настройка авторизации (прав доступа) пользователя
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()//предоставление разрешений/доступа URL
                .antMatchers("/", "/index").permitAll()//перечисление шаблонов URL.с настройкой доступа к нему, кому конкретно// разрешен
                .antMatchers("/user").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                //.anyRequest().authenticated()//остальные запросы разрешены для всех аутентифицированных пользователей
                .and()//окончание настройки разрешений, переход к другой настройке
                .formLogin()//генерация страницы логина, так как по умолчанию больше нет
                //так же сюда прикрутили надстройку для юзера после успешной аутентификации - внедрив экземпл Handler-а
                .permitAll()//настройка доступа к странице логина
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");//генерация страницы logout
                //.permitAll();
    }

//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsServiceImpl);
//    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(getPasswordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        return authenticationProvider;
    }

    // аутентификация inMemory - хранение аутентифицированных юзеров в памяти без БД
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("user")
//                        .roles("USER")
//                        .build();
//        UserDetails admin =
//                User.withDefaultPasswordEncoder()
//                        .username("admin")
//                        .password("user")
//                        .roles("ADMIN", "USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
        @Bean
        public PasswordEncoder getPasswordEncoder() {
            return NoOpPasswordEncoder.getInstance();
        }


}
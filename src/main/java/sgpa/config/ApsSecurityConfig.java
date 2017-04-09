package sgpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class ApsSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/css/**", "/js/**", "/fonts/**").permitAll()
				.antMatchers("/usuario/entrar", "/usuario/primeiroacesso", "/usuario/definirsenha",
						"/usuario/recuperarsenha", "/usuario/alterarsenha", "/site/**")
				.permitAll()
				.antMatchers("/usuario", "/usuario/", "/usuario/cadastrar", "/usuario/cadastrar/", "/usuario/**/editar",
						"/usuario/**/editar/", "/noticia", "/noticia/", "/noticia/cadastrar", "/noticia/cadastrar/",
						"/noticia/**/editar", "/noticia/**/editar/")
				.hasRole("ADMIN").anyRequest().authenticated().and().formLogin().loginPage("/usuario/login")
				.loginProcessingUrl("/doLogin").permitAll().and().logout().logoutUrl("/doLogout").permitAll().and()
				.csrf().disable();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

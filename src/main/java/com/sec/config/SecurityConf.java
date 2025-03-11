package com.sec.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConf {

	private final PasswordEncoder passwordEncoder;

    public SecurityConf(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
	public UserDetailsService configureAuth() {
		UserDetails user = User.builder()
						.username("user")
						.password(passwordEncoder.encode("pass"))
						.roles("USER")
						.build();

		UserDetails admin = User.builder()
						.username("Admin")
						.password(passwordEncoder.encode("pass"))
						.roles("ADMIN")
						.build();

		return new InMemoryUserDetailsManager(user, admin);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
						.requestMatchers("/css/**")
						.permitAll()
						.requestMatchers("/registration")
						.permitAll()
						.requestMatchers("/reg").permitAll()
						.anyRequest().authenticated())				//mindent authentikálunk
				.formLogin(config -> config
						.loginPage("/login")
						.defaultSuccessUrl("/", true).permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login?logout").permitAll()
				);
		return http.build();
	}
	
	/*@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.logoutSuccessUrl("/login?logout")
				.permitAll();
	}	
	*/
}

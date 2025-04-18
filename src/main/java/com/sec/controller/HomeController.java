package com.sec.controller;

import com.sec.entity.User;
import com.sec.repository.UserRepository;
import com.sec.service.EmailService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

	private EmailService emailService;
	private UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final Logger log = LoggerFactory.getLogger(this.getClass());                //sima controller miadt nézetekre bontjuk

    public HomeController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public String home(){
		return "index";
	}
	
	@RequestMapping("/bloggers")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public String bloggers(){
		return "bloggers";
	}
	
	@RequestMapping("/stories")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public String stories(){
		return "stories";
	}

	@RequestMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String admin(){
		return "admin";
	}

	@RequestMapping("/registration")
	public String registration(Model model) {
		log.info("helló/i");
		log.debug("hello/d");
		model.addAttribute("user", new User());
		return "registration";
	}

	@RequestMapping(path = "/activation/{code}", method = RequestMethod.GET)
	public String activation(@PathVariable("code") String code, HttpServletResponse response) {
		String result = userRepository.userActivation(code);
		return "auth/login?activationsuccess";
	}

	@RequestMapping(value = "/reg", method = RequestMethod.POST)
	public String greetingSubmit(@ModelAttribute User user) {
		emailService.sendMessage(user.getEmail(), user.getActivation());
		System.out.println("New User created!");
		log.info("New User");
		log.debug(user.getEmail());
		log.debug(passwordEncoder.encode(user.getPassword()));
		userRepository.registerUser(user);
		return "auth/login";
	}
}

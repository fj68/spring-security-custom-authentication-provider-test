package com.example.stockstore.controller;

import java.util.List;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.stockstore.entity.User;
import com.example.stockstore.repository.UserRepository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Controller
public class UsersController {
	@Data
	public static class SigninForm {
		@NotBlank
		private String username;
		@NotBlank
		private String password;
	}

	@Data
	public static class SignupForm {
		@NotBlank
		private String username;
		@NotBlank
		private String password;
		@NotBlank
		@Pattern(regexp = "ROLE_(ADMIN|USER|READONLY)")
		private String role;
	}

	private final UserRepository userRepository;

	public UsersController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostMapping("/signin")
	public String signIn(@Validated SigninForm form, BindingResult result, Model model, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			redirect.addFlashAttribute("errors",
					result.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList());
			return "redirect:/signin";
		}

		if (userRepository.get(form.getUsername()).isEmpty()) {
			redirect.addAttribute("errors",
					List.of(String.format("user name \"%s\" does not exist.", form.getUsername())));
			return "redirect:/signin";
		}

		return "redirect:/users";
	}

	@PostMapping("/signup")
	public String signUp(@Validated SignupForm form, BindingResult result, Model model, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			redirect.addFlashAttribute("errors",
					result.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList());
			return "redirect:/signup";
		}

		var passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

		var user = new User(form.getUsername(), passwordEncoder.encode(form.getPassword()), form.getRole());

		if (userRepository.get(form.getUsername()).isPresent()) {
			redirect.addAttribute("errors",
					List.of(String.format("user name \"%s\" is already taken.", form.getUsername())));
			return "redirect:/signup";
		}

		userRepository.create(user);

		redirect.addFlashAttribute("signup", user.getUsername());
		return "redirect:/users";
	}

	@GetMapping("/signin")
	String signinView(Model model) {
		return "users/signin";
	}

	@GetMapping("/signup")
	String signupView(Model model) {
		return "users/signup";
	}

	@GetMapping("/users")
	String usersView(Model model) {
		model.addAttribute("users", userRepository.getAll());
		return "users/list";
	}
}

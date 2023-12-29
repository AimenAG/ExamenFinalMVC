package com.example.controller;

import java.util.List;

import com.example.entities.User;
import com.example.repositories.UserRepository;
import com.example.service.RoleService;
import com.example.entities.Role;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AppController {

	private final RoleService roleService;
	private final UserService userService;
	private final UserRepository userRepo;

	@Autowired
	public AppController(RoleService roleService, UserService userService, UserRepository userRepo) {
		this.roleService = roleService;
		this.userService = userService;
		this.userRepo = userRepo;
	}

	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		List<Role> rolesList = roleService.getAllRoles();
		model.addAttribute("rolesList", rolesList);
		model.addAttribute("user", new User());
		return "signup_form";
	}
	
	@PostMapping("/process_register")
	public String processRegister(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		userRepo.save(user);
		
		return "register_success";
	}
	
	@GetMapping("/users")
	public String listUsers(Model model) {
		List<User> listUsers = userRepo.findAll();
		model.addAttribute("listUsers", listUsers);
		
		return "users";
	}

	@GetMapping("/editProfile")
	public String editProfile(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		User user = userService.findByEmail(username); // Replace with your method to find a user by email

		if (user != null) {
			model.addAttribute("user", user);
			List<Role> rolesList = roleService.getAllRoles();
			model.addAttribute("rolesList", rolesList);
		}

		return "editProfile";
	}

	@PostMapping("/editProfile")
	public String saveProfile(@ModelAttribute("user") User updatedUser) {

		userService.save(updatedUser);

		return "redirect:/users";
	}
	@GetMapping("/deleteUser/{id}")
	public String delete(@PathVariable Long id) {
		userService.deleteById(id);
		return "redirect:/users";
	}
}
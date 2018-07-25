package com.eventsapp.controllers;

import java.security.Principal;
import com.eventsapp.models.Event;

import javax.validation.Valid;

import com.eventsapp.models.Role;
import com.eventsapp.models.Users;
import com.eventsapp.repository.EventRepository;
import com.eventsapp.repository.RoleRepository;
import com.eventsapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

	@Autowired
	private UserRepository ur;

	@Autowired
	private RoleRepository rr;

	@Autowired
	private EventRepository er;

	@RequestMapping(value = "/entrar", method = RequestMethod.GET)
	public String Login() {
		return "users/login";
	}

	@RequestMapping(value = "/registre-se", method = RequestMethod.GET)
	public String Register() {
		return "users/registerUsers";
	}

	@RequestMapping(value = "/registre-se", method = RequestMethod.POST)
	public String RegisterUser(@Valid Users user, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			attributes.addFlashAttribute("msg", "Não foi possivel fazer seu cadastro, tente novamente!");
			return "redirect:/registre-se";
		}
		user.setPass(new BCryptPasswordEncoder().encode(user.getPassword()));
		ur.save(user);
		attributes.addFlashAttribute("msg", "Usuario cadastrado com sucesso!");
		return "redirect:/registre-se";
	}

	@RequestMapping(value = "/tornarAdmin", method = RequestMethod.GET)
	public ModelAndView ListUsersAndRoles() {
		ModelAndView mv = new ModelAndView("admin/becomeAdmin");
		Iterable<Users> users = ur.findAll();
		mv.addObject("users", users);

		Iterable<Role> roles = rr.findAll();
		mv.addObject("roles", roles);
		return mv;
	}

	@RequestMapping(value = "/conta", method = RequestMethod.GET)
	public ModelAndView ListUsersAndEvents(Principal principal) {
		ModelAndView mv = new ModelAndView("users/myAccount");
		Iterable<Event> event = er.findAll();
		Users user = ur.findByLogin(principal.getName());
		mv.addObject("events", event);
		mv.addObject("users", user);
		return mv;
	}
}
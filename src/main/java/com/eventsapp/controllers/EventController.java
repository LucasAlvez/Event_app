package com.eventsapp.controllers;

import java.security.Principal;

import javax.validation.Valid;

import com.eventsapp.models.Event;
import com.eventsapp.models.Guest;
import com.eventsapp.models.Users;
import com.eventsapp.repository.EventRepository;
import com.eventsapp.repository.GuestRepository;
import com.eventsapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EventController {

	@Autowired
	private EventRepository er;

	@Autowired
	private GuestRepository gr;

	@Autowired
	private UserRepository ur;

	@RequestMapping(value = "/cadastrar", method = RequestMethod.GET)
	public String register() {
		return "event/registerEvents";
	}

	@RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
	public String registerEvent(Principal principal, @Valid Event event, BindingResult result,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			attributes.addFlashAttribute("msg", "Não foi possivel cadastrar o evento, tente novamente!");
			return "redirect:/cadastrar";
		}
		Users user = ur.findByLogin(principal.getName());
		event.setUsers(user);
		er.save(event);
		attributes.addFlashAttribute("msg", "Evento cadastrado com sucesso!");
		return "redirect:/cadastrar";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String registerGuest(@PathVariable("id") long id, @Valid Guest guest, BindingResult result,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			attributes.addFlashAttribute("msg",
					"Não foi possivel confirmar sua participação no evento, tente novamente!");
			return "redirect:/{id}";
		}
		Event event = er.findById(id);
		guest.setEvent(event);
		gr.save(guest);
		attributes.addFlashAttribute("msg", "Parabéns, você está na lista de participantes do evento!");
		return "redirect:/{id}";
	}

	@RequestMapping(value = "/eventos", method = RequestMethod.GET)
	public ModelAndView listEvents() {
		ModelAndView mv = new ModelAndView("event/listEvents");
		Iterable<Event> event = er.findAll();
		mv.addObject("events", event);
		return mv;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView eventsDetails(@PathVariable("id") long id) {
		Event event = er.findById(id);
		ModelAndView mv = new ModelAndView("event/eventsDetails");
		mv.addObject("event", event);

		Iterable<Guest> guest = gr.findByEvent(event);
		mv.addObject("guests", guest);
		return mv;
	}

	@RequestMapping(value = "/deletarEvento", method = RequestMethod.GET)
	public String deleteEvent(long id) {
		Event event = er.findById(id);
		Iterable<Guest> guest = gr.findByEvent(event);
		gr.deleteAll(guest);
		er.delete(event);
		return "redirect:/eventos";
	}

	@RequestMapping(value = "/deletarParticipante", method = RequestMethod.GET)
	public String deleteGuest(String rg) {
		Guest guest = gr.findByRg(rg);
		gr.delete(guest);

		Event event = guest.getEvent();
		String cod = "" + event.getId();

		return "redirect:/" + cod;
	}
}
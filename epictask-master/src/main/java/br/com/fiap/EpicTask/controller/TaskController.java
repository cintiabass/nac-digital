package br.com.fiap.EpicTask.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fiap.EpicTask.model.Task;
import br.com.fiap.EpicTask.repository.TaskRepository;

@Controller
@RequestMapping("/task")
public class TaskController {
	
	@Autowired
	private TaskRepository repository;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping()
	public ModelAndView users() {
		List<Task> tasks = repository.findAll();
		ModelAndView modelAndView = new ModelAndView("tasks");
		modelAndView.addObject("tasks", tasks);
		return modelAndView;
	}

	@PostMapping()
	public String save(@Valid Task task, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors())
			return "task_new";
		repository.save(task);
		attributes.addFlashAttribute("message", getMessage("message.newtask.success"));
		return "redirect:/task";
	}

	@RequestMapping("new")
	public String formUser(Task task) {
		return "task_new";
	}

	@GetMapping("delete/{id}")
	public String deleteUser(@PathVariable("id") Long id) {
		repository.deleteById(id);
		return "redirect:/task";
	}
	
	@GetMapping("/{id}")
	public ModelAndView editTaskForm(@PathVariable Long id) {
		Optional<Task> task = repository.findById(id);
		ModelAndView modelAndView = new ModelAndView("task_edit");
		modelAndView.addObject("task", task);
		return modelAndView;		
	}

	@PostMapping("/update")
	public String updateUser(@Valid Task task, BindingResult result, RedirectAttributes redirect) {
		if (result.hasErrors()) return "task_edit";
		repository.save(task);
		redirect.addFlashAttribute("message", getMessage("message.edittask.success"));
		return "redirect:/task"; 
	}
	
	private String getMessage(String code) {
		return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
	}
	
}

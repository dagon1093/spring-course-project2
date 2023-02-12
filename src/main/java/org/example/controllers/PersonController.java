package org.example.controllers;

import org.example.models.Person;
import org.example.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PersonController {

    private final PeopleService peopleService;

    @Autowired
    public PersonController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping
    public String index(Model model){
        model.addAttribute("people", peopleService.findAll());
        return "people/all";
    }
    @GetMapping("/add")
    public String addPerson(Model model){
        model.addAttribute("person", new Person());
        return "people/add";
    }

    @PostMapping("/add")
    public String addPerson(@ModelAttribute("person") @Valid Person person,
                            BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "people/add";
        }
        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String personPage(@PathVariable("id") int id, Model model){
        Person person = peopleService.findById(id);
        model.addAttribute("person", person);
        if ( person.getBooks() != null){
            model.addAttribute("books", peopleService.getBooksByPersonId(id));
        }
        return "people/show";
    }
    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") int id, Model model){
        model.addAttribute(peopleService.findById(id));
        return "people/edit";
    }
    @PatchMapping("/{id}")
    public String edit(@ModelAttribute("person") @Valid Person person,
                       BindingResult bindingResult,
                       @PathVariable("id") int id){

        if(bindingResult.hasErrors()){
            return "people/edit";
        }

        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        Person person = new Person();
        person.setId(id);
        peopleService.delete(person);
        return "redirect:/people";
    }
}

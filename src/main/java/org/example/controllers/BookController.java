package org.example.controllers;

import org.example.models.Book;
import org.example.models.Person;
import org.example.services.BooksService;
import org.example.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }


    @GetMapping()
    public String showAll(Model model,
                          @RequestParam(name = "page", required = false) Integer page,
                          @RequestParam(name = "books_per_page", required = false) Integer books_per_page,
                          @RequestParam(name = "sort_by_year", required = false) boolean sort_by_year){
        if (page == null || books_per_page == null){
            model.addAttribute("books", booksService.findAll(sort_by_year));
        } else {
            model.addAttribute("books", booksService.findAll(page, books_per_page,sort_by_year));
        }
        return "books/all";
    }


    @GetMapping("/new")
    public String newBook(Model model){
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @PostMapping()
    public String newBook(@ModelAttribute("book") @Valid Book book,
                          BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "books/new";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id,
                       @ModelAttribute("person")Person person,
                       Model model){
        Person bookOwner = booksService.findById(id).getPerson();
        if (bookOwner != null){
            model.addAttribute("owner", bookOwner);
        } else {
            List<Person> people = peopleService.findAll();
            model.addAttribute("people", people);
        }

        model.addAttribute("book", booksService.findById(id));
//        model.addAttribute("person", new Person());
        return "books/show";
    }

    @PatchMapping("/{id}")
    public String edit(@ModelAttribute("person") Person person, @PathVariable("id") Integer id){
        Book book = booksService.findById(id);
        book.setPerson(person);
        booksService.update(book.getId(),book);
        return "redirect:/books/" + id;
    }

    @GetMapping("/{id}/edit")
    public String editBook(Model model, @PathVariable("id") Integer id){
        model.addAttribute("book", booksService.findById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}/edit")
    public String editBook(@ModelAttribute("book") Book book, @PathVariable("id") Integer id){
        booksService.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id){
        booksService.releaseBook(id);
        return "redirect:/books/" + id;
    }

    @PostMapping("/search")
    public String search(@RequestParam(name = "searchString") String searchString,
                         Model model){
        model.addAttribute("books",booksService.findByTitleStartingWith(searchString));
        return "books/search";
    }

    @GetMapping("/search")
    public String search(){
        return "books/search";
    }

}

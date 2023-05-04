package spring.Project2Boot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.Project2Boot.models.Book;
import spring.Project2Boot.models.Person;
import spring.Project2Boot.services.BookService;
import spring.Project2Boot.services.PeopleService;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/books")
public class BookController {

    private final PeopleService peopleService;
    private final BookService bookService;

    @Autowired
    public BookController(PeopleService peopleService, BookService bookService) {
        this.peopleService = peopleService;
        this.bookService = bookService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                        @RequestParam(value = "books_per_page", required = false, defaultValue = "0") int booksPerPage,
                        @RequestParam(value = "sort_by", required = false, defaultValue = "false") String sort) {
        List<Book> list = bookService.findAll();
        if (page != 0 && booksPerPage != 0 && !sort.equals("false")){
            switch (sort){
                case "id":
                    list = bookService.findAll(PageRequest.of(page, booksPerPage), Sort.by("id"));
                    break;
                case "title":
                    list = bookService.findAll(PageRequest.of(page, booksPerPage), Sort.by("title"));
                    break;
                case "author":
                    list = bookService.findAll(PageRequest.of(page, booksPerPage), Sort.by("author"));
                    break;
                case "year":
                    list = bookService.findAll(PageRequest.of(page, booksPerPage), Sort.by("year"));
                    break;
            }
        }
        else if (page != 0 && booksPerPage != 0){
            list = bookService.findAll(PageRequest.of(page, booksPerPage));
        }
        else if (sort != null){
            switch (sort){
                case "id":
                    list = bookService.findAll(Sort.by("id"));
                    break;
                case "title":
                    list = bookService.findAll(Sort.by("title"));
                    break;
                case "author":
                    list = bookService.findAll(Sort.by("author"));
                    break;
                case "year":
                    list = bookService.findAll(Sort.by("year"));
                    break;
            }
        }
        model.addAttribute("books", list);
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.findOne(id));

        Optional<Person> bookOwner = bookService.findOne(id).getOwner();

        if (bookOwner.isPresent())
            model.addAttribute("owner", bookOwner.get());
        else
            model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book Book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book Book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";
        bookService.save(Book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";
        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String search(@ModelAttribute("book") Book Book){
        return "books/search";
    }

    @PatchMapping("/search")
    public String search(@RequestParam("title") String title, Model model){
        model.addAttribute("books", bookService.findByTitleStartingWith(title));
        return "books/search";
    }

}

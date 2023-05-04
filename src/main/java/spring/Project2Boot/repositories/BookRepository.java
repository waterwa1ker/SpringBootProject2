package spring.Project2Boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.Project2Boot.models.Book;


import java.util.List;


public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByTitleStartingWith(String title);

}

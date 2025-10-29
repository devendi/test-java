package com.devendi.app.web;

import com.devendi.app.model.Book;
import com.devendi.app.repo.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
  private final BookRepository repo;
  public BookController(BookRepository repo) { this.repo = repo; }

  @GetMapping public List<Book> all() { return repo.findAll(); }

  @GetMapping("/{id}")
  public ResponseEntity<Book> one(@PathVariable Long id) {
    return repo.findById(id).map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Book> create(@RequestBody Book b) {
    if (b.getId()!=null) b.setId(null);
    return ResponseEntity.ok(repo.save(b));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Book> update(@PathVariable Long id, @RequestBody Book b) {
    return repo.findById(id).map(ex -> {
      ex.setTitle(b.getTitle());
      ex.setAuthor(b.getAuthor());
      return ResponseEntity.ok(repo.save(ex));
    }).orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (!repo.existsById(id)) return ResponseEntity.notFound().build();
    repo.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
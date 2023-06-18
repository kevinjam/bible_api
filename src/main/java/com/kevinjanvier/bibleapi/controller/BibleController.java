package com.kevinjanvier.bibleapi.controller;

import com.kevinjanvier.bibleapi.model.*;
import com.kevinjanvier.bibleapi.services.BibleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bible")
public class BibleController {

    @Autowired
    private BibleService bibleService;

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bibleService.getAllBooks();
    }

    @GetMapping("/books/{book}/{chapter}")
    public Chapter getChapterByNumber(@PathVariable int chapter,
                                      @PathVariable String book) {
        return bibleService.getChapterByNumber(book,chapter);
    }

//    @GetMapping("/books/{chapter}/{verse}")
//    public Verse getVerseByNumber(@PathVariable int chapter, @PathVariable int verse) {
//        return bibleService.getVerseByNumber(chapter, verse);
//    }

}

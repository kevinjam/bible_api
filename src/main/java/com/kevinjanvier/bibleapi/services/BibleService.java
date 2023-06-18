package com.kevinjanvier.bibleapi.services;

import com.kevinjanvier.bibleapi.model.Book;
import com.kevinjanvier.bibleapi.model.Chapter;
import com.kevinjanvier.bibleapi.model.Verse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class BibleService {
    private final RestTemplate restTemplate;

    @Value("${esv.api.key}")
    private String apiKey;

    public BibleService() {
        this.restTemplate = new RestTemplate();
    }

    public List<Book> getAllBooks() {
        String apiUrl = "https://api.esv.org/v3/passage/books/";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(apiUrl));
        ResponseEntity<BookResponse> responseEntity = restTemplate.exchange(requestEntity, BookResponse.class);
        BookResponse bookResponse = responseEntity.getBody();

        List<Book> books = new ArrayList<>();
        if (bookResponse != null && bookResponse.getBooks() != null) {
            for (String bookName : bookResponse.getBooks()) {
                books.add(new Book(bookName));
            }
        }

        return books;
    }

    public Chapter getChapterByNumber(String bookName, int chapterNumber) {
        String apiUrl = "https://api.esv.org/v3/passage/html/";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String queryString = bookName + " " + chapterNumber;
        String url = apiUrl + "?q=" + queryString;

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
        ResponseEntity<ChapterResponse> responseEntity = restTemplate.exchange(requestEntity, ChapterResponse.class);
        ChapterResponse chapterResponse = responseEntity.getBody();

        List<Verse> verses = new ArrayList<>();
        if (chapterResponse != null && chapterResponse.getPassages() != null) {
            String passageHtml = chapterResponse.getPassages().get(0);
            String[] verseLines = passageHtml.split("<br>");

            int verseNumber = 1;
            for (String verseLine : verseLines) {
                String verseText = verseLine.replaceAll("<[^>]+>", "").trim();
                verses.add(new Verse(verseNumber++, verseText));
            }
        }

        return new Chapter(chapterNumber, verses);
    }

    // Response classes for JSON deserialization
    private static class BookResponse {
        private List<String> books;

        public List<String> getBooks() {
            return books;
        }
    }

    private static class ChapterResponse {
        private List<String> passages;

        public List<String> getPassages() {
            return passages;
        }
    }
}

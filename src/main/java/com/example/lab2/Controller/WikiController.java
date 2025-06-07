package com.example.lab2.Controller;

import com.example.lab2.Dto.WikipediaDto;
import com.example.lab2.Model.WikiPage;
import com.example.lab2.Service.WikiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wiki")
@RequiredArgsConstructor
public class WikiController {
    private final WikiService wikiService;

    @GetMapping("/search")
    public WikipediaDto searchWikipedia(@RequestParam String term) {
        return wikiService.fetchWikipediaSummary(term);
    }

    @PostMapping("/search-and-save")
    public WikiPage searchAndSave(@RequestParam String term, @RequestParam Long categoryId) {
        return wikiService.fetchAndSaveWikiPage(term, categoryId);
    }

    @GetMapping
    public List<WikiPage> getAllWikiPages() {
        return wikiService.findAllWikiPages();
    }

    @GetMapping("/{id}")
    public WikiPage getWikiPageById(@PathVariable Long id) {
        return wikiService.findWikiPageById(id);
    }

    @PostMapping
    public WikiPage createWikiPage(@RequestBody WikiPage wikiPage) {
        return wikiService.saveWikiPage(wikiPage);
    }

    @PutMapping("/{id}")
    public WikiPage updateWikiPage(@PathVariable Long id, @RequestBody WikiPage wikiPageDetails) {
        return wikiService.updateWikiPage(id, wikiPageDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteWikiPage(@PathVariable Long id) {
        wikiService.deleteWikiPage(id);
    }
    @GetMapping("/by-category/{categoryId}")
    public List<WikiPage> getWikiPagesByCategory(@PathVariable Long categoryId) {
        return wikiService.findWikiPagesByCategory(categoryId);
    }
}
package com.example.lab2.Cache;

import com.example.lab2.Model.Category;
import com.example.lab2.Model.WikiPage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryCache {

    private final Map<Long, WikiPage> wikiPageByIdCache = new HashMap<>();

    private final Map<Long, List<WikiPage>> wikiPagesByCategoryCache = new HashMap<>();

    private final Map<Long, Category> categoryByIdCache = new HashMap<>();

    public void putWikiPage(Long id, WikiPage wikiPage) {
        wikiPageByIdCache.put(id, wikiPage);
    }

    public WikiPage getWikiPage(Long id) {
        return wikiPageByIdCache.get(id);
    }

    public boolean containsWikiPage(Long id) {
        return wikiPageByIdCache.containsKey(id);
    }

    public void removeWikiPage(Long id) {
        wikiPageByIdCache.remove(id);
    }

    public void putWikiPagesByCategory(Long categoryId, List<WikiPage> wikiPages) {
        wikiPagesByCategoryCache.put(categoryId, wikiPages);
    }

    public List<WikiPage> getWikiPagesByCategory(Long categoryId) {
        return wikiPagesByCategoryCache.get(categoryId);
    }

    public boolean containsWikiPagesByCategory(Long categoryId) {
        return wikiPagesByCategoryCache.containsKey(categoryId);
    }

    public void removeWikiPagesByCategory(Long categoryId) {
        wikiPagesByCategoryCache.remove(categoryId);
    }

    public void putCategory(Long id, Category category) {
        categoryByIdCache.put(id, category);
    }

    public Category getCategory(Long id) {
        return categoryByIdCache.get(id);
    }

    public boolean containsCategory(Long id) {
        return categoryByIdCache.containsKey(id);
    }

    public void removeCategory(Long id) {
        categoryByIdCache.remove(id);
    }
}

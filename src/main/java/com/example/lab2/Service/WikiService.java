package com.example.lab2.Service;

import com.example.lab2.Cache.InMemoryCache;
import com.example.lab2.Dto.WikipediaDto;
import com.example.lab2.Model.WikiPage;
import com.example.lab2.Repository.WikiPageRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WikiService {
    private final WikiPageRepository wikiPageRepository;
    private final InMemoryCache cache;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String WIKIPEDIA_API_URL =
            "https://ru.wikipedia.org/w/api.php?action=query&prop=extracts&exintro=true&explaintext=true&format=json&titles=%s";

    public WikipediaDto fetchWikipediaSummary(String term) {
        try {
            String url = String.format(WIKIPEDIA_API_URL, term.replace(" ", "%20"));
            String jsonResponse = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode pages = root.path("query").path("pages");

            if (pages.elements().hasNext()) {
                JsonNode page = pages.elements().next();
                String extract = page.path("extract").asText("Описание не найдено.");
                return new WikipediaDto(term, extract);
            } else {
                return new WikipediaDto(term, "Результат не найден.");
            }
        } catch (Exception e) {
            return new WikipediaDto(term, "Ошибка при получении данных: " + e.getMessage());
        }
    }

    public WikiPage fetchAndSaveWikiPage(String term, Long categoryId) {
        WikipediaDto dto = fetchWikipediaSummary(term);
        WikiPage wikiPage = new WikiPage();
        wikiPage.setTerm(dto.getTerm());
        wikiPage.setSummary(dto.getSummary());
        WikiPage saved = wikiPageRepository.save(wikiPage);
        cache.putWikiPage(saved.getId(), saved);
        cache.removeWikiPagesByCategory(categoryId);
        return saved;
    }

    public List<WikiPage> findAllWikiPages() {
        return wikiPageRepository.findAll();
    }

    public WikiPage findWikiPageById(Long id) {
        if (cache.containsWikiPage(id)) {
            return cache.getWikiPage(id);
        }
        WikiPage wp = wikiPageRepository.findById(id).orElseThrow();
        cache.putWikiPage(id, wp);
        return wp;
    }

    public WikiPage saveWikiPage(WikiPage wikiPage) {
        WikiPage saved = wikiPageRepository.save(wikiPage);
        cache.putWikiPage(saved.getId(), saved);
        if (saved.getCategory() != null) {
            cache.removeWikiPagesByCategory(saved.getCategory().getId());
        }
        return saved;
    }

    public WikiPage updateWikiPage(Long id, WikiPage wikiPageDetails) {
        WikiPage wikiPage = findWikiPageById(id);
        wikiPage.setTerm(wikiPageDetails.getTerm());
        wikiPage.setSummary(wikiPageDetails.getSummary());
        WikiPage updated = wikiPageRepository.save(wikiPage);
        cache.putWikiPage(updated.getId(), updated);
        if (updated.getCategory() != null) {
            cache.removeWikiPagesByCategory(updated.getCategory().getId());
        }
        return updated;
    }

    public void deleteWikiPage(Long id) {
        WikiPage wikiPage = findWikiPageById(id);
        wikiPageRepository.deleteById(id);
        cache.removeWikiPage(id);
        if (wikiPage.getCategory() != null) {
            cache.removeWikiPagesByCategory(wikiPage.getCategory().getId());
        }
    }

    public List<WikiPage> findWikiPagesByCategory(Long categoryId) {
        if (cache.containsWikiPagesByCategory(categoryId)) {
            return cache.getWikiPagesByCategory(categoryId);
        }
        List<WikiPage> list = wikiPageRepository.findByCategoryId(categoryId);
        cache.putWikiPagesByCategory(categoryId, list);
        return list;
    }
}

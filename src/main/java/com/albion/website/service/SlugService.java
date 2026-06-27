package com.albion.website.service;

import com.ibm.icu.text.Transliterator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class SlugService {

    private final Transliterator transliterator =
            Transliterator.getInstance("Any-Latin; Latin-ASCII");

    public String generateSlug(String title, int maxWords) {
        String base = toSlug(title);
        return shortenSlug(base, maxWords);
    }

    private String toSlug(String titleInput) {
        String result = transliterator.transliterate(titleInput);
        result = result.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");

        return shortenSlug(result, 5);
    }

    public String shortenSlug(String slug, int maxWords) {
        String[] words = slug.split("-");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length && i < maxWords; i++) {
            if (i > 0) result.append("-");
            result.append(words[i]);
        }

        return result.toString();
    }

    public String generateUniqueSlug(
            String name,
            Function<String, Boolean> existsBySlug
    ) {
        String base = generateSlug(name, 5);

        if (!existsBySlug.apply(base)) {
            return base;
        }

        int i = 2;
        while (true) {
            String slug = base + "-" + i;

            if (!existsBySlug.apply(slug)) {
                return slug;
            }

            i++;
        }
    }
}
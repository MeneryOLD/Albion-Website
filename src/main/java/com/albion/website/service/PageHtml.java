package com.albion.website.service;

/**
 * Helpers for HTML produced by the admin page builder.
 */
public final class PageHtml {

    private PageHtml() {
    }

    /**
     * Returns {@code null} when the builder output has no real content
     * (only whitespace and/or empty {@code <style>} blocks), so the public page
     * falls back to its standard template instead of rendering a blank custom page.
     */
    public static String normalize(String html) {
        if (html == null) {
            return null;
        }
        String withoutStyles = html.replaceAll("(?is)<style[^>]*>.*?</style>", "");
        if (withoutStyles.replaceAll("(?s)<!--.*?-->", "").isBlank()) {
            return null;
        }
        return html;
    }
}

package com.example.android.newsappstage2;

/**
 * An {@link News} object contains information.
 */
public class News {
    private String articleTitle, articleCategory, articleDate, articleUrl, articleAuthor, articleImage;

    /**
     * Constructs a new {@link News} object.
     */
    public News(String articleTitle, String articleCategory, String articleDate, String articleUrl, String articleAuthor, String articleImage) {
        this.articleTitle = articleTitle;
        this.articleCategory = articleCategory;
        this.articleDate = articleDate;
        this.articleUrl = articleUrl;
        this.articleAuthor = articleAuthor;
        this.articleImage = articleImage;
    }

    /**
     * Returns the  ArticleTitle of the news.
     */
    public String getArticleTitle() {
        return articleTitle;
    }

    /**
     * Returns the ArticleCategory of the news.
     */
    public String getArticleCategory() {
        return articleCategory;
    }

    /**
     * Returns the ArticleDate of the news.
     */
    public String getArticleDate() {
        return articleDate;
    }

    /**
     * Returns the ArticleUrl of the news.
     */
    public String getArticleUrl() {
        return articleUrl;
    }

    /**
     * Returns the ArticleAuthor of the news.
     */
    public String getArticleAuthor() {
        return articleAuthor;
    }

    public String getArticleImage() {
        return articleImage;
    }

}

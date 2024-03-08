package edu.bethlehem.scinexus.Article;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Article {
    private @Id @GeneratedValue Long id;
    private String content;

    public Article(String content) {
        this.content = content;
    }

    public Article() {
    }
}
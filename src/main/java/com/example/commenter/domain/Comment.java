package com.example.commenter.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Comment.
 */
@Entity
@Table(name = "comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 2047)
    @Column(name = "url", length = 2047, nullable = false)
    private String url;

    @Column(name = "content")
    private String content;

    @OneToOne
    @JoinColumn(unique = true)
    private Author author;

    @OneToMany(mappedBy = "comment")
    @JsonIgnoreProperties(value = { "comment" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Comment id(Long id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return this.url;
    }

    public Comment url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return this.content;
    }

    public Comment content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Author getAuthor() {
        return this.author;
    }

    public Comment author(Author author) {
        this.setAuthor(author);
        return this;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public Comment tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Comment addTags(Tag tag) {
        this.tags.add(tag);
        tag.setComment(this);
        return this;
    }

    public Comment removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.setComment(null);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.setComment(null));
        }
        if (tags != null) {
            tags.forEach(i -> i.setComment(this));
        }
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        return id != null && id.equals(((Comment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comment{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}

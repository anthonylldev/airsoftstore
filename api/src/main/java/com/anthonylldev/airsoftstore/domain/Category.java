package com.anthonylldev.airsoftstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "cover")
    private byte[] cover;

    @Column(name = "cover_content_type")
    private String coverContentType;

    @OneToMany(mappedBy = "category")
    @JsonIgnoreProperties(value = { "category", "items" }, allowSetters = true)
    private Set<SubCategory> subcategories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Category id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Category title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getCover() {
        return this.cover;
    }

    public Category cover(byte[] cover) {
        this.setCover(cover);
        return this;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public String getCoverContentType() {
        return this.coverContentType;
    }

    public Category coverContentType(String coverContentType) {
        this.coverContentType = coverContentType;
        return this;
    }

    public void setCoverContentType(String coverContentType) {
        this.coverContentType = coverContentType;
    }

    public Set<SubCategory> getSubcategories() {
        return this.subcategories;
    }

    public void setSubcategories(Set<SubCategory> subCategories) {
        if (this.subcategories != null) {
            this.subcategories.forEach(i -> i.setCategory(null));
        }
        if (subCategories != null) {
            subCategories.forEach(i -> i.setCategory(this));
        }
        this.subcategories = subCategories;
    }

    public Category subcategories(Set<SubCategory> subCategories) {
        this.setSubcategories(subCategories);
        return this;
    }

    public Category addSubcategory(SubCategory subCategory) {
        this.subcategories.add(subCategory);
        subCategory.setCategory(this);
        return this;
    }

    public Category removeSubcategory(SubCategory subCategory) {
        this.subcategories.remove(subCategory);
        subCategory.setCategory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", cover='" + getCover() + "'" +
            ", coverContentType='" + getCoverContentType() + "'" +
            "}";
    }
}

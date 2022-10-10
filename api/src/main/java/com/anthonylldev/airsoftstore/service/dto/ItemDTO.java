package com.anthonylldev.airsoftstore.service.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.anthonylldev.airsoftstore.domain.Item} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    @Min(value = 0L)
    private Long price;

    @NotNull
    @Min(value = 0)
    private Integer stock;

    private String description;

    @Lob
    private byte[] cover;

    private String coverContentType;
    private BrandDTO brand;

    private SubCategoryDTO subCategory;

    private LocalDateTime inclusionDate;

    private Integer discount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public String getCoverContentType() {
        return coverContentType;
    }

    public void setCoverContentType(String coverContentType) {
        this.coverContentType = coverContentType;
    }

    public BrandDTO getBrand() {
        return brand;
    }

    public void setBrand(BrandDTO brand) {
        this.brand = brand;
    }

    public SubCategoryDTO getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategoryDTO subCategory) {
        this.subCategory = subCategory;
    }

    public LocalDateTime getInclusionDate() {
        return inclusionDate;
    }

    public void setInclusionDate(LocalDateTime inclusionDate) {
        this.inclusionDate = inclusionDate;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemDTO)) {
            return false;
        }

        ItemDTO itemDTO = (ItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, itemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", price=" + getPrice() +
            ", stock=" + getStock() +
            ", description='" + getDescription() + "'" +
            ", cover='" + getCover() + "'" +
            ", brand=" + getBrand() +
            ", subCategory=" + getSubCategory() +
            ", inclusionDate=" + getInclusionDate() +
            ", discount=" + getDiscount() +
            "}";
    }
}

package com.anthonylldev.airsoftstore.service.dto;

import javax.persistence.Lob;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A simple DTO the {@link com.anthonylldev.airsoftstore.domain.Item} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SimpleItemDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    @Min(value = 0L)
    private Long price;

    @NotNull
    @Min(value = 0)
    private Integer stock;

    @Lob
    private byte[] cover;

    private String coverContentType;

    private LocalDateTime inclusionDate;

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

    public LocalDateTime getInclusionDate() {
        return inclusionDate;
    }

    public void setInclusionDate(LocalDateTime inclusionDate) {
        this.inclusionDate = inclusionDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimpleItemDTO)) {
            return false;
        }

        SimpleItemDTO simpleItemDTO = (SimpleItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, simpleItemDTO.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", price=" + getPrice() +
            ", stock=" + getStock() +
            ", inclusionDate=" + getInclusionDate() +
            "}";
    }
}

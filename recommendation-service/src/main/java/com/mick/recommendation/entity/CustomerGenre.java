package com.mick.recommendation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CustomerGenre {

    @Id
    private Integer customerId;
    private String favoriteGenre;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getFavoriteGenre() {
        return favoriteGenre;
    }

    public void setFavoriteGenre(String favoriteGenre) {
        this.favoriteGenre = favoriteGenre;
    }
}

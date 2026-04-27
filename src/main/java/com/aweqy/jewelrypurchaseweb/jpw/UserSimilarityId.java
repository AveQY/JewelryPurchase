package com.aweqy.jewelrypurchaseweb.jpw;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class UserSimilarityId implements Serializable {
    @Column(name = "user_id")
    private Integer userId;


    @Column(name = "similar_user_id")
    private Integer similarUserId;

    public Integer getSimilarUserId() {
        return similarUserId;
    }

    public void setSimilarUserId(Integer similarUserId) {
        this.similarUserId = similarUserId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}

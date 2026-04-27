package com.aweqy.jewelrypurchaseweb.jpw;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "user_similarities")
@Data
public class UserSimilarity {
    @EmbeddedId
    private UserSimilarityId id = new UserSimilarityId();

    @Column(name = "similarity_score", precision = 10, scale = 9)
    private BigDecimal similarityScore;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("similarUserId")
    @JoinColumn(name = "similar_user_id")
    private User similarUser;

    public UserSimilarityId getId() {
        return id;
    }

    public void setId(UserSimilarityId id) {
        this.id = id;
    }

    public BigDecimal getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(BigDecimal similarityScore) {
        this.similarityScore = similarityScore;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getSimilarUser() {
        return similarUser;
    }

    public void setSimilarUser(User similarUser) {
        this.similarUser = similarUser;
    }
}

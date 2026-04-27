package com.aweqy.jewelrypurchaseweb.Dao;

import com.aweqy.jewelrypurchaseweb.jpw.Carousel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarouselRepository extends JpaRepository<Carousel, Integer> {
    @Query("SELECT u FROM Carousel u")
    List<Carousel> findAllCarousel();

    @Query("SELECT count(u) FROM Carousel u")
    Integer findCarouselCount();
}
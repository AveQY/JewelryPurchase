package com.aweqy.jewelrypurchaseweb.controller;

import com.aweqy.jewelrypurchaseweb.jpw.Carousel;
import com.aweqy.jewelrypurchaseweb.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    @GetMapping("/api/carousel")
    public List<Carousel> getCarouselImages() {
        return carouselService.findAllCarousel();
    }

    @GetMapping("/api/search/carousel/count")
    public Integer getCarouselImagesCount() {
        return carouselService.getCarouseCount();
    }
}
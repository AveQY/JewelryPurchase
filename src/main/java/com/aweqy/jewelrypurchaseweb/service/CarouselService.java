package com.aweqy.jewelrypurchaseweb.service;

import com.aweqy.jewelrypurchaseweb.Dao.CarouselRepository;
import com.aweqy.jewelrypurchaseweb.jpw.Carousel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarouselService {

    @Autowired
    private CarouselRepository carouselRepository;

    private CarouselRepository carouseCount;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Carousel> findAllCarousel() {
        return carouselRepository.findAllCarousel();
    }

    public Integer getCarouseCount() {
        return carouselRepository.findCarouselCount();
    }

}

package com.codecademy.goldmedal.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.codecademy.goldmedal.model.GoldMedal;

public interface GoldMedalRepository extends JpaRepository<GoldMedal, Integer> {
    List<GoldMedal> findByCountry(String country, Sort sort);

    List<GoldMedal> findByCountryAndSeason(String country, String season);

    List<GoldMedal> findBySeason(String season);

    List<GoldMedal> findByGenderAndCountry(String gender, String country);
}

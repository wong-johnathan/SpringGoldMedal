package com.codecademy.goldmedal.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codecademy.goldmedal.model.Country;

public interface CountryRepository extends JpaRepository<Country, Integer>{
    Optional<Country> findByName(String countryName);
}

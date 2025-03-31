package com.codecademy.goldmedal.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.text.WordUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codecademy.goldmedal.model.CountriesResponse;
import com.codecademy.goldmedal.model.Country;
import com.codecademy.goldmedal.model.CountryDetailsResponse;
import com.codecademy.goldmedal.model.CountryMedalsListResponse;
import com.codecademy.goldmedal.model.CountrySummary;
import com.codecademy.goldmedal.model.GoldMedal;
import com.codecademy.goldmedal.repositories.CountryRepository;
import com.codecademy.goldmedal.repositories.GoldMedalRepository;

@RestController
@RequestMapping("/countries")
public class GoldMedalController {
    private final CountryRepository countryRepository;
    private final GoldMedalRepository goldMedalRepository;

    public GoldMedalController(final CountryRepository countryRepository,
            final GoldMedalRepository goldMedalRepository) {
        this.countryRepository = countryRepository;
        this.goldMedalRepository = goldMedalRepository;
    }

    @GetMapping
    public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
    }

    @GetMapping("/{country}")
    public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
        String countryName = WordUtils.capitalizeFully(country);
        return getCountryDetailsResponse(countryName);
    }

    @GetMapping("/{country}/medals")
    public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by,
            @RequestParam String ascending) {
        String countryName = WordUtils.capitalizeFully(country);
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
    }

    private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy,
            boolean ascendingOrder) {
        List<GoldMedal> medalsList;
        Direction sortDirection = ascendingOrder ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy);
        if (countryName != null)
            medalsList = this.goldMedalRepository.findByCountry(countryName, sort);
        else
            medalsList = this.goldMedalRepository.findAll(sort);
        return new CountryMedalsListResponse(medalsList);
    }

    private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
        Optional<Country> countryOptional = this.countryRepository.findByName(countryName);
        if (countryOptional.isEmpty()) {
            return new CountryDetailsResponse(countryName);
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "year");
        
        Country country = countryOptional.get();
        int goldMedalCount = this.goldMedalRepository.findByCountry(countryName, sort).size();

        List<GoldMedal> summerWins = this.goldMedalRepository.findByCountryAndSeason(countryName, "Summer");
        int numberSummerWins = !summerWins.isEmpty() ? summerWins.size() : 0;
        int totalSummerEvents = this.goldMedalRepository.findBySeason("Summer").size();
        float percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != 0
                ? (float) summerWins.size() / totalSummerEvents
                : 0;
        int yearFirstSummerWin = !summerWins.isEmpty() ? summerWins.get(0).getYear() : 0;

        List<GoldMedal> winterWins = this.goldMedalRepository.findByCountryAndSeason(countryName, "Winter");
        int numberWinterWins = !winterWins.isEmpty() ? winterWins.size() : 0;
        int totalWinterEvents = this.goldMedalRepository.findBySeason("Winter").size();
        float percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != 0
                ? (float) winterWins.size() / totalWinterEvents
                : 0;
        int yearFirstWinterWin = !winterWins.isEmpty() ? winterWins.get(0).getYear() : 0;

        int numberEventsWonByFemaleAthletes = this.goldMedalRepository.findByGenderAndCountry("Women", countryName).size();
        int numberEventsWonByMaleAthletes = this.goldMedalRepository.findByGenderAndCountry("Men", countryName).size();
        return new CountryDetailsResponse(
                countryName,
                country.getGdp(),
                country.getPopulation(),
                goldMedalCount,
                numberSummerWins,
                percentageTotalSummerWins,
                yearFirstSummerWin,
                numberWinterWins,
                percentageTotalWinterWins,
                yearFirstWinterWin,
                numberEventsWonByFemaleAthletes,
                numberEventsWonByMaleAthletes);
    }

    private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
        List<Country> countries;
        Direction sortDirection = ascendingOrder ? Sort.Direction.ASC : Sort.Direction.DESC;
        countries = this.countryRepository.findAll(Sort.by(sortDirection, sortBy));

        var countrySummaries = getCountrySummariesWithMedalCount(countries);

        if (sortBy.equalsIgnoreCase("medals")) {
            countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
        }

        return countrySummaries;
    }

    private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
        return countrySummaries.stream()
                .sorted((t1, t2) -> ascendingOrder ? t1.getMedals() - t2.getMedals() : t2.getMedals() - t1.getMedals())
                .collect(Collectors.toList());
    }

    private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
        List<CountrySummary> countrySummaries = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.DESC, "year");
        for (var country : countries) {
            var goldMedalCount = this.goldMedalRepository.findByCountry(country.getName(),sort).size();
            countrySummaries.add(new CountrySummary(country, goldMedalCount));
        }
        return countrySummaries;
    }
}

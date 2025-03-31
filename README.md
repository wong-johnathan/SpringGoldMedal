# Gold Medal Metrics Challenge

## Overview

This project serves as a personal challenge to build an Olympics analytics web application using **Spring Data JPA**. The app allows users to access and analyze data related to Olympic countries, athletes, and medals. Through this project, I explore concepts like data modeling, RESTful API design, and database interactions in Spring Boot.

The app provides various endpoints to retrieve Olympic statistics, including country-specific details and a list of Gold medal winners, all powered by a backend implemented in Java using Spring Data JPA.

## Features

- **Country Information**: Fetch details about Olympic countries, including GDP, population, and medal counts.
- **Gold Medal Winners**: Retrieve a list of athletes who have won Gold medals for a specific country, sortable by various fields.
- **Sorting and Filtering**: Supports sorting by fields like athlete name and medal year, with ascending or descending order.

## Testing the API

You can manually test the API endpoints using `cURL`. Below are a few example requests and their expected responses.

### 1. Get Countries Sorted by Name (Ascending Order)

This request fetches a list of Olympic countries, sorted by name in ascending order:

```bash
curl --request GET "http://localhost:3001/countries?sort_by=name&ascending=y"
```

**Expected Response:**

```json
{
  "countries": [
    {
      "name": "Afghanistan",
      "code": "AFG",
      "gdp": 594.32,
      "population": 32526562,
      "medals": 0
    },
    ...
  ]
}
```

### 2. Get Details for the United States Olympic Team

This request retrieves detailed Olympic data for the United States, including medal counts and historical performance:

```bash
curl --request GET "http://localhost:3001/countries/united%20states"
```

**Expected Response:**

```json
{
  "name": "United States",
  "gdp": "56115.72",
  "population": "321418820",
  "numberMedals": "2477",
  "numberSummerWins": "2302",
  "percentageTotalSummerWins": "21.957268",
  "yearFirstSummerWin": "1896",
  "numberWinterWins": "175",
  "percentageTotalWinterWins": "9.1098385",
  "yearFirstWinterWin": "1924",
  "numberEventsWonByFemaleAthletes": "747",
  "numberEventsWonByMaleAthletes": "1730"
}
```

### 3. Get Gold Medal Winners for the United States, Sorted by Athlete Name (Descending Order)

This request fetches a list of Gold medal winners for the United States, sorted by the athlete's name in descending order:

```bash
curl --request GET "http://localhost:3001/countries/united%20states/medals?sort_by=name&ascending=n"
```

**Expected Response:**

```json
{
  "medals": [
    {
      "year": 1968,
      "city": "Mexico",
      "season": "Summer",
      "name": "ZORN, Zachary",
      "country": "United States",
      "gender": "Men",
      "sport": "Aquatics",
      "discipline": "Swimming",
      "event": "4X100M Freestyle Relay"
    },
    ...
  ]
}
```

## Technologies Used

- **Spring Boot**: Framework for building the web application.
- **Spring Data JPA**: Used for database interaction and entity management.
- **H2 Database**: In-memory database for storing Olympic data (you can switch to a more robust database for production).
- **REST API**: Exposes endpoints to interact with the Olympic data.
- **cURL**: Used for testing the API endpoints manually.

## Challenge and Learnings

This project provided a hands-on opportunity to work with Spring Boot, Spring Data JPA, and RESTful API development. Key challenges included designing a flexible data model, implementing sorting functionality, and learning how to work with Spring Data JPA's repository methods to query data efficiently.

Through this project, I improved my skills in:
- Setting up a Spring Boot project and structuring a Spring Data JPA repository.
- Designing and implementing RESTful APIs.
- Working with various data types and managing database queries with JPA.

## Future Improvements

- Implement pagination for large sets of data.
- Add authentication and authorization to secure endpoints.
- Expand data model to include more Olympic data such as Winter and Summer games statistics.
- Refactor the project for deployment in a production environment (e.g., using PostgreSQL instead of an in-memory database).

---

This version of the README makes it clear that this is a personal project and highlights the key technologies, features, challenges, and learnings involved. Let me know if you'd like to add or modify any other details!
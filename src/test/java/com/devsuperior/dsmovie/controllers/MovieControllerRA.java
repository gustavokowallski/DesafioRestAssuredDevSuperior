package com.devsuperior.dsmovie.controllers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class MovieControllerRA {

    private String titleName;

    @BeforeEach
    void setUp(){
        titleName = "Guerra Mundial Z";
        baseURI = "http://localhost:8080";
    }

    @Test
    public void findAllShouldReturnOkWhenMovieNoArgumentsGiven(){
        given()
                .get("/movies")
                .then()
                .statusCode(200)
                .body("content.title", hasItems("The Witcher", "Harry Potter e a Pedra Filosofal"))
                ;
    }
    @Test
    public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty(){
        given()
                .get("/movies?title={titleName}", titleName)
                .then()
                .statusCode(200)
                .body("content.id", hasItems(18))
                .body("content.title", hasItems("Guerra Mundial Z"))
                .body("content.score", hasItem(0.0f))
                .body("content", hasSize(1));
        ;
    }
}

package com.devsuperior.dsmovie.controllers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class MovieControllerRA {

    private String titleName;
    private String existingId, nonExistingId;
    @BeforeEach
    void setUp(){
        titleName = "Guerra Mundial Z";
        baseURI = "http://localhost:8080";

        existingId = "18";
        nonExistingId = "200";
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
    }
    @Test
    public void findByIdShouldReturnMovieWhenIdExists(){
        given()
                .get("/movies/{existingId}", existingId)
                .then()
                .statusCode(200)
                .body("id", is(18))
                .body("title", equalTo("Guerra Mundial Z"))
                .body("score", is(0.0f))
                .body("count", is(0))
                .body("image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/31VpBgUX5O4Z3dn5ZbX8HLqoXH3.jpg"));
    }
    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist(){
        given()
                .get("/movies/{nonExistingId}", nonExistingId)
                .then()
                .statusCode(404)
                .body("path", equalTo("/movies/200"))
                .body("error", equalTo("Recurso não encontrado"));
    }

}

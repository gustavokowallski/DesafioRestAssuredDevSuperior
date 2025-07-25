package com.devsuperior.dsmovie.controllers;


import com.devsuperior.dsmovie.test.TokenUtil;
import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class MovieControllerRA {

    private String titleName;
    private String existingId, nonExistingId;

    private String clientUsername, clientPassword, adminUsername, adminPassword;

    private Map<String, Object> postMovieObject;

    private String clientToken, adminToken, invalidToken;
    @BeforeEach
    void setUp(){
        clientUsername = "maria@gmail.com";
        adminUsername = "alex@gmail.com";

        clientPassword = "123456";
        adminPassword = "123456";

        clientToken = TokenUtil.obtainAcessToken(clientUsername, clientPassword);
        adminToken = TokenUtil.obtainAcessToken(adminUsername, adminPassword);
        invalidToken = adminToken + "xpto";

        titleName = "Guerra Mundial Z";
        baseURI = "http://localhost:8080";

        existingId = "18";
        nonExistingId = "200";

        postMovieObject = new HashMap<>();
        postMovieObject.put("title", "Test Movie");
        postMovieObject.put("score", 0.0);
        postMovieObject.put("count", 0);
        postMovieObject.put("image", "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");
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
    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle(){
        postMovieObject.remove("title");
        JSONObject newProduct = new JSONObject(postMovieObject);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(newProduct)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)

                .when()
                .post("/movies")
                .then()
                .statusCode(422)
                .body("errors.fieldName", hasItems("title"))
                .body("errors", hasSize(1))
                .body("errors.message", hasItem("Campo requerido"));


    }
    @Test
    public void insertShouldReturnForbiddenWhenClientLogged(){

        JSONObject newProduct = new JSONObject(postMovieObject);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
                .body(newProduct)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)

                .when()
                .post("/movies")
                .then()
                .statusCode(403);
    }
    @Test
    public void insertShouldReturnUnauthorizedWhenInvalidToken(){

        JSONObject newProduct = new JSONObject(postMovieObject);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + invalidToken)
                .body(newProduct)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)

                .when()
                .post("/movies")
                .then()
                .statusCode(401);
    }
    }



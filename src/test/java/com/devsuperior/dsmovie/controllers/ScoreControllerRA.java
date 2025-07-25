package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.test.TokenUtil;
import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ScoreControllerRA {
    private String clientUsername, adminUsername;

    private String clientPassword, adminPassword;

    private String clientToken, adminToken, invalidToken;

    private Map<String, Object> postScoreObject;
    private List<Map<String, Object>> movieObj;

    @BeforeEach
    void setUp() {
        clientUsername = "maria@gmail.com";
        adminUsername = "alex@gmail.com";

        clientPassword = "123456";
        adminPassword = "123456";

        clientToken = TokenUtil.obtainAcessToken(clientUsername, clientPassword);
        adminToken = TokenUtil.obtainAcessToken(adminUsername, adminPassword);
        invalidToken = adminToken + "xpto";
        postScoreObject = new HashMap<>();
        postScoreObject.put("movieId", 1);
        postScoreObject.put("score", 5);

        movieObj = new ArrayList<>();

    }
    @Test
    public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist(){
        postScoreObject.put("movieId", 1000);

        JSONObject newScore = new JSONObject(postScoreObject);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
                .body(newScore)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)

                .when()
                .put("/scores")
                .then()
                .statusCode(404);
    }
    @Test
    public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId(){
        postScoreObject.remove("movieId");

        JSONObject newScore = new JSONObject(postScoreObject);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
                .body(newScore)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)

                .when()
                .put("/scores")
                .then()
                .statusCode(422);
    }

    @Test
    public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero(){
        postScoreObject.put("score", -1);

        JSONObject newScore = new JSONObject(postScoreObject);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
                .body(newScore)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)

                .when()
                .put("/scores")
                .then()
                .statusCode(422);
    }
}

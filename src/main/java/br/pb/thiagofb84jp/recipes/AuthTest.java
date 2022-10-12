package br.pb.thiagofb84jp.recipes;

import io.restassured.http.ContentType;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AuthTest {

    @Test
    public void deveAcessarSWAPI() {
        given()
             .log().all()
        .when()
             .get("https://swapi.dev/api/people/1")
        .then()
             .log().all()
             .statusCode(200)
        .and()
             .body("name", is("Luke Skywalker"))
        ;
    }

    @Test
    public void deveObterClima() {
        given()
             .log().all()
             .queryParam("q", "Fortaleza,BR")
             .queryParam("appid", "a941198d66f571ea632bc4d876fe97ea")
             .queryParam("units", "metric")
        .when()
             .get("http://api.openweathermap.org/data/2.5/weather")
        .then()
             .log().all()
             .statusCode(200)
        .and()
             .body("name", is("Fortaleza"))
             .body("coord.lon", is(-38.5247F))
             .body("main.temp", greaterThan(25F))
        ;
    }

    @Test
    public void naoDeveAcessarSemSenha() {
        given()
             .log().all()
        .when()
             .get("https://restapi.wcaquino.me/basicauth")
        .then()
             .log().all()
             .statusCode(401)
        .and()
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasica() {
        given()
             .log().all()
        .when()
             .get("https://admin:senha@restapi.wcaquino.me/basicauth")
        .then()
             .log().all()
             .statusCode(200)
        .and()
             .body("status", is("logado"))
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasica2() {
        given()
             .log().all()
             .auth().basic("admin", "senha")
        .when()
             .get("https://restapi.wcaquino.me/basicauth")
        .then()
             .log().all()
             .statusCode(200)
        .and()
             .body("status", is("logado"))
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasicaChallenge() {
        given()
             .log().all()
             .auth().preemptive().basic("admin", "senha")
        .when()
             .get("https://restapi.wcaquino.me/basicauth2")
        .then()
             .log().all()
             .statusCode(200)
        .and()
             .body("status", is("logado"))
        ;
    }

    @Test
    public void deveFazerAutorizacaoComTokenJWT() {
        // Login na API
        Map<String, String> login = new HashMap<String, String>();
        login.put("email", "thiago.ferreira@gmail.com");
        login.put("senha", "abcd_123");

        // Recebendo o Token
        String token = given()
                            .log().all()
                            .body(login)
                            .contentType(ContentType.JSON)
                       .when()
                            .post("https://barrigarest.wcaquino.me/signin")
                       .then()
                            .log().all()
                            .statusCode(200)
                       .and()
                            .extract().path("token")
        ;

        // Obtendo as contas
        given()
             .log().all()
             .header("Authorization", "JWT " +  token)
        .when()
             .get("https://barrigarest.wcaquino.me/contas")
        .then()
             .log().all()
             .statusCode(200)
             .body("nome", hasItem("Conta 1020"))
        ;
    }
}

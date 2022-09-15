package br.pb.thiagofb84jp.rest.tests;

import br.pb.thiagofb84jp.rest.core.BaseTest;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BarrigaTest extends BaseTest {

    private String TOKEN;

    @Before
    public void login() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "thiago.ferreira@gmail.com");
        login.put("senha", "abcd_123");

        TOKEN = given()
                     .body(login)
                .when()
                     .post("/signin")
                .then()
                     .statusCode(200)
                     .extract().path("token")
                ;
    }

    @Test
    public void naoDeveAcessarAPISemToken() {
        given()
        .when()
             .get("/contas")
        .then()
             .statusCode(401)
        ;
    }

    @Test
    public void deveIncluirContaComSucesso() {
        Faker faker = new Faker();
        int number = faker.number().numberBetween(1, 500);

        given()
             .header("Authorization", "JWT " + TOKEN)
             .body("{\"nome\": \"Conta " +  number +"\"}")
        .when()
             .post("/contas")
        .then()
             .statusCode(201)
        ;
    }

    @Test
    public void deveAlterarContaComSucesso() {
        given()
             .header("Authorization", "JWT " + TOKEN)
             .body("{\"nome\": \"Conta alterada\"}")
        .when()
             .put("/contas/1381328")
        .then()
             .statusCode(200)
        .and()
             .body("nome", is("Conta alterada"))
        ;
    }
}
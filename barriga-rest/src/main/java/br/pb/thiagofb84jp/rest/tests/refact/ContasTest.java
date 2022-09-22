package br.pb.thiagofb84jp.rest.tests.refact;

import br.pb.thiagofb84jp.rest.core.BaseTest;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.Matchers.is;

public class ContasTest extends BaseTest {

    @BeforeClass
    public static void login() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "thiago.ferreira@gmail.com");
        login.put("senha", "abcd_123");

        String TOKEN = given()
                            .body(login)
                       .when()
                            .post("/signin")
                       .then()
                            .statusCode(200)
                            .extract().path("token")
        ;

        requestSpecification.header("Authorization", "JWT " + TOKEN);
        get("/reset").then().statusCode(200);
    }

    @Test
    public void deveIncluirContaComSucesso() {
        Faker faker = new Faker();
        String nomeConta = "Conta "+ faker.funnyName().name();

        given()
             .body("{\"nome\": \""+ nomeConta +"\"}")
        .when()
             .post("/contas")
        .then()
             .statusCode(201)
        ;
    }

    @Test
    public void deveAlterarContaComSucesso() {
        Faker faker = new Faker();
        String nomeConta = "Conta "+ faker.funnyName().name();

        given()
             .body("{\"nome\": \""+ nomeConta +" alterada\"}")
             .pathParam("id", CONTA_ID)
        .when()
             .put("/contas/{id}")
        .then()
             .statusCode(200)
        .and()
             .body("nome", is(CONTA_NAME +" alterada"))
        ;
    }


}

package br.pb.thiagofb84jp.rest.tests.refact;

import br.pb.thiagofb84jp.rest.core.BaseTest;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static br.pb.thiagofb84jp.rest.utils.BarrigaUtils.getIdContaPeloNome;
import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.Matchers.is;

public class ContasTest extends BaseTest {

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
        Integer CONTA_ID = getIdContaPeloNome("Conta para alterar");

        given()
             .body("{\"nome\": \"Conta alterada\"}")
             .pathParam("id", CONTA_ID)
        .when()
             .put("/contas/{id}")
        .then()
             .statusCode(200)
        .and()
             .body("nome", is("Conta alterada"))
        ;
    }

    @Test
    public void naoDeveInserirContaMesmoNome() {
        given()
             .body("{\"nome\": \"Conta mesmo nome\"}")
        .when()
             .post("/contas")
        .then()
             .statusCode(400)
        .and()
             .body("error", is("Já existe uma conta com esse nome!"))
        ;
    }

}

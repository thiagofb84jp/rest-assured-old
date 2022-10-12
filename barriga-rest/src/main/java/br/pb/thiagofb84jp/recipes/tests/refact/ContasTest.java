package br.pb.thiagofb84jp.recipes.tests.refact;

import br.pb.thiagofb84jp.recipes.core.BaseTest;
import com.github.javafaker.Faker;
import org.junit.Test;

import static br.pb.thiagofb84jp.recipes.utils.BarrigaUtils.getIdContaPeloNome;
import static io.restassured.RestAssured.given;
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

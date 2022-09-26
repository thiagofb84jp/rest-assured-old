package br.pb.thiagofb84jp.rest.tests.refact;

import br.pb.thiagofb84jp.rest.core.BaseTest;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

public class SaldoTest extends BaseTest {

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
    public void deveCalcularSaldoContas() {
        Integer CONTA_ID = getIdContaPeloNome("Conta para saldo");

        given()
        .when()
             .get("/saldo")
        .then()
             .statusCode(200)
        .and()
             .body("find{it.conta_id == "+ CONTA_ID +"}.saldo", is("534.00"))
        ;
    }

    public Integer getIdContaPeloNome(String nome) {
        return RestAssured.get("/contas?nome=" + nome).then().extract().path("id[0]");
    }

}

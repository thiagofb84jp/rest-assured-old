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
import static org.hamcrest.Matchers.is;

public class SaldoTest extends BaseTest {

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

}

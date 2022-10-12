package br.pb.thiagofb84jp.recipes.tests.refact;

import br.pb.thiagofb84jp.recipes.core.BaseTest;
import org.junit.Test;

import static br.pb.thiagofb84jp.recipes.utils.BarrigaUtils.getIdContaPeloNome;
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

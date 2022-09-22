package br.pb.thiagofb84jp.rest.tests;

import br.pb.thiagofb84jp.rest.core.BaseTest;
import br.pb.thiagofb84jp.rest.utils.DataUtils;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.manipulation.Filterable;
import org.junit.runners.MethodSorters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTest extends BaseTest {

    private static String TOKEN;
    private static String CONTA_NAME = "Conta " + System.nanoTime();
    private static Integer CONTA_ID;
    private static Integer MOV_ID;

    @BeforeClass
    public static void login() {
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

        requestSpecification.header("Authorization", "JWT " + TOKEN);
    }

    @Test
    public void t01_deveIncluirContaComSucesso() {
        CONTA_ID = given()
                        .body("{\"nome\": \""+ CONTA_NAME +"\"}")
                   .when()
                        .post("/contas")
                   .then()
                        .statusCode(201)
                   .and()
                        .extract().path("id")
        ;
    }

    @Test
    public void t02_deveAlterarContaComSucesso() {
        given()
             .body("{\"nome\": \""+ CONTA_NAME +" alterada\"}")
             .pathParam("id", CONTA_ID)
        .when()
             .put("/contas/{id}")
        .then()
             .statusCode(200)
        .and()
             .body("nome", is(CONTA_NAME +" alterada"))
        ;
    }

    @Test
    public void t03_naoDeveInserirContaMesmoNome() {
        given()
             .body("{\"nome\": \""+ CONTA_NAME +" alterada\"}")
        .when()
             .post("/contas")
        .then()
             .statusCode(400)
        .and()
             .body("error", is("Já existe uma conta com esse nome!"))
        ;
    }

    @Test
    public void t04_deveInserirMovimentacaoComSucesso() {
        Movimentacao mov = getMovimentacaoValida();

        MOV_ID = given()
                      .body(mov)
                 .when()
                      .post("/transacoes")
                 .then()
                      .statusCode(201)
                      .extract().path("id")
        ;
    }

    @Test
    public void t05_deveValidarCamposObrigatoriosMovimentacao() {
        given()
             .body("{}")
        .when()
             .post("/transacoes")
        .then()
             .statusCode(400)
        .and()
             .body("$", hasSize(8))
             .body("msg", hasItems(
                     "Data da Movimentação é obrigatório",
                     "Data do pagamento é obrigatório",
                     "Descrição é obrigatório",
                     "Interessado é obrigatório",
                     "Valor é obrigatório",
                     "Valor deve ser um número",
                     "Conta é obrigatório",
                     "Situação é obrigatório"
             ))
        ;
    }

    @Test
    public void t06_deveInserirMovimentacaoComDataFutura() {
        Movimentacao mov = getMovimentacaoValida();
        mov.setData_transacao(DataUtils.getDataDiferencaDias(2));

        given()
             .body(mov)
        .when()
             .post("/transacoes")
        .then()
             .statusCode(400)
        .and()
             .body("$", hasSize(1))
             .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
        ;
    }

    @Test
    public void t07_naoDeveRemoverContaComMovimentacao() {
        given()
             .pathParam("id", CONTA_ID)
        .when()
             .delete("/contas/{id}")
        .then()
             .statusCode(500)
        .and()
             .body("constraint", is("transacoes_conta_id_foreign"))
        ;
    }

    @Test
    public void t08_deveCalcularSaldoContas() {
        given()
        .when()
             .get("/saldo")
        .then()
             .statusCode(200)
        .and()
//             .body("find{it.conta_id == 1381328}.saldo", is("300.00"))
             .body("find{it.conta_id == "+ CONTA_ID +"}.saldo", is("100.00"))
        ;
    }

    @Test
    public void t09_deveRemoverMovimentacao() {
        given()
             .pathParam("id", MOV_ID)
        .when()
             .delete("/transacoes/{id}")
        .then()
             .statusCode(204)
        ;
    }

    @Test
    public void t10_naoDeveAcessarAPISemToken() {
        FilterableRequestSpecification req = (FilterableRequestSpecification) requestSpecification;
        req.removeHeader("Authorization");

        given()
        .when()
             .get("/contas")
        .then()
             .statusCode(401)
        ;
    }

    /*
    *   Generating mass of data
     */
    private Movimentacao getMovimentacaoValida() {
        Movimentacao mov = new Movimentacao();

        mov.setConta_id(CONTA_ID);
//        mov.setUsuarioId();
        mov.setDescricao("Descrição da movimentação");
        mov.setEnvolvido("Envolvido na mov");
        mov.setTipo("REC");
        mov.setData_transacao(DataUtils.getDataDiferencaDias(-1));
        mov.setData_pagamento(DataUtils.getDataDiferencaDias(5));
        mov.setValor(100F);
        mov.setStatus(true);

        return mov;
    }

}
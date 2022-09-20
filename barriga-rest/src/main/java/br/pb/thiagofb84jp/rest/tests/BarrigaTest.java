package br.pb.thiagofb84jp.rest.tests;

import br.pb.thiagofb84jp.rest.core.BaseTest;
import br.pb.thiagofb84jp.rest.utils.DataUtils;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
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

    private String TOKEN;

    private static String CONTA_NAME = "Conta " + System.nanoTime();

    private static Integer CONTA_ID;

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
    public void t01_naoDeveAcessarAPISemToken() {
        given()
        .when()
             .get("/contas")
        .then()
             .statusCode(401)
        ;
    }

    @Test
    public void t02_deveIncluirContaComSucesso() {
        CONTA_ID = given()
                        .header("Authorization", "JWT " + TOKEN)
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
    public void t03_deveAlterarContaComSucesso() {
        given()
             .header("Authorization", "JWT " + TOKEN)
             .body("{\"nome\": \""+ CONTA_NAME +" alterada\"}")
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
    public void t04_naoDeveInserirContaMesmoNome() {
        given()
             .header("Authorization", "JWT " + TOKEN)
             .body("{\"nome\": \""+ CONTA_NAME +"  alterada\"}")
        .when()
             .post("/contas")
        .then()
             .statusCode(400)
        .and()
             .body("error", is("Já existe uma conta com esse nome!"))
        ;
    }

    @Test
    public void t05_deveInserirMovimentacaoComSucesso() {
        Movimentacao mov = getMovimentacaoValida();

        given()
             .header("Authorization", "JWT " + TOKEN)
             .body(mov)
        .when()
             .post("/transacoes")
        .then()
             .statusCode(201)
        ;
    }

    @Test
    public void t06_deveValidarCamposObrigatoriosMovimentacao() {
        given()
             .header("Authorization", "JWT " + TOKEN)
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
    public void t07_deveInserirMovimentacaoComDataFutura() {
        Movimentacao mov = getMovimentacaoValida();
        mov.setData_transacao(DataUtils.getDataDiferencaDias(2));

        given()
             .header("Authorization", "JWT " + TOKEN)
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
    public void t08_naoDeveRemoverContaComMovimentacao() {
        given()
             .header("Authorization", "JWT " + TOKEN)
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
    public void t09_deveCalcularSaldoContas() {
        given()
             .header("Authorization", "JWT " + TOKEN)
        .when()
             .get("/saldo")
        .then()
             .statusCode(200)
        .and()
             .body("find{it.conta_id == 1381328}.saldo", is("300.00"))
        ;
    }

    @Test
    public void t10_deveRemoverMovimentacao() {
        given()
             .header("Authorization", "JWT " + TOKEN)
        .when()
             .delete("/transacoes/1293357")
        .then()
             .statusCode(204)
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
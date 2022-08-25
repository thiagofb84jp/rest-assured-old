package br.pb.thiagofb84jp;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class FileTest {

    @Test
    public void deveObrigarEnvioArquivo() {
        given()
             .log().all()
        .when()
             .post("http://restapi.wcaquino.me/upload")
        .then()
             .log().all()
             .statusCode(404)
        .and()
             .body("error", is("Arquivo não enviado"))
        ;
    }

    @Test
    public void deveFazerUploadDoArquivo() {
        given()
             .log().all()
             .multiPart("arquivo", new File("src/main/resources/users.pdf"))
        .when()
             .post("http://restapi.wcaquino.me/upload")
        .then()
             .log().all()
             .statusCode(200)
        .and()
             .body("name", is("users.pdf"))
             .body("md5", is(notNullValue()))
             .body("size", is(notNullValue()))
        ;
    }

    @Test
    public void naoDeveFazerUploadArquivoGrande() {
        given()
             .log().all()
             .multiPart("arquivo", new File("src/main/resources/heavyFile.ps"))
        .when()
             .post("http://restapi.wcaquino.me/upload")
        .then()
             .log().all()
             .time(lessThan(5000L))
             .statusCode(413)
        ;
    }

    @Test
    public void deveBaixarArquivo() throws IOException {
        byte[] image = given()
                            .log().all()
                       .when()
                            .get("http://restapi.wcaquino.me/download")
                       .then()
                            .statusCode(200)
                       .and()
                            .extract().asByteArray()
                       ;
        File imagem = new File("src/main/resources/file.jpg");
        OutputStream outputStream = new FileOutputStream(imagem);
        outputStream.write(image);
        outputStream.close();

        Assert.assertThat(imagem.length(), lessThan(100000L));
    }
}

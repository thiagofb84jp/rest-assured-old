package br.pb.thiagofb84jp.rest.dummyJson.tests;

import br.pb.thiagofb84jp.rest.dummyJson.core.BaseTest;
import br.pb.thiagofb84jp.rest.dummyJson.dto.Product;
import com.github.javafaker.Faker;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProductsTest extends BaseTest {

    @Test
    public void createProduct() {
        Product product = getValidProduct();

        given()
             .body(product)
        .when()
             .post("/products/add")
        .then()
             .statusCode(200)
        .and()
             .body("id", is(notNullValue()))
             .body("title", is(product.getTitle()))
             .body("description", is(product.getDescription()))
             .body("price", is(product.getPrice()))
             .body("rating", is(product.getRating()))
             .body("stock", is(product.getStock()))
             .body("brand", is(product.getBrand()))
             .body("category", is(product.getCategory()))
             .body("thumbnail", is(product.getThumbnail()))
             .body("images", is(product.getImages()))
        ;
    }

    @Test
    public void updateProduct() {
        Product product = getValidProduct();

        given()
             .pathParam("id", 1)
             .body(product)
        .when()
             .put("/products/{id}")
        .then()
             .statusCode(200)
        .and()
             .body("id", is("1"))
             .body("title", is(product.getTitle()))
             .body("description", is(product.getDescription()))
             .body("price", is(product.getPrice()))
             .body("rating", is(product.getRating()))
             .body("stock", is(product.getStock()))
             .body("brand", is(product.getBrand()))
             .body("category", is(product.getCategory()))
             .body("thumbnail", is(product.getThumbnail()))
             .body("images", is(product.getImages()))
        ;
    }

    @Test
    public void getSingleProduct() {
        given()
             .pathParam("id", 38)
        .when()
             .get("/products/{id}")
        .then()
             .statusCode(200)
        .and()
             .body("id", is(38))
             .body("title", is("sublimation plain kids tank"))
             .body("description", is("sublimation plain kids tank tops wholesale"))
             .body("price", is(100))
             .body("discountPercentage", is(11.12F))
             .body("rating", is(4.8F))
             .body("stock", is(20))
             .body("brand", is("Soft Cotton"))
             .body("category", is("tops"))
             .body("thumbnail", is("https://dummyjson.com/image/i/products/38/thumbnail.jpg"))
             .body("images", hasItems("https://dummyjson.com/image/i/products/38/1.png",
                                         "https://dummyjson.com/image/i/products/38/2.jpg",
                                         "https://dummyjson.com/image/i/products/38/3.jpg",
                                         "https://dummyjson.com/image/i/products/38/4.jpg"))
             .body("images", hasSize(4))
        ;
    }

    private static Product getValidProduct() {
        Product product = new Product();
        product.setTitle("iPhone 14 XMas Pro Ultimate Generation");
        product.setDescription("This is the new iPhone 14 XMas Pro Ultimate Generation");
        product.setPrice(4009.99F);
        product.setRating(4.11F);
        product.setStock(100);
        product.setBrand("YIOSI");
        product.setCategory("smartphones");
        product.setThumbnail("https://dummyjson.com/image/i/products/100/thumbnail.jpg");

        ArrayList<String> images = new ArrayList<String>();
        images.add("https://dummyjson.com/image/i/products/100/1.jpg");
        images.add("https://dummyjson.com/image/i/products/100/2.jpg");
        images.add("https://dummyjson.com/image/i/products/100/3.jpg");
        images.add("https://dummyjson.com/image/i/products/100/thumbnail.jpg.jpg");
        product.setImages(images);

        return product;
    }

    private static Product updateValidProduct() {
        Product product = new Product();
        product.setTitle("iPhone 14 XMas Pro Ultimate Generation");

        return product;
    }

}

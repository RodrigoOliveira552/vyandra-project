package br.com.vyandra.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class LoginApiTest {

    private static Faker faker;
    private static String emailCriado;
    private static String senhaCriada = "senhaApi123";

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://serverest.dev";
        faker = new Faker(new Locale("pt", "BR"));
        emailCriado = faker.internet().emailAddress();

        // Antes de testar o login, precisamos ter um usuário no banco
        String jsonBody = "{\n" +
                "  \"nome\": \"" + faker.name().fullName() + "\",\n" +
                "  \"email\": \"" + emailCriado + "\",\n" +
                "  \"password\": \"" + senhaCriada + "\",\n" +
                "  \"administrador\": \"true\"\n" +
                "}";

        given().contentType(ContentType.JSON).body(jsonBody).post("/usuarios");
    }

    @Test
    @DisplayName("Deve realizar login via API e capturar o Token de Autorização (Bearer Token)")
    public void testLoginViaApi() {
        String loginBody = "{\n" +
                "  \"email\": \"" + emailCriado + "\",\n" +
                "  \"password\": \"" + senhaCriada + "\"\n" +
                "}";

        System.out.println("Tentando logar com: " + emailCriado);

        given()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(200)
                .body("authorization", notNullValue());
    }
}
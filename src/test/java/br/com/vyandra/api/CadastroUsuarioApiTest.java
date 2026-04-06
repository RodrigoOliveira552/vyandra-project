package br.com.vyandra.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CadastroUsuarioApiTest {

    private static Faker faker;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://serverest.dev";
        faker = new Faker(new Locale("pt", "BR"));
    }

    @Test
    @DisplayName("Deve cadastrar um usuário via API e retornar Status 201")
    public void testCadastrarUsuarioViaApi() {
        String jsonBody = "{\n" +
                "  \"nome\": \"" + faker.name().fullName() + "\",\n" +
                "  \"email\": \"" + faker.internet().emailAddress() + "\",\n" +
                "  \"password\": \"senha123\",\n" +
                "  \"administrador\": \"true\"\n" +
                "}";

        System.out.println("🚀 Enviando Payload para a API:\n" + jsonBody);

        given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/usuarios")
                .then()
                .log().all()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .body("_id", notNullValue());
    }
}
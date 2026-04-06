package br.com.vyandra.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioCrudApiTest {

    private static Faker faker;
    private static String idUsuario;
    private static String emailUsuario;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://serverest.dev";
        faker = new Faker(new Locale("pt", "BR"));
        emailUsuario = faker.internet().emailAddress();
    }

    @Test
    @Order(1)
    @DisplayName("1. CREATE - Deve criar um usuário e extrair o ID")
    public void testCriarUsuario() {
        String jsonBody = "{\n" +
                "  \"nome\": \"" + faker.name().fullName() + "\",\n" +
                "  \"email\": \"" + emailUsuario + "\",\n" +
                "  \"password\": \"senha123\",\n" +
                "  \"administrador\": \"true\"\n" +
                "}";

        idUsuario = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().path("_id");

        System.out.println("Usuário CRIADO com ID: " + idUsuario);
    }

    @Test
    @Order(2)
    @DisplayName("2. READ - Deve consultar o usuário criado usando o ID")
    public void testConsultarUsuario() {
        given()
                .pathParam("_id", idUsuario) // Injeta o ID na URL
                .when()
                .get("/usuarios/{_id}")
                .then()
                .statusCode(200)
                .body("email", equalTo(emailUsuario)) // Garante que é o e-mail exato que criamos
                .body("_id", equalTo(idUsuario));

        System.out.println("🔍 Usuário CONSULTADO com sucesso.");
    }

    @Test
    @Order(3)
    @DisplayName("3. UPDATE - Deve atualizar o nome do usuário")
    public void testAtualizarUsuario() {
        String novoNome = "Engenheiro " + faker.name().lastName();

        String jsonBodyAtualizado = "{\n" +
                "  \"nome\": \"" + novoNome + "\",\n" +
                "  \"email\": \"" + emailUsuario + "\",\n" +
                "  \"password\": \"senhaNova123\",\n" +
                "  \"administrador\": \"true\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .pathParam("_id", idUsuario)
                .body(jsonBodyAtualizado)
                .when()
                .put("/usuarios/{_id}")
                .then()
                .statusCode(200)
                .body("message", equalTo("Registro alterado com sucesso"));

        System.out.println("✏️ Usuário ATUALIZADO para: " + novoNome);
    }

    @Test
    @Order(4)
    @DisplayName("4. DELETE - Deve deletar o usuário para manter o banco limpo")
    public void testDeletarUsuario() {
        given()
                .pathParam("_id", idUsuario)
                .when()
                .delete("/usuarios/{_id}")
                .then()
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"));

        System.out.println("🗑️ Usuário DELETADO. Banco de dados limpo.");
    }
}
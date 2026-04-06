package br.com.vyandra.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioCrudApiTest {

    private static Faker faker;
    private static String idUsuario;
    private static String emailUsuario;

    private static String idManual = "";

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://serverest.dev";
        faker = new Faker(new Locale("pt", "BR"));
        emailUsuario = faker.internet().emailAddress();
    }

    private String obterIdParaUso() {
        if (idUsuario != null) return idUsuario;
        if (!idManual.isEmpty()) return idManual;

        return given().get("/usuarios").then().extract().path("usuarios[0]._id");
    }

    @Test
    @Order(0)
    @DisplayName("0. MASSA - Buscar 2 Admins e 2 Comuns no banco (Para copiar o ID)")
    public void testBuscarMassaDeDados() {
        System.out.println("\n" + "=".repeat(20));
        System.out.println("MASSA DE DADOS DISPONIVEIS ENCONTRADAS");
        System.out.println("=".repeat(20));

        imprimirUsuariosDaBase("true", "ADMINISTRADOR", 2);
        imprimirUsuariosDaBase("false", "COMUM", 2);

        System.out.println("=".repeat(20));
    }


    private void imprimirUsuariosDaBase(String ehAdmin, String tituloPerfil, int limiteDesejado) {
        System.out.println("\n>>> PERFIL: " + tituloPerfil);

        io.restassured.response.Response response = given()
                .queryParam("administrador", ehAdmin)
                .when()
                .get("/usuarios");

        int quantidadeReal = response.path("quantidade");
        int totalParaExibir = Math.min(limiteDesejado, quantidadeReal);

        if (totalParaExibir == 0) {
            System.out.println("  NENHUM usuário encontrado para este perfil.");
            return;
        }

        for (int i = 0; i < totalParaExibir; i++) {
            String nome = response.path("usuarios[" + i + "].nome");
            String email = response.path("usuarios[" + i + "].email");
            String id = response.path("usuarios[" + i + "]._id");

            System.out.println("  Opção " + (i + 1) + " | Nome: " + nome + " | Email: " + email);
            System.out.println("  ID PARA COPIAR: " + id);
            System.out.println("  -".repeat(10));
        }
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

        System.out.println("\n[CRIANDO USUÁRIO] Dados enviados e resposta da API:");
        idUsuario = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/usuarios")
                .then()
                .log().all()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().path("_id");
    }

    @Test
    @Order(2)
    @DisplayName("2. READ - Deve consultar o usuário pelo ID")
    public void testConsultarUsuario() {
        String idAlvo = obterIdParaUso();
        System.out.println("\n[CONSULTANDO USUÁRIO] ID: " + idAlvo);
        given()
                .pathParam("_id", idAlvo)
                .when()
                .get("/usuarios/{_id}")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test
    @Order(3)
    @DisplayName("3. UPDATE - Deve atualizar o nome do usuário")
    public void testAtualizarUsuario() {
        String idAlvo = obterIdParaUso();
        String novoNome = "Alterado " + faker.name().lastName();

        String jsonBodyAtualizado = "{\n" +
                "  \"nome\": \"" + novoNome + "\",\n" +
                "  \"email\": \"" + faker.internet().emailAddress() + "\",\n" +
                "  \"password\": \"senhaNova123\",\n" +
                "  \"administrador\": \"true\"\n" +
                "}";

        System.out.println("\n[ATUALIZANDO USUÁRIO] ID: " + idAlvo + " | Novo Nome: " + novoNome);
        given()
                .contentType(ContentType.JSON)
                .pathParam("_id", idAlvo)
                .body(jsonBodyAtualizado)
                .when()
                .put("/usuarios/{_id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("message", equalTo("Registro alterado com sucesso"));
    }

    @Test
    @Order(4)
    @DisplayName("4. DELETE - Deve deletar o usuário para manter o banco limpo")
    public void testDeletarUsuario() {
        String idAlvo = obterIdParaUso();

        System.out.println("\n[DELETANDO USUÁRIO] ID: " + idAlvo);
        given()
                .pathParam("_id", idAlvo)
                .when()
                .delete("/usuarios/{_id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"));
    }
}
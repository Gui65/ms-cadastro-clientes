package br.com.cadastrocliente.mscadastrocliente.application.controller;

import br.com.cadastrocliente.mscadastrocliente.domain.entity.Cliente;
import br.com.cadastrocliente.mscadastrocliente.domain.service.ClienteService;
import br.com.cadastrocliente.mscadastrocliente.domain.valueObject.Endereco;
import br.com.cadastrocliente.mscadastrocliente.infra.repository.ClienteRespository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClienteControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRespository clienteRespository;


    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        List<Cliente> clientes = getClientes();

        clienteRespository.saveAll(clientes);
    }

    private List<Cliente> getClientes() {
        // Cliente 1
        Cliente cliente1 = new Cliente(
                1L,
                "Guilherme Matos de Carvalho",
                "8Xa5I@example.com",
                "Sdsadwd21321@#$",
                "11923465432",
                "12345678910",
                "147852369",
                new Endereco(
                        "147852369",
                        "Rua A",
                        "Casa",
                        "Centro",
                        "São Paulo",
                        "SP",
                        "189",
                        1111.54551,
                        111122.226
                )
        );

        // Cliente 2
        Cliente cliente2 = new Cliente(
                2L,
                "Fulano de Tal",
                "fulano@example.com",
                "senha123",
                "987654321",
                "11987654321",
                "987654321",
                new Endereco(
                        "987654321",
                        "Rua B",
                        "Apartamento",
                        "Bairro Novo",
                        "Rio de Janeiro",
                        "RJ",
                        "876",
                        2222.98765,
                        333333.333
                )
        );

        // Cliente 3
        Cliente cliente3 = new Cliente(
                3L,
                "Ciclano da Silva",
                "ciclano@example.com",
                "senha456",
                "369258147",
                "11987654321",
                "369258147",
                new Endereco(
                        "369258147",
                        "Rua C",
                        "Sobrado",
                        "Centro",
                        "Curitiba",
                        "PR",
                        "369",
                        4444.12345,
                        555555.555
                )
        );

        // Cliente 4
        Cliente cliente4 = new Cliente(
                4L,
                "Beltrano Oliveira",
                "beltrano@example.com",
                "senha789",
                "654123987",
                "11987654321",
                "654123987",
                new Endereco(
                        "654123987",
                        "Rua D",
                        "Casa",
                        "Periferia",
                        "Salvador",
                        "BA",
                        "123",
                        6666.78901,
                        777777.777
                )
        );

        // Cliente 5
        Cliente cliente5 = new Cliente(
                5L,
                "Maria Souza",
                "maria@example.com",
                "senhaabc",
                "852147369",
                "11987654321",
                "852147369",
                new Endereco(
                        "852147369",
                        "Rua E",
                        "Apartamento",
                        "Praia",
                        "Fortaleza",
                        "CE",
                        "987",
                        8888.13579,
                        999999.999
                )
        );

        return List.of(cliente1, cliente2, cliente3, cliente4, cliente5);
    }

    @AfterEach
    void dropDatabase() {
        System.out.println("Dropping database");
        clienteRespository.deleteAll();
    }

    @Nested
    class ObterClientes{

        @Test
        void obterTodos() {

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/cliente")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(matchesJsonSchemaInClasspath("schemas/cliente-list.schema.json"));
        }

        @Test
        void deveObterVazio() {
            dropDatabase();
            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/cliente")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", equalTo(0));
        }

        @Test
        void obterPorId() {
            var id = 1L;

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/cliente/buscar/{id}", id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(matchesJsonSchemaInClasspath("schemas/cliente.schema.json"));
        }

        @Test
        void deveGerarExcessaoQuandoIdNaoEncontrado() {
            var id = 6L;

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/cliente/buscar/{id}", id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                    .body("error", equalTo("Nao Encontrado Exception"))
                    .body("status", equalTo(404))
                    .body("message", equalTo(
                            String.format("Cliente com o id '%d' não encontrado", id)));
        }
    }
}
package br.com.cadastrocliente.mscadastrocliente.bdd;

import br.com.cadastrocliente.mscadastrocliente.application.request.ClienteRequestDTO;
import br.com.cadastrocliente.mscadastrocliente.application.response.ClienteResponseDTO;
import br.com.cadastrocliente.mscadastrocliente.domain.valueObject.Endereco;
import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.java.fr.Quand;
import io.cucumber.java.it.Quando;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class StepDefinitionCliente {
    private Response response;
    private ClienteResponseDTO clienteResponseDTO;
    private List<ClienteResponseDTO> clienteResponseDTOList;
    private final String ENDPOINT_API_CLIENTE = "http://localhost:8080/cliente";

    @Quando("cadastar um novo cliente")
    public ClienteResponseDTO cadastrar_um_novo_cliente() {
        var clienteRequest = getClienteRequest();
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(clienteRequest)
                .when()
                .post(ENDPOINT_API_CLIENTE + "/cadastrar");

        return response.then().extract().as(ClienteResponseDTO.class);
    }

    @Entao("o cliente e cadastrado com sucesso")
    public void o_cliente_e_cadastrado_com_sucesso() {
        response.then().statusCode(HttpStatus.CREATED.value());
    }

    @Entao("deve ser apresentado")
    public void deve_ser_apresentado() {
        response.then()
                .body("id", notNullValue());
    }

    @Dado("que um cliente foi cadastrado")
    public void que_um_cliente_foi_cadastrado() {
        clienteResponseDTO = cadastrar_um_novo_cliente();
    }

    @Quando("eu realizar uma busca de todos os clientes")
    public void eu_realizar_uma_busca_de_todos_os_clientes() {
        response = given()
                .when()
                .get(ENDPOINT_API_CLIENTE + "/listar");

        clienteResponseDTOList = new ArrayList<>(response.then().extract()
                .as(new TypeReference<List<ClienteResponseDTO>>() {

                }.getType()));
    }

    @Entao("a lista de clientes deve ser apresentada com sucesso")
    public void a_lista_de_clientes_deve_ser_apresentada_com_sucesso() {
        response.then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("size()", notNullValue())
                .body("[0].id", equalTo(clienteResponseDTOList.get(0).id()))
                .body("[0].nome", equalTo(clienteResponseDTOList.get(0).nome()))
                .body("[0].email", equalTo(clienteResponseDTOList.get(0).email()))
                .body("[0].cpf", equalTo(clienteResponseDTOList.get(0).cpf()))
                .body("[0].telefone", equalTo(clienteResponseDTOList.get(0).telefone()))
                .body("[0].endereco.cep", equalTo(clienteResponseDTOList.get(0).endereco().getCep()))
                .body("[0].endereco.logradouro", equalTo(clienteResponseDTOList.get(0).endereco().getLogradouro()))
                .body("[0].endereco.bairro", equalTo(clienteResponseDTOList.get(0).endereco().getBairro()))
                .body("[0].endereco.cidade", equalTo(clienteResponseDTOList.get(0).endereco().getCidade()))
                .body("[0].endereco.uf", equalTo(clienteResponseDTOList.get(0).endereco().getUf()))
                .body("[0].endereco.numero", equalTo(clienteResponseDTOList.get(0).endereco().getNumero()))
                .body("[0].endereco.complemento", equalTo(clienteResponseDTOList.get(0).endereco().getComplemento()))
                .body("[0].endereco.latitude", equalTo(clienteResponseDTOList.get(0).endereco().getLatitude()))
                .body("[0].endereco.longitude", equalTo(clienteResponseDTOList.get(0).endereco().getLongitude()));


    }


    private ClienteRequestDTO getClienteRequest() {
        return new ClienteRequestDTO(
                1L,
                "Guilherme Matos de Carvalho",
                "8Xa5I@example.com",
                "Sdsadwd21321@#$",
                "11987654321",
                "984551845456",
                "147852369",
                new Endereco(
                        "147852369",
                        "123",
                        "Casa",
                        "Centro",
                        "São Paulo",
                        "SP",
                        "12345678",
                        1111.54551,
                        111122.226
                )
        );
    }
}

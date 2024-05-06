package br.com.cadastrocliente.mscadastrocliente.domain.service;

import br.com.cadastrocliente.mscadastrocliente.application.request.ClienteRequestDTO;
import br.com.cadastrocliente.mscadastrocliente.application.response.ClienteResponseDTO;
import br.com.cadastrocliente.mscadastrocliente.domain.entity.Cliente;
import br.com.cadastrocliente.mscadastrocliente.infra.repository.ClienteRespository;
import br.com.cadastrocliente.mscadastrocliente.utils.Utils;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRespository clienteRespository;

   private final Utils utils;

    public ClienteService(ClienteRespository clienteRespository, Utils utils) {
        this.clienteRespository = clienteRespository;
        this.utils = utils;
    }

    public ClienteResponseDTO cadastrarCliente(ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = toEntity(clienteRequestDTO);

        return toResponseDTO(clienteRespository.save(cliente));
    }

    public Cliente obterClientePorId(Long id) {
        //ALTERAR EXCEPTION
        return clienteRespository.findById(id).orElse(null);
    }

    public ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO clienteRequestDTO) {

        Cliente cliente = obterClientePorId(id);

        utils.copyNonNullProperties(clienteRequestDTO, cliente);

        return toResponseDTO(clienteRespository.save(cliente));

    }

    public void deletarCliente(Long id) {
        clienteRespository.deleteById(id);
    }

    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getTelefone(),
                cliente.getEmail(),
                cliente.getEndereco()
        );
    }

    public Cliente toEntity(ClienteRequestDTO clienteRequestDTO) {
        return Cliente.builder()
                .id(clienteRequestDTO.id())
                .nome(clienteRequestDTO.nome())
                .cpf(clienteRequestDTO.cpf())
                .rg(clienteRequestDTO.rg())
                .email(clienteRequestDTO.email())
                .senha(clienteRequestDTO.senha())
                .telefone(clienteRequestDTO.telefone())
                .endereco(clienteRequestDTO.endereco())
                .build();
    }

}

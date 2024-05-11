package br.com.cadastrocliente.mscadastrocliente.domain.entity;

import br.com.cadastrocliente.mscadastrocliente.domain.valueObject.Endereco;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "TB_CLIENTE")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String cpf;
    private String rg;

    @Embedded
    private Endereco endereco;


}

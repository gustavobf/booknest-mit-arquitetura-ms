package br.edu.infnet.gustavo_figueiredo_api.usuario.model;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.*;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "usuarios")
public class Usuario implements Identificavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do usuário", example = "1")
    @Positive(message = "ID do usuário deve ser positivo.")
    private Long id;

    @Schema(description = "Nome do usuário", example = "João Silva")
    @NotBlank(message = "Nome do usuário é obrigatório.")
    @Size(max = 120, message = "Nome do usuário deve ter no máximo 120 caracteres.")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "joao.silva@biblioteca.com")
    @NotBlank(message = "E-mail do usuário é obrigatório.")
    @Email(message = "E-mail do usuário inválido.")
    private String email;

    @Schema(description = "Matrícula do usuário", example = "MAT2025001")
    @NotBlank(message = "Matrícula do usuário é obrigatória.")
    @Size(max = 30, message = "Matrícula deve ter no máximo 30 caracteres.")
    @Column(unique = true)
    private String matricula;

    @Schema(description = "Indica se o usuário está ativo", example = "true")
    @NotNull(message = "Situação de atividade do usuário é obrigatória.")
    private Boolean ativo;

    public Usuario () {
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getNome () {
        return nome;
    }

    public void setNome (String nome) {
        this.nome = nome;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getMatricula () {
        return matricula;
    }

    public void setMatricula (String matricula) {
        this.matricula = matricula;
    }

    public Boolean getAtivo () {
        return ativo;
    }

    public void setAtivo (Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString () {
        return "Usuario{" + "id=" + id + ", nome='" + nome + '\'' + ", email='" + email + '\'' + ", matricula='" + matricula + '\'' + ", ativo=" + ativo + '}';
    }
}

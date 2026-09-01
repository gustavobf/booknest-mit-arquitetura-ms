package br.edu.infnet.gustavo_figueiredo_api.catalogo.model;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.*;

@Entity
@Table(name = "autores")
public class Autor implements Identificavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do autor", example = "1")
    @Positive(message = "ID do autor deve ser positivo.")
    private Long id;

    @Schema(description = "Nome completo do autor", example = "Machado de Assis")
    @NotBlank(message = "Nome do autor é obrigatório.")
    @Size(max = 120, message = "Nome do autor deve ter no máximo 120 caracteres.")
    private String nome;

    @Schema(description = "Nacionalidade do autor", example = "Brasileiro")
    @NotBlank(message = "Nacionalidade do autor é obrigatória.")
    @Size(max = 80, message = "Nacionalidade do autor deve ter no máximo 80 caracteres.")
    private String nacionalidade;

    @Schema(description = "Ano de nascimento do autor", example = "1839")
    @NotNull(message = "Ano de nascimento é obrigatório.")
    @Min(value = 1000, message = "Ano de nascimento inválido.")
    private Integer anoNascimento;

    @OneToMany(mappedBy = "autor")
    @JsonIgnore
    private List<Livro> livros = new ArrayList<>();

    public Autor () {
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

    public String getNacionalidade () {
        return nacionalidade;
    }

    public void setNacionalidade (String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public Integer getAnoNascimento () {
        return anoNascimento;
    }

    public void setAnoNascimento (Integer anoNascimento) {
        this.anoNascimento = anoNascimento;
    }

    public List<Livro> getLivros () {
        return livros;
    }

    @Override
    public String toString () {
        return "Autor{" + "id=" + id + ", nome='" + nome + '\'' + ", nacionalidade='" + nacionalidade + '\'' + ", anoNascimento=" + anoNascimento + ", livros=" + livros.size() + '}';
    }
}

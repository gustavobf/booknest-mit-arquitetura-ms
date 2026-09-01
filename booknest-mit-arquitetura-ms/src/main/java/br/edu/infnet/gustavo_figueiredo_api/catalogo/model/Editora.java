package br.edu.infnet.gustavo_figueiredo_api.catalogo.model;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.*;

@Entity
@Table(name = "editoras")
public class Editora implements Identificavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da editora", example = "1")
    @Positive(message = "ID da editora deve ser positivo.")
    private Long id;

    @Schema(description = "Nome da editora", example = "Companhia das Letras")
    @NotBlank(message = "Nome da editora é obrigatório.")
    @Size(max = 120, message = "Nome da editora deve ter no máximo 120 caracteres.")
    private String nome;

    @Schema(description = "Cidade sede da editora", example = "São Paulo")
    @NotBlank(message = "Cidade da editora é obrigatória.")
    @Size(max = 120, message = "Cidade da editora deve ter no máximo 120 caracteres.")
    private String cidade;

    @Schema(description = "E-mail de contato", example = "contato@companhiadasletras.com.br")
    @NotBlank(message = "E-mail de contato é obrigatório.")
    @Email(message = "E-mail de contato inválido.")
    private String emailContato;

    @Schema(description = "Indicador de atividade da editora", example = "true")
    @NotNull(message = "Situação de atividade da editora é obrigatória.")
    private Boolean ativa;

    @OneToMany(mappedBy = "editora")
    @JsonIgnore
    private List<Livro> livros = new ArrayList<>();

    public Editora () {
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

    public String getCidade () {
        return cidade;
    }

    public void setCidade (String cidade) {
        this.cidade = cidade;
    }

    public String getEmailContato () {
        return emailContato;
    }

    public void setEmailContato (String emailContato) {
        this.emailContato = emailContato;
    }

    public Boolean getAtiva () {
        return ativa;
    }

    public void setAtiva (Boolean ativa) {
        this.ativa = ativa;
    }

    public List<Livro> getLivros () {
        return livros;
    }

    @Override
    public String toString () {
        return "Editora{" + "id=" + id + ", nome='" + nome + '\'' + ", cidade='" + cidade + '\'' + ", emailContato='" + emailContato + '\'' + ", ativa=" + ativa + ", livros=" + livros.size() + '}';
    }
}

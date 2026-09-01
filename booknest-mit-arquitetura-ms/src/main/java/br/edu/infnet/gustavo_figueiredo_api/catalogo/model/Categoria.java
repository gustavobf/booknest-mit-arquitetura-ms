package br.edu.infnet.gustavo_figueiredo_api.catalogo.model;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.*;

@Entity
@Table(name = "categorias")
public class Categoria implements Identificavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da categoria", example = "1")
    @Positive(message = "ID da categoria deve ser positivo.")
    private Long id;

    @Schema(description = "Nome da categoria", example = "Ficção")
    @NotBlank(message = "Nome da categoria é obrigatório.")
    @Size(max = 80, message = "Nome da categoria deve ter no máximo 80 caracteres.")
    private String nome;

    @Schema(description = "Descrição da categoria", example = "Narrativas de ficção literária")
    @NotBlank(message = "Descrição da categoria é obrigatória.")
    @Size(max = 255, message = "Descrição da categoria deve ter no máximo 255 caracteres.")
    private String descricao;

    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<Livro> livros = new ArrayList<>();

    public Categoria () {
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

    public String getDescricao () {
        return descricao;
    }

    public void setDescricao (String descricao) {
        this.descricao = descricao;
    }

    public List<Livro> getLivros () {
        return livros;
    }

    @Override
    public String toString () {
        return "Categoria{" + "id=" + id + ", nome='" + nome + '\'' + ", descricao='" + descricao + '\'' + ", livros=" + livros.size() + '}';
    }
}

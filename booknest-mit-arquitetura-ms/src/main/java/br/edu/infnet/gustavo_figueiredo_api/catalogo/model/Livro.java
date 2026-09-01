package br.edu.infnet.gustavo_figueiredo_api.catalogo.model;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.*;

@Entity
@Table(name = "livros")
public class Livro implements Identificavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do livro", example = "1")
    @Positive(message = "ID do livro deve ser positivo.")
    private Long id;

    @Schema(description = "Título do livro", example = "Dom Casmurro")
    @NotBlank(message = "Título do livro é obrigatório.")
    @Size(max = 160, message = "Título do livro deve ter no máximo 160 caracteres.")
    private String titulo;

    @Schema(description = "ISBN do livro", example = "978-8535905571")
    @NotBlank(message = "ISBN do livro é obrigatório.")
    @Size(max = 20, message = "ISBN deve ter no máximo 20 caracteres.")
    private String isbn;

    @Schema(description = "Autor associado ao livro", implementation = Autor.class)
    @ManyToOne(optional = false)
    @JoinColumn(name = "autor_id", nullable = false)
    @NotNull(message = "Livro deve possuir autor.")
    @JsonIgnoreProperties("livros")
    private Autor autor;

    @Schema(description = "Categoria associada ao livro", implementation = Categoria.class)
    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    @NotNull(message = "Livro deve possuir categoria.")
    @JsonIgnoreProperties("livros")
    private Categoria categoria;

    @Schema(description = "Editora associada ao livro", implementation = Editora.class)
    @ManyToOne(optional = false)
    @JoinColumn(name = "editora_id", nullable = false)
    @NotNull(message = "Livro deve possuir editora.")
    @JsonIgnoreProperties("livros")
    private Editora editora;

    @OneToMany(mappedBy = "livro")
    @JsonIgnore
    private List<Exemplar> exemplares = new ArrayList<>();

    public Livro () {
    }

    public Livro (Long id, String titulo, String isbn) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getTitulo () {
        return titulo;
    }

    public void setTitulo (String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn () {
        return isbn;
    }

    public void setIsbn (String isbn) {
        this.isbn = isbn;
    }

    public Boolean getDisponivel () {
        return exemplares.stream().anyMatch(Exemplar::getDisponivel);
    }

    public Autor getAutor () {
        return autor;
    }

    public void setAutor (Autor autor) {
        this.autor = autor;
    }

    public Categoria getCategoria () {
        return categoria;
    }

    public void setCategoria (Categoria categoria) {
        this.categoria = categoria;
    }

    public Editora getEditora () {
        return editora;
    }

    public void setEditora (Editora editora) {
        this.editora = editora;
    }

    public List<Exemplar> getExemplares () {
        return exemplares;
    }

    public long getQuantidadeExemplaresDisponiveis () {
        return exemplares.stream().filter(Exemplar::getDisponivel).count();
    }

    public long getQuantidadeExemplaresEmprestados () {
        return exemplares.size() - getQuantidadeExemplaresDisponiveis();
    }

    @Override
    public String toString () {
        return "Livro{" + "id=" + id + ", titulo='" + titulo + '\'' + ", isbn='" + isbn + '\'' + ", autor=" + (
                autor != null ? autor.getNome() : "N/A") + ", categoria=" + (categoria != null ? categoria.getNome() :
                "N/A") + ", editora=" + (editora != null ? editora.getNome() :
                "N/A") + ", totalExemplares=" + exemplares.size() + ", exemplaresDisponiveis=" + getQuantidadeExemplaresDisponiveis() + ", exemplaresEmprestados=" + getQuantidadeExemplaresEmprestados() + '}';
    }
}

package br.edu.infnet.gustavo_figueiredo_api.catalogo.model;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "exemplares")
public class Exemplar implements Identificavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do exemplar", example = "1")
    @Positive(message = "ID do exemplar deve ser positivo.")
    private Long id;

    @Schema(description = "Código interno do exemplar", example = "DOM-001")
    @NotBlank(message = "Código do exemplar é obrigatório.")
    @Size(max = 30, message = "Código do exemplar deve ter no máximo 30 caracteres.")
    @Column(unique = true)
    private String codigo;

    @Schema(description = "Estado de conservação do exemplar", example = "BOM")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Estado de conservação é obrigatório.")
    private EstadoConservacao estadoConservacao;

    @Schema(description = "Disponibilidade atual do exemplar", example = "true")
    @NotNull(message = "Disponibilidade do exemplar é obrigatória.")
    private Boolean disponivel;

    @Schema(description = "Livro associado ao exemplar", implementation = Livro.class)
    @ManyToOne(optional = false)
    @JoinColumn(name = "livro_id", nullable = false)
    @NotNull(message = "Exemplar deve estar associado a um livro.")
    @JsonIgnoreProperties({"exemplares"})
    private Livro livro;

    public Exemplar () {
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getCodigo () {
        return codigo;
    }

    public void setCodigo (String codigo) {
        this.codigo = codigo;
    }

    public EstadoConservacao getEstadoConservacao () {
        return estadoConservacao;
    }

    public void setEstadoConservacao (EstadoConservacao estadoConservacao) {
        this.estadoConservacao = estadoConservacao;
    }

    public Boolean getDisponivel () {
        return disponivel;
    }

    public void setDisponivel (Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Livro getLivro () {
        return livro;
    }

    public void setLivro (Livro livro) {
        this.livro = livro;
    }

    public void registrarDevolucao () {
        disponivel = true;
    }

    @Override
    public String toString () {
        return "Exemplar{" + "id=" + id + ", codigo='" + codigo + '\'' + ", estadoConservacao=" + estadoConservacao + ", disponivel=" + disponivel + ", livro=" + (
                livro != null ? livro.getTitulo() : "N/A") + '}';
    }
}

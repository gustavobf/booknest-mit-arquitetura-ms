package br.edu.infnet.gustavo_figueiredo_api.emprestimo.model;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.*;
import br.edu.infnet.gustavo_figueiredo_api.usuario.model.*;
import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.*;

@Entity
@Table(name = "emprestimos")
public class Emprestimo implements Identificavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do empréstimo", example = "1")
    @Positive(message = "ID do empréstimo deve ser positivo.")
    private Long id;

    @Schema(description = "Usuário que realizou o empréstimo", implementation = Usuario.class)
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull(message = "Empréstimo deve possuir usuário.")
    @JsonIgnoreProperties("emprestimos")
    private Usuario usuario;

    @Schema(description = "Data de início do empréstimo", example = "2025-06-05")
    @NotNull(message = "Data do empréstimo é obrigatória.")
    private LocalDate dataEmprestimo;

    @Schema(description = "Data prevista para devolução", example = "2025-06-20")
    @NotNull(message = "Data esperada de devolução é obrigatória.")
    private LocalDate dataEsperadaDevolucao;

    @Schema(description = "Data de devolução efetiva", example = "2025-06-22")
    private LocalDate dataDevolucao;

    @Schema(description = "Valor da multa", example = "5.50")
    @NotNull(message = "Multa é obrigatória.")
    @DecimalMin(value = "0.0", message = "Multa não pode ser negativa.")
    private Double multa;

    @Schema(description = "Exemplar emprestado", implementation = Exemplar.class)
    @ManyToOne(optional = false)
    @JoinColumn(name = "exemplar_id", nullable = false)
    @NotNull(message = "Empréstimo deve possuir exemplar.")
    @JsonIgnoreProperties({"emprestimos"})
    private Exemplar exemplar;

    public Emprestimo () {
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public Usuario getUsuario () {
        return usuario;
    }

    public void setUsuario (Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getDataEmprestimo () {
        return dataEmprestimo;
    }

    public void setDataEmprestimo (LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataEsperadaDevolucao () {
        return dataEsperadaDevolucao;
    }

    public void setDataEsperadaDevolucao (LocalDate dataEsperadaDevolucao) {
        this.dataEsperadaDevolucao = dataEsperadaDevolucao;
    }

    public LocalDate getDataDevolucao () {
        return dataDevolucao;
    }

    public void setDataDevolucao (LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public Double getMulta () {
        return multa;
    }

    public void setMulta (Double multa) {
        this.multa = multa;
    }

    public Exemplar getExemplar () {
        return exemplar;
    }

    public void setExemplar (Exemplar exemplar) {
        this.exemplar = exemplar;
    }

    public boolean estaDevolvido () {
        return dataDevolucao != null;
    }

    public boolean estaAtrasado () {
        if (dataEsperadaDevolucao == null) {
            return false;
        }

        LocalDate dataComparacao = estaDevolvido() ? dataDevolucao : LocalDate.now();
        return dataComparacao != null && dataComparacao.isAfter(dataEsperadaDevolucao);
    }

    public void registrarDevolucao (LocalDate dataDevolucao, Double multa) {
        this.dataDevolucao = dataDevolucao;
        this.multa = multa;
        if (exemplar != null) {
            exemplar.registrarDevolucao();
        }
    }

    @Override
    public String toString () {
        return "Emprestimo{" + "id=" + id + ", usuario='" + (usuario != null ? usuario.getNome() :
                "N/A") + '\'' + ", dataEmprestimo=" + dataEmprestimo + ", dataEsperadaDevolucao=" + dataEsperadaDevolucao + ", dataDevolucao=" + dataDevolucao + ", multa=" + multa + ", exemplar=" + (
                exemplar != null ? exemplar.getCodigo() : "N/A") + ", livro=" + (
                exemplar != null && exemplar.getLivro() != null ? exemplar.getLivro().getTitulo() :
                        "N/A") + ", status='" + (estaDevolvido() ? "DEVOLVIDO" :
                "EM_ABERTO") + '\'' + ", atrasado=" + estaAtrasado() + '}';
    }
}

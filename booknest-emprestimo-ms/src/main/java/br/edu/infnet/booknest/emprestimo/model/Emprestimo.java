package br.edu.infnet.booknest.emprestimo.model;

import jakarta.persistence.*;

import java.time.*;

@Entity
@Table(name = "emprestimos")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private Long exemplarId;

    @Column(nullable = false)
    private LocalDate dataEmprestimo;

    @Column(nullable = false)
    private LocalDate dataEsperadaDevolucao;

    private LocalDate dataDevolucao;

    @Column(nullable = false)
    private Double multa;

    public Emprestimo () {
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public Long getUsuarioId () {
        return usuarioId;
    }

    public void setUsuarioId (Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getExemplarId () {
        return exemplarId;
    }

    public void setExemplarId (Long exemplarId) {
        this.exemplarId = exemplarId;
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

    public boolean estaDevolvido () {
        return dataDevolucao != null;
    }

    public boolean estaAtrasado () {
        if (dataEsperadaDevolucao == null) {
            return false;
        }
        LocalDate dataComparacao = estaDevolvido() ? dataDevolucao : LocalDate.now();
        return dataComparacao.isAfter(dataEsperadaDevolucao);
    }

    public void registrarDevolucao (LocalDate dataDevolucao, Double multa) {
        this.dataDevolucao = dataDevolucao;
        this.multa = multa;
    }
}

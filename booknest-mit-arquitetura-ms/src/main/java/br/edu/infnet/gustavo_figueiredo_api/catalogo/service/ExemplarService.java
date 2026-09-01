package br.edu.infnet.gustavo_figueiredo_api.catalogo.service;

import br.edu.infnet.gustavo_figueiredo_api.exception.*;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.*;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.repository.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class ExemplarService {

    private final ExemplarRepository exemplarRepository;
    private final LivroRepository livroRepository;

    public ExemplarService (ExemplarRepository exemplarRepository, LivroRepository livroRepository) {
        this.exemplarRepository = exemplarRepository;
        this.livroRepository = livroRepository;
    }

    protected void validarEntidade (Exemplar entidade) {
        if (entidade == null) {
            throw new DadosInvalidosException("Exemplar não pode ser nulo.");
        }
        if (entidade.getCodigo() == null || entidade.getCodigo().isBlank()) {
            throw new DadosInvalidosException("Exemplar deve possuir código.");
        }
        if (entidade.getEstadoConservacao() == null) {
            throw new DadosInvalidosException("Exemplar deve possuir estado de conservação.");
        }
        if (entidade.getDisponivel() == null) {
            throw new DadosInvalidosException("Exemplar deve informar disponibilidade.");
        }
        if (entidade.getLivro() == null || entidade.getLivro().getId() == null) {
            throw new DadosInvalidosException("Exemplar deve estar associado a um livro.");
        }
    }

    @Transactional(readOnly = true)
    public List<Exemplar> obterLista () {
        return exemplarRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Exemplar obterPorId (Long id) {
        return exemplarRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Exemplar com id " + id + " não encontrado."));
    }

    @Transactional
    public Exemplar incluir (Exemplar entidade) {
        validarEntidade(entidade);
        prepararRelacionamentos(entidade);
        return exemplarRepository.save(entidade);
    }

    @Transactional
    public Exemplar alterar (Exemplar entidade) {
        validarEntidade(entidade);
        if (entidade.getId() == null || !exemplarRepository.existsById(entidade.getId())) {
            throw new RegistroNaoEncontradoException("Exemplar com id " + entidade.getId() + " não encontrado.");
        }
        prepararRelacionamentos(entidade);
        return exemplarRepository.save(entidade);
    }

    @Transactional
    public void excluir (Long id) {
        if (!exemplarRepository.existsById(id)) {
            throw new RegistroNaoEncontradoException("Exemplar com id " + id + " não encontrado.");
        }
        exemplarRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Exemplar> listarDisponiveis () {
        return exemplarRepository.findByDisponivelTrue();
    }

    @Transactional(readOnly = true)
    public List<Exemplar> listarIndisponiveis () {
        return exemplarRepository.findByDisponivelFalse();
    }

    @Transactional(readOnly = true)
    public List<Exemplar> listarPorDisponibilidade (Boolean disponivel) {
        if (disponivel == null) {
            return obterLista();
        }
        return disponivel ? listarDisponiveis() : listarIndisponiveis();
    }

    @Transactional(readOnly = true)
    public List<Exemplar> listarPorLivro (Long idLivro) {
        return exemplarRepository.findByLivroId(idLivro);
    }

    private void prepararRelacionamentos (Exemplar exemplar) {
        Long idLivro = exemplar.getLivro().getId();
        Livro livro = livroRepository.findById(idLivro)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Livro com id " + idLivro + " não encontrado."));
        exemplar.setLivro(livro);
    }
}

package br.edu.infnet.gustavo_figueiredo_api.catalogo.service;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.*;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.repository.*;
import br.edu.infnet.gustavo_figueiredo_api.exception.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService (CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    protected void validarEntidade (Categoria entidade) {
        if (entidade == null) {
            throw new DadosInvalidosException("Categoria não pode ser nula.");
        }
        if (entidade.getNome() == null || entidade.getNome().isBlank()) {
            throw new DadosInvalidosException("Categoria deve possuir nome.");
        }
        if (entidade.getDescricao() == null || entidade.getDescricao().isBlank()) {
            throw new DadosInvalidosException("Categoria deve possuir descrição.");
        }
    }

    @Transactional(readOnly = true)
    public List<Categoria> obterLista () {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria obterPorId (Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Categoria com id " + id + " não encontrado."));
    }

    @Transactional
    public Categoria incluir (Categoria entidade) {
        validarEntidade(entidade);
        return categoriaRepository.save(entidade);
    }

    @Transactional
    public Categoria alterar (Categoria entidade) {
        validarEntidade(entidade);
        if (entidade.getId() == null || !categoriaRepository.existsById(entidade.getId())) {
            throw new RegistroNaoEncontradoException("Categoria com id " + entidade.getId() + " não encontrado.");
        }
        return categoriaRepository.save(entidade);
    }

    @Transactional(readOnly = true)
    public List<Categoria> listar (Boolean ordenarPorQuantidadeLivros) {
        if (Boolean.TRUE.equals(ordenarPorQuantidadeLivros)) {
            return listarOrdenadasPorQuantidadeLivros();
        }
        return obterLista();
    }

    @Transactional
    public void excluir (Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RegistroNaoEncontradoException("Categoria com id " + id + " não encontrado.");
        }
        categoriaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarOrdenadasPorQuantidadeLivros () {
        return categoriaRepository.findAllOrderByQuantidadeLivrosDesc();
    }
}

package br.edu.infnet.gustavo_figueiredo_api.catalogo.service;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.*;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.repository.*;
import br.edu.infnet.gustavo_figueiredo_api.exception.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final EditoraRepository editoraRepository;

    public LivroService (LivroRepository livroRepository, AutorRepository autorRepository,
                         CategoriaRepository categoriaRepository, EditoraRepository editoraRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
        this.editoraRepository = editoraRepository;
    }

    protected void validarEntidade (Livro entidade) {
        if (entidade == null) {
            throw new DadosInvalidosException("Livro não pode ser nulo.");
        }
        if (entidade.getTitulo() == null || entidade.getTitulo().isBlank()) {
            throw new DadosInvalidosException("Livro deve possuir título.");
        }
        if (entidade.getIsbn() == null || entidade.getIsbn().isBlank()) {
            throw new DadosInvalidosException("Livro deve possuir ISBN.");
        }
        if (entidade.getAutor() == null || entidade.getAutor().getId() == null) {
            throw new DadosInvalidosException("Livro deve possuir autor.");
        }
        if (entidade.getCategoria() == null || entidade.getCategoria().getId() == null) {
            throw new DadosInvalidosException("Livro deve possuir categoria.");
        }
        if (entidade.getEditora() == null || entidade.getEditora().getId() == null) {
            throw new DadosInvalidosException("Livro deve possuir editora.");
        }
    }

    @Transactional(readOnly = true)
    public List<Livro> obterLista () {
        return livroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Livro obterPorId (Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Livro com id " + id + " não encontrado."));
    }

    @Transactional
    public Livro incluir (Livro entidade) {
        validarEntidade(entidade);
        prepararRelacionamentos(entidade);
        return livroRepository.save(entidade);
    }

    @Transactional
    public Livro alterar (Livro entidade) {
        validarEntidade(entidade);
        if (entidade.getId() == null || !livroRepository.existsById(entidade.getId())) {
            throw new RegistroNaoEncontradoException("Livro com id " + entidade.getId() + " não encontrado.");
        }
        prepararRelacionamentos(entidade);
        return livroRepository.save(entidade);
    }

    @Transactional
    public void excluir (Long id) {
        if (!livroRepository.existsById(id)) {
            throw new RegistroNaoEncontradoException("Livro com id " + id + " não encontrado.");
        }
        livroRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Livro> listarOrdenadosPorTitulo () {
        return livroRepository.findAllByOrderByTituloAsc();
    }

    @Transactional(readOnly = true)
    public List<Livro> listarDisponiveis () {
        return livroRepository.findDisponiveis();
    }

    @Transactional(readOnly = true)
    public List<Livro> listarIndisponiveis () {
        return livroRepository.findIndisponiveis();
    }

    @Transactional(readOnly = true)
    public List<Livro> listarPorDisponibilidade (Boolean disponivel) {
        if (disponivel == null) {
            return obterLista();
        }
        return disponivel ? listarDisponiveis() : listarIndisponiveis();
    }

    private void prepararRelacionamentos (Livro livro) {
        Long idAutor = livro.getAutor().getId();
        Long idCategoria = livro.getCategoria().getId();
        Long idEditora = livro.getEditora().getId();

        Autor autor = autorRepository.findById(idAutor)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Autor com id " + idAutor + " não encontrado."));
        Categoria categoria = categoriaRepository.findById(idCategoria).orElseThrow(
                () -> new RegistroNaoEncontradoException("Categoria com id " + idCategoria + " não encontrada."));
        Editora editora = editoraRepository.findById(idEditora).orElseThrow(
                () -> new RegistroNaoEncontradoException("Editora com id " + idEditora + " não encontrada."));

        livro.setAutor(autor);
        livro.setCategoria(categoria);
        livro.setEditora(editora);
    }
}

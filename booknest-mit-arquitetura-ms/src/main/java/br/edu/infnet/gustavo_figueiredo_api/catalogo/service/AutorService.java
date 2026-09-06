package br.edu.infnet.gustavo_figueiredo_api.catalogo.service;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.*;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.repository.*;
import br.edu.infnet.gustavo_figueiredo_api.exception.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService (AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    protected void validarEntidade (Autor entidade) {
        if (entidade == null) {
            throw new DadosInvalidosException("Autor não pode ser nulo.");
        }
        if (entidade.getNome() == null || entidade.getNome().isBlank()) {
            throw new DadosInvalidosException("Autor deve possuir nome.");
        }
        if (entidade.getNacionalidade() == null || entidade.getNacionalidade().isBlank()) {
            throw new DadosInvalidosException("Autor deve possuir nacionalidade.");
        }
        if (entidade.getAnoNascimento() == null || entidade.getAnoNascimento() <= 0) {
            throw new DadosInvalidosException("Autor deve possuir ano de nascimento válido.");
        }
    }

    @Transactional(readOnly = true)
    public List<Autor> obterLista () {
        return autorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Autor obterPorId (Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Autor com id " + id + " não encontrado."));
    }

    @Transactional
    public Autor incluir (Autor entidade) {
        validarEntidade(entidade);
        return autorRepository.save(entidade);
    }

    @Transactional
    public Autor alterar (Autor entidade) {
        validarEntidade(entidade);
        if (entidade.getId() == null || !autorRepository.existsById(entidade.getId())) {
            throw new RegistroNaoEncontradoException("Autor com id " + entidade.getId() + " não encontrado.");
        }
        return autorRepository.save(entidade);
    }

    @Transactional
    public void excluir (Long id) {
        if (!autorRepository.existsById(id)) {
            throw new RegistroNaoEncontradoException("Autor com id " + id + " não encontrado.");
        }
        autorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Autor> listar (Boolean ordenarPorNome) {
        if (Boolean.TRUE.equals(ordenarPorNome)) {
            return listarOrdenadosPorNome();
        }
        return obterLista();
    }

    @Transactional(readOnly = true)
    public List<Autor> listarOrdenadosPorNome () {
        return autorRepository.findAllByOrderByNomeAsc();
    }

    @Transactional(readOnly = true)
    public List<Autor> buscarPorNacionalidade (String nacionalidade) {
        return autorRepository.findByNacionalidadeIgnoreCase(nacionalidade);
    }
}

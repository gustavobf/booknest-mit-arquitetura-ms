package br.edu.infnet.gustavo_figueiredo_api.catalogo.service;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.integration.dto.*;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.*;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.repository.*;
import br.edu.infnet.gustavo_figueiredo_api.exception.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class EditoraService {

    private final EditoraRepository editoraRepository;
    private final CepService cepService;

    public EditoraService (EditoraRepository editoraRepository, CepService cepService) {
        this.editoraRepository = editoraRepository;
        this.cepService = cepService;
    }

    protected void validarEntidade (Editora entidade) {
        if (entidade == null) {
            throw new DadosInvalidosException("Editora não pode ser nula.");
        }
        if (entidade.getNome() == null || entidade.getNome().isBlank()) {
            throw new DadosInvalidosException("Editora deve possuir nome.");
        }
        if (entidade.getCidade() == null || entidade.getCidade().isBlank()) {
            throw new DadosInvalidosException("Editora deve possuir cidade.");
        }
        if (entidade.getEmailContato() == null || entidade.getEmailContato().isBlank()) {
            throw new DadosInvalidosException("Editora deve possuir email de contato.");
        }
        if (entidade.getAtiva() == null) {
            throw new DadosInvalidosException("Editora deve informar se está ativa.");
        }
    }

    @Transactional(readOnly = true)
    public List<Editora> obterLista () {
        return editoraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Editora obterPorId (Long id) {
        return editoraRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Editora com id " + id + " não encontrada."));
    }

    @Transactional
    public Editora incluir (Editora entidade) {
        validarEntidade(entidade);
        return editoraRepository.save(entidade);
    }

    @Transactional
    public Editora alterar (Editora entidade) {
        validarEntidade(entidade);
        if (entidade.getId() == null || !editoraRepository.existsById(entidade.getId())) {
            throw new RegistroNaoEncontradoException("Editora com id " + entidade.getId() + " não encontrada.");
        }
        return editoraRepository.save(entidade);
    }

    @Transactional
    public void excluir (Long id) {
        if (!editoraRepository.existsById(id)) {
            throw new RegistroNaoEncontradoException("Editora com id " + id + " não encontrada.");
        }
        editoraRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Editora> listarAtivas () {
        return editoraRepository.findByAtivaTrue();
    }

    @Transactional(readOnly = true)
    public List<Editora> listarInativas () {
        return editoraRepository.findByAtivaFalse();
    }

    @Transactional(readOnly = true)
    public List<Editora> listarPorAtiva (Boolean ativa) {
        if (ativa == null) {
            return obterLista();
        }
        return ativa ? listarAtivas() : listarInativas();
    }

    @Transactional
    public Editora atualizarCidadePorCep (Long idEditora, String cep) {
        Editora editora = obterPorId(idEditora);
        ViaCepResponse endereco = cepService.consultar(cep);
        if (endereco.cidade() == null || endereco.cidade().isBlank()) {
            throw new DadosInvalidosException("A API externa não retornou cidade para o CEP informado.");
        }

        editora.setCidade(endereco.cidade());
        return editoraRepository.save(editora);
    }
}

package br.edu.infnet.gustavo_figueiredo_api.usuario.service;

import br.edu.infnet.gustavo_figueiredo_api.exception.*;
import br.edu.infnet.gustavo_figueiredo_api.usuario.model.*;
import br.edu.infnet.gustavo_figueiredo_api.usuario.repository.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService (UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    protected void validarEntidade (Usuario entidade) {
        if (entidade == null) {
            throw new DadosInvalidosException("Usuário não pode ser nulo.");
        }
        if (entidade.getNome() == null || entidade.getNome().isBlank()) {
            throw new DadosInvalidosException("Usuário deve possuir nome.");
        }
        if (entidade.getEmail() == null || entidade.getEmail().isBlank()) {
            throw new DadosInvalidosException("Usuário deve possuir email.");
        }
        if (entidade.getMatricula() == null || entidade.getMatricula().isBlank()) {
            throw new DadosInvalidosException("Usuário deve possuir matrícula.");
        }
        if (entidade.getAtivo() == null) {
            throw new DadosInvalidosException("Usuário deve informar se está ativo.");
        }
    }

    @Transactional(readOnly = true)
    public List<Usuario> obterLista () {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario obterPorId (Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Usuário com id " + id + " não encontrado."));
    }

    @Transactional
    public Usuario incluir (Usuario entidade) {
        validarEntidade(entidade);
        return usuarioRepository.save(entidade);
    }

    @Transactional
    public Usuario alterar (Usuario entidade) {
        validarEntidade(entidade);
        if (entidade.getId() == null || !usuarioRepository.existsById(entidade.getId())) {
            throw new RegistroNaoEncontradoException("Usuário com id " + entidade.getId() + " não encontrado.");
        }
        return usuarioRepository.save(entidade);
    }

    @Transactional
    public void excluir (Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RegistroNaoEncontradoException("Usuário com id " + id + " não encontrado.");
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarAtivos () {
        return usuarioRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarInativos () {
        return usuarioRepository.findByAtivoFalse();
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarPorAtivo (Boolean ativo) {
        if (ativo == null) {
            return obterLista();
        }
        return ativo ? listarAtivos() : listarInativos();
    }
}

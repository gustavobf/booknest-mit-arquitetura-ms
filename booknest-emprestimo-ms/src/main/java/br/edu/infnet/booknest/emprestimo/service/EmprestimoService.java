package br.edu.infnet.booknest.emprestimo.service;

import br.edu.infnet.booknest.emprestimo.controller.dto.*;
import br.edu.infnet.booknest.emprestimo.exception.*;
import br.edu.infnet.booknest.emprestimo.model.*;
import br.edu.infnet.booknest.emprestimo.repository.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.time.*;
import java.util.*;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;

    public EmprestimoService (EmprestimoRepository emprestimoRepository) {
        this.emprestimoRepository = emprestimoRepository;
    }

    protected void validarEntidade (Emprestimo entidade) {
        if (entidade == null) {
            throw new DadosInvalidosException("Empréstimo não pode ser nulo.");
        }
        if (entidade.getUsuarioId() == null) {
            throw new DadosInvalidosException("Empréstimo deve possuir usuário.");
        }
        if (entidade.getExemplarId() == null) {
            throw new DadosInvalidosException("Empréstimo deve possuir exemplar.");
        }
        if (entidade.getDataEmprestimo() == null) {
            throw new DadosInvalidosException("Empréstimo deve possuir data de empréstimo.");
        }
        if (entidade.getDataEsperadaDevolucao() == null) {
            throw new DadosInvalidosException("Empréstimo deve possuir data esperada de devolução.");
        }
        if (entidade.getMulta() == null) {
            throw new DadosInvalidosException("Empréstimo deve possuir multa.");
        }
    }

    @Transactional(readOnly = true)
    public List<Emprestimo> obterLista () {
        return emprestimoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Emprestimo obterPorId (Long id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Empréstimo com id " + id + " não encontrado."));
    }

    @Transactional
    public Emprestimo incluir (Emprestimo entidade) {
        validarEntidade(entidade);
        return emprestimoRepository.save(entidade);
    }

    @Transactional
    public Emprestimo alterar (Emprestimo entidade) {
        validarEntidade(entidade);
        if (entidade.getId() == null || !emprestimoRepository.existsById(entidade.getId())) {
            throw new RegistroNaoEncontradoException("Empréstimo com id " + entidade.getId() + " não encontrado.");
        }
        return emprestimoRepository.save(entidade);
    }

    @Transactional
    public void excluir (Long id) {
        if (!emprestimoRepository.existsById(id)) {
            throw new RegistroNaoEncontradoException("Empréstimo com id " + id + " não encontrado.");
        }
        emprestimoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Emprestimo> listarAtrasados () {
        return emprestimoRepository.findAtrasados(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public List<Emprestimo> listarNaoAtrasados () {
        return emprestimoRepository.findNaoAtrasados(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public List<Emprestimo> listarPorUsuario (Long usuarioId) {
        if (usuarioId == null) {
            throw new DadosInvalidosException("ID do usuário é obrigatório.");
        }
        return emprestimoRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Emprestimo> listarPorSituacaoAtraso (Boolean atrasado) {
        if (atrasado == null) {
            return obterLista();
        }
        return atrasado ? listarAtrasados() : listarNaoAtrasados();
    }

    @Transactional
    public Emprestimo registrarDevolucao (Long idEmprestimo, LocalDate dataDevolucao, Double multa) {
        if (dataDevolucao == null) {
            throw new DadosInvalidosException("Data de devolução é obrigatória.");
        }
        if (multa == null || multa < 0) {
            throw new DadosInvalidosException("Multa deve ser informada e não pode ser negativa.");
        }

        Emprestimo emprestimo = obterPorId(idEmprestimo);
        if (emprestimo.estaDevolvido()) {
            throw new OperacaoNaoPermitidaException("Empréstimo " + idEmprestimo + " já foi devolvido.");
        }

        emprestimo.registrarDevolucao(dataDevolucao, multa);
        return emprestimoRepository.save(emprestimo);
    }

    public EmprestimoResponse toResponse (Emprestimo emprestimo) {
        return EmprestimoResponse.from(emprestimo);
    }

    public Emprestimo toEntity (EmprestimoRequest request, Long id) {
        return request.toEntity(id);
    }

    public Emprestimo toEntity (RegistrarDevolucaoRequest request, Emprestimo emprestimo) {
        emprestimo.registrarDevolucao(request.dataDevolucao(), request.multa());
        return emprestimo;
    }
}

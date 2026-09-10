package br.edu.infnet.gustavo_figueiredo_api.emprestimo.service;

import br.edu.infnet.gustavo_figueiredo_api.emprestimo.controller.dto.*;
import br.edu.infnet.gustavo_figueiredo_api.emprestimo.integration.client.*;
import br.edu.infnet.gustavo_figueiredo_api.emprestimo.integration.dto.*;
import br.edu.infnet.gustavo_figueiredo_api.exception.*;
import com.fasterxml.jackson.databind.*;
import feign.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class EmprestimoService {

    private final EmprestimoClient emprestimoClient;

    public EmprestimoService (EmprestimoClient emprestimoClient) {
        this.emprestimoClient = emprestimoClient;
    }

    public List<EmprestimoResponse> obterLista () {
        try {
            return emprestimoClient.listarTodos().stream().map(EmprestimoResponse::fromClientDto).toList();
        } catch (FeignException ex) {
            throw erroDoClient(ex);
        }
    }

    public EmprestimoResponse obterPorId (Long id) {
        try {
            return EmprestimoResponse.fromClientDto(emprestimoClient.obterPorId(id));
        } catch (FeignException ex) {
            throw erroDoClient(ex);
        }
    }

    public EmprestimoResponse incluir (EmprestimoRequest request) {
        try {
            return EmprestimoResponse.fromClientDto(emprestimoClient.incluir(toRequest(request)));
        } catch (FeignException ex) {
            throw erroDoClient(ex);
        }
    }

    public EmprestimoResponse alterar (Long id, EmprestimoRequest request) {
        try {
            return EmprestimoResponse.fromClientDto(emprestimoClient.alterar(id, toRequest(request)));
        } catch (FeignException ex) {
            throw erroDoClient(ex);
        }
    }

    public void excluir (Long id) {
        try {
            emprestimoClient.excluir(id);
        } catch (FeignException ex) {
            throw erroDoClient(ex);
        }
    }

    public List<EmprestimoResponse> listarAtrasados () {
        try {
            return emprestimoClient.listar(true).stream().map(EmprestimoResponse::fromClientDto).toList();
        } catch (FeignException ex) {
            throw erroDoClient(ex);
        }
    }

    public List<EmprestimoResponse> listarNaoAtrasados () {
        try {
            return emprestimoClient.listar(false).stream().map(EmprestimoResponse::fromClientDto).toList();
        } catch (FeignException ex) {
            throw erroDoClient(ex);
        }
    }

    public List<EmprestimoResponse> listarPorUsuario (Long usuarioId) {
        try {
            return emprestimoClient.listarPorUsuario(usuarioId).stream().map(EmprestimoResponse::fromClientDto)
                    .toList();
        } catch (FeignException ex) {
            throw erroDoClient(ex);
        }
    }

    public List<EmprestimoResponse> listarPorSituacaoAtraso (Boolean atrasado) {
        if (atrasado == null) {
            return obterLista();
        }
        return atrasado ? listarAtrasados() : listarNaoAtrasados();
    }

    public EmprestimoResponse registrarDevolucao (Long idEmprestimo, RegistrarDevolucaoRequest request) {
        try {
            return EmprestimoResponse.fromClientDto(emprestimoClient.registrarDevolucao(idEmprestimo,
                    new RegistrarDevolucaoClientRequest(request.dataDevolucao(), request.multa())));
        } catch (FeignException ex) {
            throw erroDoClient(ex);
        }
    }

    private EmprestimoClientRequest toRequest (EmprestimoRequest request) {
        return new EmprestimoClientRequest(request.usuarioId(), request.exemplarId(), request.dataEmprestimo(),
                request.dataEsperadaDevolucao(), request.multa());
    }

    private RuntimeException erroDoClient (FeignException ex) {
        String mensagem = extrairMensagemDoCliente(ex);

        return switch (ex.status()) {
            case 400 -> new DadosInvalidosException(mensagem);
            case 404 -> new RegistroNaoEncontradoException(mensagem);
            case 409 -> new OperacaoNaoPermitidaException(mensagem);
            default -> new ServicoIndisponivelException("Erro retornado pelo serviço de empréstimos: " + mensagem);
        };
    }

    private String extrairMensagemDoCliente (FeignException ex) {
        String corpo = ex.contentUTF8();
        if (corpo != null && !corpo.isBlank()) {
            try {
                JsonNode json = new ObjectMapper().readTree(corpo);
                if (json.has("message") && !json.get("message").isNull()) {
                    return json.get("message").asText();
                }
                if (json.has("error") && !json.get("error").isNull()) {
                    return json.get("error").asText();
                }
                return corpo;
            } catch (Exception ignored) {
                return corpo;
            }
        }
        return ex.getMessage();
    }
}

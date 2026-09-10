package br.edu.infnet.gustavo_figueiredo_api.emprestimo.integration.client;

import br.edu.infnet.gustavo_figueiredo_api.emprestimo.integration.dto.*;
import feign.*;

import java.util.*;

public interface EmprestimoClient {

    @RequestLine("POST /emprestimos")
    @Headers("Content-Type: application/json")
    EmprestimoClientResponse incluir (EmprestimoClientRequest request);

    @RequestLine("GET /emprestimos")
    @Headers("Accept: application/json")
    List<EmprestimoClientResponse> listarTodos ();

    @RequestLine("GET /emprestimos?atrasado={atrasado}")
    @Headers("Accept: application/json")
    List<EmprestimoClientResponse> listar (@Param("atrasado") Boolean atrasado);

    @RequestLine("GET /emprestimos/usuario/{usuarioId}")
    @Headers("Accept: application/json")
    List<EmprestimoClientResponse> listarPorUsuario (@Param("usuarioId") Long usuarioId);

    @RequestLine("GET /emprestimos/{id}")
    @Headers("Accept: application/json")
    EmprestimoClientResponse obterPorId (@Param("id") Long id);

    @RequestLine("PUT /emprestimos/{id}")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    EmprestimoClientResponse alterar (@Param("id") Long id, EmprestimoClientRequest request);

    @RequestLine("DELETE /emprestimos/{id}")
    @Headers("Accept: application/json")
    void excluir (@Param("id") Long id);

    @RequestLine("PATCH /emprestimos/{id}/devolucao")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    EmprestimoClientResponse registrarDevolucao (@Param("id") Long id, RegistrarDevolucaoClientRequest request);
}

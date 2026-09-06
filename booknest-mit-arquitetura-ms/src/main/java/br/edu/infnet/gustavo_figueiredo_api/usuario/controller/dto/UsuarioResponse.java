package br.edu.infnet.gustavo_figueiredo_api.usuario.controller.dto;

import br.edu.infnet.gustavo_figueiredo_api.usuario.model.Usuario;

public record UsuarioResponse(Long id, String nome, String email, String matricula, Boolean ativo,
                              long emprestimosEmAberto) {
    public static UsuarioResponse from (Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getMatricula(),
                usuario.getAtivo(), usuario.getEmprestimosEmAberto());
    }
}

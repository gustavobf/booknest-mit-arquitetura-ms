package br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.Editora;
import jakarta.validation.constraints.*;

public record EditoraRequest(
        @NotBlank(message = "Nome da editora é obrigatório.")
        @Size(max = 120, message = "Nome da editora deve ter no máximo 120 caracteres.")
        String nome,

        @NotBlank(message = "Cidade da editora é obrigatória.")
        @Size(max = 120, message = "Cidade da editora deve ter no máximo 120 caracteres.")
        String cidade,

        @NotBlank(message = "E-mail de contato é obrigatório.")
        @Email(message = "E-mail de contato inválido.")
        String emailContato,

        @NotNull(message = "Situação de atividade da editora é obrigatória.")
        Boolean ativa) {

    public Editora toEntity () {
        return toEntity(null);
    }

    public Editora toEntity (Long id) {
        Editora editora = new Editora();
        editora.setId(id);
        editora.setNome(nome);
        editora.setCidade(cidade);
        editora.setEmailContato(emailContato);
        editora.setAtiva(ativa);
        return editora;
    }
}

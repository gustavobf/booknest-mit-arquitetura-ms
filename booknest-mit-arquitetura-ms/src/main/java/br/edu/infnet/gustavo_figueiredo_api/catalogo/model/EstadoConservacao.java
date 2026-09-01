package br.edu.infnet.gustavo_figueiredo_api.catalogo.model;

import io.swagger.v3.oas.annotations.media.*;

@Schema(description = "Estado de conservação do exemplar: EXCELENTE, BOM ou REGULAR.")
public enum EstadoConservacao {
    EXCELENTE("Excelente"), BOM("Bom"), REGULAR("Regular");

    private final String descricao;

    EstadoConservacao (String descricao) {
        this.descricao = descricao;
    }

    public static EstadoConservacao fromDescricao (String valor) {
        for (EstadoConservacao estado : values()) {
            if (estado.descricao.equalsIgnoreCase(valor) || estado.name().equalsIgnoreCase(valor)) {
                return estado;
            }
        }

        throw new IllegalArgumentException("Estado de conservação inválido: " + valor);
    }

    @Override
    public String toString () {
        return descricao;
    }
}

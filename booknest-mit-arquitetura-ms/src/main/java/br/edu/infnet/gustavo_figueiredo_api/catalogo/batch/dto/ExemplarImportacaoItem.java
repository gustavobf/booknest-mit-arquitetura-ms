package br.edu.infnet.gustavo_figueiredo_api.catalogo.batch.dto;

public class ExemplarImportacaoItem {

    private String codigo;
    private String isbnLivro;
    private String estadoConservacao;

    public ExemplarImportacaoItem () {
    }

    public String getCodigo () {
        return codigo;
    }

    public void setCodigo (String codigo) {
        this.codigo = codigo;
    }

    public String getIsbnLivro () {
        return isbnLivro;
    }

    public void setIsbnLivro (String isbnLivro) {
        this.isbnLivro = isbnLivro;
    }

    public String getEstadoConservacao () {
        return estadoConservacao;
    }

    public void setEstadoConservacao (String estadoConservacao) {
        this.estadoConservacao = estadoConservacao;
    }

    @Override
    public String toString () {
        return "ExemplarImportacaoItem{" + "codigo='" + codigo + '\'' + ", isbnLivro='" + isbnLivro + '\'' + ", estadoConservacao='" + estadoConservacao + '\'' + '}';
    }
}

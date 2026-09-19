package br.edu.infnet.gustavo_figueiredo_api.catalogo.batch;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.batch.dto.*;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.*;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.repository.*;
import org.slf4j.*;
import org.springframework.batch.item.*;
import org.springframework.stereotype.*;

@Component
public class ExemplarItemProcessor implements ItemProcessor<ExemplarImportacaoItem, Exemplar> {

    private static final Logger log = LoggerFactory.getLogger(ExemplarItemProcessor.class);

    private final LivroRepository livroRepository;

    public ExemplarItemProcessor (LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Override
    public Exemplar process (ExemplarImportacaoItem item) {
        if (item.getCodigo() == null || item.getCodigo().isBlank()) {
            log.warn("Registro ignorado: código do exemplar não informado. {}", item);
            return null;
        }

        if (item.getIsbnLivro() == null || item.getIsbnLivro().isBlank()) {
            log.warn("Registro ignorado: ISBN do livro não informado. {}", item);
            return null;
        }

        var livroEncontrado = livroRepository.findByIsbn(item.getIsbnLivro().trim());
        if (livroEncontrado.isEmpty()) {
            log.warn("Registro ignorado: nenhum livro encontrado para o ISBN '{}'.", item.getIsbnLivro());
            return null;
        }

        EstadoConservacao estado;
        try {
            estado = EstadoConservacao.fromDescricao(item.getEstadoConservacao());
        } catch (IllegalArgumentException ex) {
            log.warn("Registro ignorado: estado de conservação inválido '{}'.", item.getEstadoConservacao());
            return null;
        }

        Exemplar exemplar = new Exemplar();
        exemplar.setCodigo(item.getCodigo().trim().toUpperCase());
        exemplar.setEstadoConservacao(estado);
        exemplar.setDisponivel(true);
        exemplar.setLivro(livroEncontrado.get());
        return exemplar;
    }
}

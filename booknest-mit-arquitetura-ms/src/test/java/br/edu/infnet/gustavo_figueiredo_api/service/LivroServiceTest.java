package br.edu.infnet.gustavo_figueiredo_api.service;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.Livro;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.service.AutorService;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.service.CategoriaService;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.service.EditoraService;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.service.LivroService;
import br.edu.infnet.gustavo_figueiredo_api.exception.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.transaction.annotation.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LivroServiceTest {
    @Autowired
    private LivroService livroService;
    @Autowired
    private AutorService autorService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private EditoraService editoraService;

    @Test
    void deveExecutarCrudPersistido () {
        Livro livro = criarLivro("Clean Code");
        Livro livroCriado = livroService.incluir(livro);
        Long idCriado = livroCriado.getId();

        assertEquals("Clean Code", livroService.obterPorId(idCriado).getTitulo());

        livroCriado.setTitulo("Clean Code Atualizado");
        livroService.alterar(livroCriado);

        assertEquals("Clean Code Atualizado", livroService.obterPorId(idCriado).getTitulo());

        livroService.excluir(idCriado);
        assertThrows(RegistroNaoEncontradoException.class, () -> livroService.obterPorId(idCriado));
    }

    @Test
    void deveRetornarLivrosDisponiveisComConsultaCustomizada () {
        assertFalse(livroService.listarDisponiveis().isEmpty());
        assertFalse(livroService.listarOrdenadosPorTitulo().isEmpty());
    }

    private Livro criarLivro (String titulo) {
        Livro livro = new Livro(null, titulo, "ISBN-001");
        livro.setAutor(autorService.obterPorId(1L));
        livro.setCategoria(categoriaService.obterPorId(1L));
        livro.setEditora(editoraService.obterPorId(1L));
        return livro;
    }
}

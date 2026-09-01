package br.edu.infnet.gustavo_figueiredo_api.integration;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.Exemplar;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.Livro;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.service.ExemplarService;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.service.LivroService;
import br.edu.infnet.gustavo_figueiredo_api.emprestimo.model.Emprestimo;
import br.edu.infnet.gustavo_figueiredo_api.emprestimo.service.EmprestimoService;
import br.edu.infnet.gustavo_figueiredo_api.usuario.model.Usuario;
import br.edu.infnet.gustavo_figueiredo_api.usuario.service.UsuarioService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.transaction.annotation.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(readOnly = true)
class CargaInicialBancoTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LivroService livroService;

    @Autowired
    private ExemplarService exemplarService;

    @Autowired
    private EmprestimoService emprestimoService;

    @Test
    void deveCarregarDadosIniciaisNoStartup () {
        assertEquals(5, usuarioService.obterLista().size());
        assertEquals(5, livroService.obterLista().size());
        assertEquals(8, exemplarService.obterLista().size());
        assertEquals(5, emprestimoService.obterLista().size());
    }

    @Test
    void deveManterRelacionamentosDaCargaInicial () {
        Livro horaDaEstrela = livroService.obterPorId(2L);
        Usuario maria = usuarioService.obterPorId(2L);
        Exemplar exemplarEmprestado = exemplarService.obterPorId(3L);
        Emprestimo emprestimoAberto = emprestimoService.obterPorId(2L);

        assertEquals(2, horaDaEstrela.getExemplares().size());
        assertEquals(1, horaDaEstrela.getQuantidadeExemplaresDisponiveis());
        assertEquals(1, maria.getEmprestimos().size());
        assertFalse(exemplarEmprestado.getDisponivel());
        assertFalse(emprestimoAberto.estaDevolvido());
    }
}

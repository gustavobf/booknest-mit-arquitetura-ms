package br.edu.infnet.gustavo_figueiredo_api.catalogo.repository;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findAllByOrderByTituloAsc ();

    @Query("select distinct l from Livro l join l.exemplares e where e.disponivel = true")
    List<Livro> findDisponiveis ();

    @Query("select l from Livro l where not exists (select e from Exemplar e where e.livro = l and e.disponivel = true)")
    List<Livro> findIndisponiveis ();

    List<Livro> findByAutorId (Long idAutor);
}

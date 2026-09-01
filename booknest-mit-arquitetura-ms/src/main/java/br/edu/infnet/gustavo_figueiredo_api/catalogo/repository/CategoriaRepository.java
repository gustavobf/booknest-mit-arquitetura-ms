package br.edu.infnet.gustavo_figueiredo_api.catalogo.repository;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    @Query("select c from Categoria c left join c.livros l group by c.id order by count(l) desc")
    List<Categoria> findAllOrderByQuantidadeLivrosDesc ();
}

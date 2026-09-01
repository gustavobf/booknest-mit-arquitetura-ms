package br.edu.infnet.gustavo_figueiredo_api.catalogo.repository;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    List<Autor> findAllByOrderByNomeAsc ();

    List<Autor> findByNacionalidadeIgnoreCase (String nacionalidade);
}

package br.edu.infnet.gustavo_figueiredo_api.catalogo.repository;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface EditoraRepository extends JpaRepository<Editora, Long> {
    List<Editora> findByAtivaTrue ();

    List<Editora> findByAtivaFalse ();
}

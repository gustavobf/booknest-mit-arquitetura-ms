package br.edu.infnet.gustavo_figueiredo_api.catalogo.repository;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface ExemplarRepository extends JpaRepository<Exemplar, Long> {
    List<Exemplar> findByDisponivelTrue ();

    List<Exemplar> findByDisponivelFalse ();

    List<Exemplar> findByLivroId (Long idLivro);
}

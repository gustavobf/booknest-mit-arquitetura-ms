package br.edu.infnet.gustavo_figueiredo_api.usuario.repository;

import br.edu.infnet.gustavo_figueiredo_api.usuario.model.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByAtivoTrue ();

    List<Usuario> findByAtivoFalse ();
}

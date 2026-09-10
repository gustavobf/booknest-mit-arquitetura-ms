package br.edu.infnet.booknest.emprestimo.repository;

import br.edu.infnet.booknest.emprestimo.model.*;
import org.springframework.data.jpa.repository.*;

import java.time.*;
import java.util.*;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByUsuarioId (Long usuarioId);

    @Query("select e from Emprestimo e where e.dataEsperadaDevolucao < :hoje and e.dataDevolucao is null")
    List<Emprestimo> findAtrasados (LocalDate hoje);

    @Query("select e from Emprestimo e where (e.dataDevolucao is not null or e.dataEsperadaDevolucao >= :hoje)")
    List<Emprestimo> findNaoAtrasados (LocalDate hoje);
}

package br.edu.infnet.gustavo_figueiredo_api.emprestimo.repository;

import br.edu.infnet.gustavo_figueiredo_api.emprestimo.model.*;
import org.springframework.data.jpa.repository.*;

import java.time.*;
import java.util.*;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByDataDevolucaoIsNull ();

    List<Emprestimo> findByUsuarioId (Long idUsuario);

    @Query("select e from Emprestimo e where e.dataEsperadaDevolucao is not null and coalesce(e.dataDevolucao, :dataReferencia) > e.dataEsperadaDevolucao")
    List<Emprestimo> findAtrasados (LocalDate dataReferencia);

    @Query("select e from Emprestimo e where e.dataEsperadaDevolucao is null or coalesce(e.dataDevolucao, :dataReferencia) <= e.dataEsperadaDevolucao")
    List<Emprestimo> findNaoAtrasados (LocalDate dataReferencia);
}

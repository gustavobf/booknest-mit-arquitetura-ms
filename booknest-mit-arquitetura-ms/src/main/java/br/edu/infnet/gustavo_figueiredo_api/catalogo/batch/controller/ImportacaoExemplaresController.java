package br.edu.infnet.gustavo_figueiredo_api.catalogo.batch.controller;

import br.edu.infnet.gustavo_figueiredo_api.exception.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import java.io.*;
import java.nio.file.*;

@RestController
@RequestMapping("/catalogo/exemplares/importacao")
@Tag(name = "Importação em lote de exemplares")
public class ImportacaoExemplaresController {

    private final JobLauncher jobLauncher;
    private final Job importacaoExemplaresJob;

    public ImportacaoExemplaresController (JobLauncher jobLauncher, Job importacaoExemplaresJob) {
        this.jobLauncher = jobLauncher;
        this.importacaoExemplaresJob = importacaoExemplaresJob;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Importar exemplares via CSV",
            description = "Processa em lote (Spring Batch) um arquivo CSV de exemplares no formato "
                    + "'codigo,isbnLivro,estadoConservacao', vinculando cada exemplar ao livro correspondente pelo ISBN.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Job de importação iniciado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Arquivo inválido ou ausente")})
    public ResponseEntity<String> importar (@RequestParam("arquivo") MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new DadosInvalidosException("Arquivo CSV de exemplares é obrigatório.");
        }

        try {
            Path arquivoTemporario = Files.createTempFile("importacao-exemplares-", ".csv");
            arquivo.transferTo(arquivoTemporario);

            JobParameters parametros = new JobParametersBuilder()
                    .addString("arquivo", arquivoTemporario.toAbsolutePath().toString())
                    .addLong("executadoEm", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execucao = jobLauncher.run(importacaoExemplaresJob, parametros);

            return ResponseEntity.accepted().body("Job de importação iniciado com id " + execucao.getJobId()
                    + ". Status: " + execucao.getStatus());
        } catch (IOException ex) {
            throw new DadosInvalidosException("Não foi possível processar o arquivo enviado: " + ex.getMessage());
        } catch (Exception ex) {
            throw new DadosInvalidosException("Não foi possível iniciar o job de importação: " + ex.getMessage());
        }
    }
}

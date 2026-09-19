package br.edu.infnet.gustavo_figueiredo_api.catalogo.batch;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.batch.dto.*;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.*;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.repository.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.repository.*;
import org.springframework.batch.core.step.builder.*;
import org.springframework.batch.item.*;
import org.springframework.batch.item.data.*;
import org.springframework.batch.item.data.builder.*;
import org.springframework.batch.item.file.*;
import org.springframework.batch.item.file.builder.*;
import org.springframework.batch.item.file.mapping.*;
import org.springframework.batch.item.file.transform.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.core.io.*;
import org.springframework.transaction.*;

@Configuration
public class ImportacaoExemplaresBatchConfig {

    private static final int TAMANHO_CHUNK = 5;

    @Bean
    @StepScope
    public FlatFileItemReader<ExemplarImportacaoItem> exemplarItemReader (
            @Value("#{jobParameters['arquivo']}") String caminhoArquivo) {

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("codigo", "isbnLivro", "estadoConservacao");

        BeanWrapperFieldSetMapper<ExemplarImportacaoItem> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ExemplarImportacaoItem.class);

        DefaultLineMapper<ExemplarImportacaoItem> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return new FlatFileItemReaderBuilder<ExemplarImportacaoItem>()
                .name("exemplarItemReader")
                .resource(new FileSystemResource(caminhoArquivo))
                .linesToSkip(1)
                .lineMapper(lineMapper)
                .build();
    }

    @Bean
    public RepositoryItemWriter<Exemplar> exemplarItemWriter (ExemplarRepository exemplarRepository) {
        return new RepositoryItemWriterBuilder<Exemplar>()
                .repository(exemplarRepository)
                .methodName("save")
                .build();
    }

    @Bean
    public Step importacaoExemplaresStep (JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                           FlatFileItemReader<ExemplarImportacaoItem> exemplarItemReader,
                                           ExemplarItemProcessor exemplarItemProcessor,
                                           RepositoryItemWriter<Exemplar> exemplarItemWriter) {
        return new StepBuilder("importacaoExemplaresStep", jobRepository)
                .<ExemplarImportacaoItem, Exemplar>chunk(TAMANHO_CHUNK, transactionManager)
                .reader(exemplarItemReader)
                .processor(exemplarItemProcessor)
                .writer(exemplarItemWriter)
                .build();
    }

    @Bean
    public Job importacaoExemplaresJob (JobRepository jobRepository, Step importacaoExemplaresStep) {
        return new JobBuilder("importacaoExemplaresJob", jobRepository)
                .start(importacaoExemplaresStep)
                .build();
    }
}

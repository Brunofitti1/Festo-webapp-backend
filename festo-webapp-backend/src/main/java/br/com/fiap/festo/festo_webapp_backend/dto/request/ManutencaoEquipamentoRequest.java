package br.com.fiap.festo.festo_webapp_backend.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para criação e atualização de manutenções de equipamentos
 * 
 * @author Seu Nome
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManutencaoEquipamentoRequest {

    private Long equipamentoId;

    private String tipoManutencao; // PREVENTIVA, CORRETIVA, PREDITIVA, EMERGENCIAL

    private String status; // PROGRAMADA, EM_ANDAMENTO, CONCLUIDA, CANCELADA, AGUARDANDO_PECAS

    private LocalDateTime dataProgramada;

    private LocalDateTime dataInicio;

    private LocalDateTime dataConclusao;

    private String tecnicoResponsavel;

    private String descricao;

    private String pecasSubstituidas;

    private Integer tempoParadaMinutos;

    private BigDecimal custoManutencao;

    private String observacoesTecnicas;

    private String prioridade; // BAIXA, MEDIA, ALTA, CRITICA
}

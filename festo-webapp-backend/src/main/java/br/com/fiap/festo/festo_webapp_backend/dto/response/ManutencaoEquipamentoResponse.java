package br.com.fiap.festo.festo_webapp_backend.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * DTO de resposta para manutenções de equipamentos
 * 
 * @author Seu Nome
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManutencaoEquipamentoResponse {

    private Long id;
    private Long equipamentoId;
    private String nomeEquipamento;
    private String tipoManutencao;
    private String status;
    private LocalDateTime dataProgramada;
    private LocalDateTime dataInicio;
    private LocalDateTime dataConclusao;
    private String tecnicoResponsavel;
    private String descricao;
    private String pecasSubstituidas;
    private Integer tempoParadaMinutos;
    private BigDecimal custoManutencao;
    private String observacoesTecnicas;
    private String prioridade;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Campos calculados
    private Long duracaoMinutos;
    private String statusCor; // GREEN, YELLOW, RED, BLUE
    private Boolean atrasada;
    private Integer diasAtraso;
}

package br.com.fiap.festo.festo_webapp_backend.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * DTO de resposta para leituras de sensores
 * 
 * @author Seu Nome
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeituraSensorResponse {

    private Long id;
    private Long equipamentoId;
    private String nomeEquipamento;
    private String tipoSensor;
    private Double valor;
    private String unidade;
    private Double valorMinimo;
    private Double valorMaximo;
    private Boolean alertaAtivo;
    private String nivelAlerta;
    private LocalDateTime timestampLeitura;
    private String observacoes;

    // Indicadores visuais
    private String statusCor; // GREEN, YELLOW, RED
    private String tendencia; // CRESCENTE, DECRESCENTE, ESTAVEL
}

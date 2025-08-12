package br.com.fiap.festo.festo_webapp_backend.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * DTO para registro de leituras de sensores
 * 
 * @author Seu Nome
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeituraSensorRequest {

    private Long equipamentoId;

    private String tipoSensor; // PRESSAO, TEMPERATURA, VIBRACAO, POSICAO

    private Double valor;

    private String unidade; // Bar, °C, Hz, mm

    private Double valorMinimo;

    private Double valorMaximo;

    private LocalDateTime timestampLeitura;

    private String observacoes;
}

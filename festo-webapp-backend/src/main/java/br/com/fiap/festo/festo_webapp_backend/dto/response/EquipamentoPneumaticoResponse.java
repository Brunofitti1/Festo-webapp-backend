package br.com.fiap.festo.festo_webapp_backend.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO de resposta para equipamentos pneumáticos
 * 
 * @author Seu Nome
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipamentoPneumaticoResponse {

    private Long id;
    private String nome;
    private String tipo;
    private String modelo;
    private String fabricante;
    private String numeroSerie;
    private String status;
    private Double pressaoOperacao;
    private Double temperaturaOperacao;
    private Long ciclosRealizados;
    private LocalDate dataInstalacao;
    private LocalDate dataUltimaManutencao;
    private LocalDate proximaManutencao;
    private Integer intervaloManutencaoHoras;
    private String observacoes;
    private Boolean ativo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Dados resumidos das últimas leituras
    private List<LeituraSensorResponse> ultimasLeituras;

    // Dados resumidos das últimas manutenções
    private List<ManutencaoEquipamentoResponse> ultimasManutencoes;

    // Indicadores de saúde do equipamento
    private String statusSaude; // OTIMO, BOM, ATENCAO, CRITICO
    private Double scoreConfiabilidade; // 0.0 a 1.0
    private Integer diasParaProximaManutencao;
    private Boolean alertaAtivo;
}

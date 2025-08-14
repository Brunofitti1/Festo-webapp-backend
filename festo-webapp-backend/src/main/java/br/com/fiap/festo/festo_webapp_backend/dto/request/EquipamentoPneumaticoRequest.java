package br.com.fiap.festo.festo_webapp_backend.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

/**
 * DTO para criação e atualização de equipamentos pneumáticos
 * 
 * @author Seu Nome
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipamentoPneumaticoRequest {

    // TODO: Adicionar validações quando jakarta.validation estiver disponível
    // @NotBlank(message = "Nome é obrigatório")
    // @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    // @NotBlank(message = "Tipo é obrigatório")
    // @Pattern(regexp = "CILINDRO|VALVULA|COMPRESSOR|SENSOR",
    // message = "Tipo deve ser: CILINDRO, VALVULA, COMPRESSOR ou SENSOR")
    private String tipo;

    // @Size(max = 100, message = "Modelo deve ter no máximo 100 caracteres")
    private String modelo;

    // @Size(max = 50, message = "Fabricante deve ter no máximo 50 caracteres")
    private String fabricante;

    // @Size(max = 100, message = "Número de série deve ter no máximo 100
    // caracteres")
    private String numeroSerie;

    // @NotNull(message = "Status é obrigatório")
    // @Pattern(regexp = "OPERANDO|PARADO|MANUTENCAO|FALHA|DESATIVADO",
    // message = "Status deve ser: OPERANDO, PARADO, MANUTENCAO, FALHA ou
    // DESATIVADO")
    private String status;

    // @Positive(message = "Pressão de operação deve ser positiva")
    private Double pressaoOperacao;

    private Double temperaturaOperacao;

    // @PositiveOrZero(message = "Ciclos realizados deve ser zero ou positivo")
    private Long ciclosRealizados;

    private LocalDate dataInstalacao;

    private LocalDate dataUltimaManutencao;

    private LocalDate proximaManutencao;

    // @Positive(message = "Intervalo de manutenção deve ser positivo")
    private Integer intervaloManutencaoHoras;

    // @Size(max = 1000, message = "Observações deve ter no máximo 1000 caracteres")
    private String observacoes;

    @Builder.Default
    private Boolean ativo = true;
}

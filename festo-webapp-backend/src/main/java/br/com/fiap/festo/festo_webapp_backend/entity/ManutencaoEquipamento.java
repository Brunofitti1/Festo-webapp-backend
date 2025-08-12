package br.com.fiap.festo.festo_webapp_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * Entidade para registrar manutenções dos equipamentos
 * (Preventiva, corretiva, preditiva)
 * 
 * @author Seu Nome
 * @version 1.0
 */
@Entity
@Table(name = "manutencoes_equipamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManutencaoEquipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private EquipamentoPneumatico equipamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_manutencao", nullable = false)
    private TipoManutencao tipoManutencao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusManutencao status;

    @Column(name = "data_programada")
    private LocalDateTime dataProgramada;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Column(name = "tecnico_responsavel", length = 100)
    private String tecnicoResponsavel;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "pecas_substituidas", columnDefinition = "TEXT")
    private String pecasSubstituidas;

    @Column(name = "tempo_parada_minutos")
    private Integer tempoParadaMinutos;

    @Column(name = "custo_manutencao", precision = 10, scale = 2)
    private BigDecimal custoManutencao;

    @Column(name = "observacoes_tecnicas", columnDefinition = "TEXT")
    private String observacoesTecnicas;

    @Column(name = "nivel_prioridade")
    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum TipoManutencao {
        PREVENTIVA,
        CORRETIVA,
        PREDITIVA,
        EMERGENCIAL
    }

    public enum StatusManutencao {
        PROGRAMADA,
        EM_ANDAMENTO,
        CONCLUIDA,
        CANCELADA,
        AGUARDANDO_PECAS
    }

    public enum Prioridade {
        BAIXA,
        MEDIA,
        ALTA,
        CRITICA
    }
}

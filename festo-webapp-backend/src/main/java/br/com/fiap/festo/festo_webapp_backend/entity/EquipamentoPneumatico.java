package br.com.fiap.festo.festo_webapp_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade representando um equipamento pneumático
 * (Cilindros, válvulas, compressores, etc.)
 * 
 * @author Seu Nome
 * @version 1.0
 */
@Entity
@Table(name = "equipamentos_pneumaticos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipamentoPneumatico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 50)
    private String tipo; // CILINDRO, VALVULA, COMPRESSOR, SENSOR

    @Column(length = 100)
    private String modelo;

    @Column(length = 50)
    private String fabricante;

    @Column(name = "numero_serie", length = 100)
    private String numeroSerie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEquipamento status;

    @Column(name = "pressao_operacao")
    private Double pressaoOperacao; // em Bar

    @Column(name = "temperatura_operacao")
    private Double temperaturaOperacao; // em Celsius

    @Column(name = "ciclos_realizados")
    private Long ciclosRealizados;

    @Column(name = "data_instalacao")
    private LocalDateTime dataInstalacao;

    @Column(name = "data_ultima_manutencao")
    private LocalDateTime dataUltimaManutencao;

    @Column(name = "proxima_manutencao")
    private LocalDateTime proximaManutencao;

    @Column(name = "intervalo_manutencao_horas")
    private Integer intervaloManutencaoHoras;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relacionamento com leituras de sensores
    @OneToMany(mappedBy = "equipamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LeituraSensor> leituras;

    // Relacionamento com manutenções
    @OneToMany(mappedBy = "equipamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ManutencaoEquipamento> manutencoes;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum StatusEquipamento {
        OPERANDO,
        PARADO,
        MANUTENCAO,
        FALHA,
        DESATIVADO
    }
}

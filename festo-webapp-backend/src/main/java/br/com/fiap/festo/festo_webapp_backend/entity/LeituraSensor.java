package br.com.fiap.festo.festo_webapp_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade para armazenar leituras dos sensores
 * (Pressão, temperatura, vibração, etc.)
 * 
 * @author Seu Nome
 * @version 1.0
 */
@Entity
@Table(name = "leituras_sensores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeituraSensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private EquipamentoPneumatico equipamento;

    @Column(name = "tipo_sensor", nullable = false, length = 50)
    private String tipoSensor; // PRESSAO, TEMPERATURA, VIBRACAO, POSICAO

    @Column(nullable = false)
    private Double valor;

    @Column(length = 20)
    private String unidade; // Bar, °C, Hz, mm

    @Column(name = "valor_minimo")
    private Double valorMinimo;

    @Column(name = "valor_maximo")
    private Double valorMaximo;

    @Column(name = "alerta_ativo")
    @Builder.Default
    private Boolean alertaAtivo = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_alerta")
    private NivelAlerta nivelAlerta;

    @Column(name = "timestamp_leitura", nullable = false)
    private LocalDateTime timestampLeitura;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @PrePersist
    protected void onCreate() {
        if (this.timestampLeitura == null) {
            this.timestampLeitura = LocalDateTime.now();
        }
        // Verificar se valor está dentro dos limites
        verificarAlertas();
    }

    @PreUpdate
    protected void onUpdate() {
        verificarAlertas();
    }

    private void verificarAlertas() {
        if (valorMinimo != null && valor < valorMinimo) {
            this.alertaAtivo = true;
            this.nivelAlerta = NivelAlerta.CRITICO;
        } else if (valorMaximo != null && valor > valorMaximo) {
            this.alertaAtivo = true;
            this.nivelAlerta = NivelAlerta.CRITICO;
        } else {
            this.alertaAtivo = false;
            this.nivelAlerta = null;
        }
    }

    public enum NivelAlerta {
        BAIXO,
        MEDIO,
        ALTO,
        CRITICO
    }
}

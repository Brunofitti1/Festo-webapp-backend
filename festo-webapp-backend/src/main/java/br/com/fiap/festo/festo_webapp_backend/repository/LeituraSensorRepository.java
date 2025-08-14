package br.com.fiap.festo.festo_webapp_backend.repository;

import br.com.fiap.festo.festo_webapp_backend.entity.LeituraSensor;
import br.com.fiap.festo.festo_webapp_backend.entity.EquipamentoPneumatico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para operações de leituras de sensores
 * 
 * @author Seu Nome
 * @version 1.0
 */
@Repository
public interface LeituraSensorRepository extends JpaRepository<LeituraSensor, Long> {

    // Buscar por equipamento
    List<LeituraSensor> findByEquipamentoOrderByTimestampLeituraDesc(EquipamentoPneumatico equipamento);

    // Buscar por equipamento ID
    List<LeituraSensor> findByEquipamento_IdOrderByTimestampLeituraDesc(Long equipamentoId);

    // Buscar por tipo de sensor
    List<LeituraSensor> findByTipoSensorOrderByTimestampLeituraDesc(String tipoSensor);

    // Buscar leituras com alertas ativos
    List<LeituraSensor> findByAlertaAtivoTrueOrderByTimestampLeituraDesc();

    // Buscar últimas N leituras de um equipamento
    @Query("SELECT l FROM LeituraSensor l WHERE l.equipamento.id = :equipamentoId ORDER BY l.timestampLeitura DESC")
    List<LeituraSensor> findTopNByEquipamentoId(@Param("equipamentoId") Long equipamentoId);

    // Buscar leituras por período
    @Query("SELECT l FROM LeituraSensor l WHERE l.timestampLeitura BETWEEN :inicio AND :fim ORDER BY l.timestampLeitura DESC")
    List<LeituraSensor> findByTimestampLeituraBetween(@Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    // Buscar leituras por equipamento e tipo de sensor
    List<LeituraSensor> findByEquipamento_IdAndTipoSensorOrderByTimestampLeituraDesc(Long equipamentoId,
            String tipoSensor);

    // Buscar leituras fora dos limites (potenciais alertas)
    @Query("SELECT l FROM LeituraSensor l WHERE (l.valor < l.valorMinimo OR l.valor > l.valorMaximo) AND l.valorMinimo IS NOT NULL AND l.valorMaximo IS NOT NULL ORDER BY l.timestampLeitura DESC")
    List<LeituraSensor> findLeiturasForaLimites();

    // Contar leituras por equipamento
    @Query("SELECT l.equipamento.id, COUNT(l) FROM LeituraSensor l GROUP BY l.equipamento.id")
    List<Object[]> countLeiturasByEquipamento();

    // Buscar última leitura de cada tipo por equipamento
    @Query("SELECT l FROM LeituraSensor l WHERE l.equipamento.id = :equipamentoId AND l.timestampLeitura = (SELECT MAX(l2.timestampLeitura) FROM LeituraSensor l2 WHERE l2.equipamento.id = :equipamentoId AND l2.tipoSensor = l.tipoSensor)")
    List<LeituraSensor> findUltimasLeiturasPorTipo(@Param("equipamentoId") Long equipamentoId);

    // Buscar média de valores por tipo de sensor em um período
    @Query("SELECT l.tipoSensor, AVG(l.valor) FROM LeituraSensor l WHERE l.equipamento.id = :equipamentoId AND l.timestampLeitura BETWEEN :inicio AND :fim GROUP BY l.tipoSensor")
    List<Object[]> findMediaValoresPorTipo(@Param("equipamentoId") Long equipamentoId,
            @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    // Métodos com paginação
    Page<LeituraSensor> findByEquipamento_IdOrderByTimestampLeituraDesc(Long equipamentoId, Pageable pageable);

    Page<LeituraSensor> findByTipoSensorOrderByTimestampLeituraDesc(String tipoSensor, Pageable pageable);

    Page<LeituraSensor> findByTimestampLeituraBetweenOrderByTimestampLeituraDesc(LocalDateTime inicio,
            LocalDateTime fim, Pageable pageable);

    // Buscar leituras críticas
    @Query("SELECT l FROM LeituraSensor l WHERE l.alertaAtivo = true AND l.nivelAlerta = 'CRITICO' ORDER BY l.timestampLeitura DESC")
    List<LeituraSensor> findLeiturasCriticas();

    @Query("SELECT l FROM LeituraSensor l WHERE l.equipamento.id = :equipamentoId AND l.alertaAtivo = true AND l.nivelAlerta = 'CRITICO' ORDER BY l.timestampLeitura DESC")
    List<LeituraSensor> findLeiturasCriticasPorEquipamento(@Param("equipamentoId") Long equipamentoId);

    // Calcular média específica
    @Query("SELECT AVG(l.valor) FROM LeituraSensor l WHERE l.equipamento.id = :equipamentoId AND l.tipoSensor = :tipoSensor AND l.timestampLeitura BETWEEN :inicio AND :fim")
    Double calcularMediaPorPeriodo(@Param("equipamentoId") Long equipamentoId, @Param("tipoSensor") String tipoSensor,
            @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    // Buscar por limites de valor
    List<LeituraSensor> findByTipoSensorAndValorGreaterThanOrderByTimestampLeituraDesc(String tipoSensor,
            Double valorLimite);

    List<LeituraSensor> findByTipoSensorAndValorLessThanOrderByTimestampLeituraDesc(String tipoSensor,
            Double valorLimite);

    // Excluir leituras antigas
    @Modifying
    @Query("DELETE FROM LeituraSensor l WHERE l.timestampLeitura < :dataLimite")
    int deleteByTimestampLeituraBefore(@Param("dataLimite") LocalDateTime dataLimite);

    // Estatísticas detalhadas
    @Query("SELECT l.tipoSensor, COUNT(l), MIN(l.valor), MAX(l.valor), AVG(l.valor) FROM LeituraSensor l WHERE l.equipamento.id = :equipamentoId AND l.timestampLeitura BETWEEN :inicio AND :fim GROUP BY l.tipoSensor")
    List<Object[]> getEstatisticasLeituras(@Param("equipamentoId") Long equipamentoId,
            @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    // Verificar existência
    boolean existsByEquipamento_Id(Long equipamentoId);
}

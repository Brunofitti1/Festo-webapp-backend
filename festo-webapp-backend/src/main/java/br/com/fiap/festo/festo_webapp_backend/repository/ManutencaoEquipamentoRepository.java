package br.com.fiap.festo.festo_webapp_backend.repository;

import br.com.fiap.festo.festo_webapp_backend.entity.ManutencaoEquipamento;
import br.com.fiap.festo.festo_webapp_backend.entity.ManutencaoEquipamento.StatusManutencao;
import br.com.fiap.festo.festo_webapp_backend.entity.ManutencaoEquipamento.TipoManutencao;
import br.com.fiap.festo.festo_webapp_backend.entity.ManutencaoEquipamento.Prioridade;
import br.com.fiap.festo.festo_webapp_backend.entity.EquipamentoPneumatico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para operações de manutenções de equipamentos
 * 
 * @author Seu Nome
 * @version 1.0
 */
@Repository
public interface ManutencaoEquipamentoRepository extends JpaRepository<ManutencaoEquipamento, Long> {

    // Buscar por equipamento
    List<ManutencaoEquipamento> findByEquipamentoOrderByCreatedAtDesc(EquipamentoPneumatico equipamento);

    // Buscar por equipamento ID
    List<ManutencaoEquipamento> findByEquipamento_IdOrderByCreatedAtDesc(Long equipamentoId);

    // Buscar por status
    List<ManutencaoEquipamento> findByStatusOrderByCreatedAtDesc(StatusManutencao status);

    // Buscar por tipo de manutenção
    List<ManutencaoEquipamento> findByTipoManutencaoOrderByCreatedAtDesc(TipoManutencao tipoManutencao);

    // Buscar por prioridade
    List<ManutencaoEquipamento> findByPrioridadeOrderByCreatedAtDesc(Prioridade prioridade);

    // Buscar por técnico responsável
    List<ManutencaoEquipamento> findByTecnicoResponsavelContainingIgnoreCaseOrderByCreatedAtDesc(String tecnico);

    // Buscar manutenções vencidas (data programada passou e status ainda é
    // PROGRAMADA)
    @Query("SELECT m FROM ManutencaoEquipamento m WHERE m.dataProgramada < :dataAtual AND m.status = 'PROGRAMADA'")
    List<ManutencaoEquipamento> findManutencoesVencidas(@Param("dataAtual") LocalDateTime dataAtual);

    // Buscar manutenções programadas para um período
    @Query("SELECT m FROM ManutencaoEquipamento m WHERE m.dataProgramada BETWEEN :inicio AND :fim ORDER BY m.dataProgramada ASC")
    List<ManutencaoEquipamento> findManutencoesProgamadasPorPeriodo(@Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    // Buscar manutenções em andamento
    List<ManutencaoEquipamento> findByStatusAndDataInicioIsNotNullAndDataConclusaoIsNullOrderByDataInicioDesc(
            StatusManutencao status);

    // Buscar manutenções concluídas em um período
    @Query("SELECT m FROM ManutencaoEquipamento m WHERE m.status = 'CONCLUIDA' AND m.dataConclusao BETWEEN :inicio AND :fim ORDER BY m.dataConclusao DESC")
    List<ManutencaoEquipamento> findManutencoesConcluidasPorPeriodo(@Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    // Contar manutenções por status
    @Query("SELECT m.status, COUNT(m) FROM ManutencaoEquipamento m GROUP BY m.status")
    List<Object[]> countManutencoesByStatus();

    // Contar manutenções por tipo
    @Query("SELECT m.tipoManutencao, COUNT(m) FROM ManutencaoEquipamento m GROUP BY m.tipoManutencao")
    List<Object[]> countManutencoesByTipo();

    // Buscar próximas manutenções (próximos N dias)
    @Query("SELECT m FROM ManutencaoEquipamento m WHERE m.dataProgramada BETWEEN :dataAtual AND :dataLimite AND m.status = 'PROGRAMADA' ORDER BY m.dataProgramada ASC")
    List<ManutencaoEquipamento> findProximasManutencoes(@Param("dataAtual") LocalDateTime dataAtual,
            @Param("dataLimite") LocalDateTime dataLimite);

    // Buscar tempo médio de manutenção por tipo
    @Query("SELECT m.tipoManutencao, AVG(m.tempoParadaMinutos) FROM ManutencaoEquipamento m WHERE m.tempoParadaMinutos IS NOT NULL GROUP BY m.tipoManutencao")
    List<Object[]> findTempoMedioManutencaoPorTipo();

    // Buscar custo total de manutenções por equipamento
    @Query("SELECT m.equipamento.id, SUM(m.custoManutencao) FROM ManutencaoEquipamento m WHERE m.custoManutencao IS NOT NULL GROUP BY m.equipamento.id")
    List<Object[]> findCustoTotalPorEquipamento();

    // Buscar última manutenção de cada equipamento
    @Query("SELECT m FROM ManutencaoEquipamento m WHERE m.dataConclusao = (SELECT MAX(m2.dataConclusao) FROM ManutencaoEquipamento m2 WHERE m2.equipamento.id = m.equipamento.id AND m2.status = 'CONCLUIDA')")
    List<ManutencaoEquipamento> findUltimaManutencaoPorEquipamento();

    // Buscar manutenções críticas não iniciadas
    @Query("SELECT m FROM ManutencaoEquipamento m WHERE m.prioridade = 'CRITICA' AND m.status = 'PROGRAMADA' ORDER BY m.dataProgramada ASC")
    List<ManutencaoEquipamento> findManutencoesCriticasNaoIniciadas();
}

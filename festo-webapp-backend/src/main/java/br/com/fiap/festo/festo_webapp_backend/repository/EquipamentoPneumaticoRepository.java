package br.com.fiap.festo.festo_webapp_backend.repository;

import br.com.fiap.festo.festo_webapp_backend.entity.EquipamentoPneumatico;
import br.com.fiap.festo.festo_webapp_backend.entity.EquipamentoPneumatico.StatusEquipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de equipamentos pneumáticos
 * 
 * @author Seu Nome
 * @version 1.0
 */
@Repository
public interface EquipamentoPneumaticoRepository extends JpaRepository<EquipamentoPneumatico, Long> {

    // Buscar equipamentos ativos
    List<EquipamentoPneumatico> findByAtivoTrue();

    // Buscar por status
    List<EquipamentoPneumatico> findByStatusAndAtivoTrue(StatusEquipamento status);

    // Buscar por tipo
    List<EquipamentoPneumatico> findByTipoAndAtivoTrue(String tipo);

    // Buscar por fabricante
    List<EquipamentoPneumatico> findByFabricanteContainingIgnoreCaseAndAtivoTrue(String fabricante);

    // Buscar por nome
    List<EquipamentoPneumatico> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    // Buscar equipamentos que precisam de manutenção
    @Query("SELECT e FROM EquipamentoPneumatico e WHERE e.proximaManutencao <= :dataLimite AND e.ativo = true")
    List<EquipamentoPneumatico> findEquipamentosComManutencaoVencida(@Param("dataLimite") LocalDate dataLimite);

    // Buscar equipamentos com manutenção próxima (próximos N dias)
    @Query("SELECT e FROM EquipamentoPneumatico e WHERE e.proximaManutencao BETWEEN :dataInicio AND :dataFim AND e.ativo = true")
    List<EquipamentoPneumatico> findEquipamentosComManutencaoProxima(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim);

    // Buscar por número de série
    Optional<EquipamentoPneumatico> findByNumeroSerieAndAtivoTrue(String numeroSerie);

    // Contar equipamentos por status
    @Query("SELECT e.status, COUNT(e) FROM EquipamentoPneumatico e WHERE e.ativo = true GROUP BY e.status")
    List<Object[]> countEquipamentosByStatus();

    // Contar equipamentos por tipo
    @Query("SELECT e.tipo, COUNT(e) FROM EquipamentoPneumatico e WHERE e.ativo = true GROUP BY e.tipo")
    List<Object[]> countEquipamentosByTipo();

    // Buscar equipamentos com mais ciclos
    @Query("SELECT e FROM EquipamentoPneumatico e WHERE e.ativo = true ORDER BY e.ciclosRealizados DESC")
    List<EquipamentoPneumatico> findEquipamentosOrderByCiclos();

    // Buscar equipamentos críticos (com falha ou em manutenção)
    @Query("SELECT e FROM EquipamentoPneumatico e WHERE e.status IN ('FALHA', 'MANUTENCAO') AND e.ativo = true")
    List<EquipamentoPneumatico> findEquipamentosCriticos();
}

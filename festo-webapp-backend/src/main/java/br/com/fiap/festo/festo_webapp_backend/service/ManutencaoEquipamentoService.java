package br.com.fiap.festo.festo_webapp_backend.service;

import br.com.fiap.festo.festo_webapp_backend.dto.request.ManutencaoEquipamentoRequest;
import br.com.fiap.festo.festo_webapp_backend.dto.response.ManutencaoEquipamentoResponse;
import br.com.fiap.festo.festo_webapp_backend.entity.ManutencaoEquipamento.StatusManutencao;
import br.com.fiap.festo.festo_webapp_backend.entity.ManutencaoEquipamento.TipoManutencao;
import br.com.fiap.festo.festo_webapp_backend.entity.ManutencaoEquipamento.Prioridade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface de serviço para gerenciamento de manutenções de equipamentos
 *
 * @author Seu Nome
 * @version 1.0
 */
public interface ManutencaoEquipamentoService {

    /**
     * Cria uma nova manutenção
     *
     * @param request dados da manutenção
     * @return manutenção criada
     */
    ManutencaoEquipamentoResponse save(ManutencaoEquipamentoRequest request);

    /**
     * Busca manutenção por ID
     *
     * @param id identificador da manutenção
     * @return manutenção encontrada
     */
    Optional<ManutencaoEquipamentoResponse> findById(Long id);

    /**
     * Lista todas as manutenções com paginação
     *
     * @param pageable configuração da paginação
     * @return página de manutenções
     */
    Page<ManutencaoEquipamentoResponse> findAll(Pageable pageable);

    /**
     * Busca manutenções por equipamento
     *
     * @param equipamentoId ID do equipamento
     * @param pageable      configuração da paginação
     * @return página de manutenções do equipamento
     */
    Page<ManutencaoEquipamentoResponse> findByEquipamento(Long equipamentoId, Pageable pageable);

    /**
     * Busca manutenções por status
     *
     * @param status   status da manutenção
     * @param pageable configuração da paginação
     * @return página de manutenções com o status
     */
    Page<ManutencaoEquipamentoResponse> findByStatus(StatusManutencao status, Pageable pageable);

    /**
     * Busca manutenções por tipo
     *
     * @param tipo     tipo da manutenção
     * @param pageable configuração da paginação
     * @return página de manutenções do tipo
     */
    Page<ManutencaoEquipamentoResponse> findByTipo(TipoManutencao tipo, Pageable pageable);

    /**
     * Busca manutenções por prioridade
     *
     * @param prioridade prioridade da manutenção
     * @param pageable   configuração da paginação
     * @return página de manutenções com a prioridade
     */
    Page<ManutencaoEquipamentoResponse> findByPrioridade(Prioridade prioridade, Pageable pageable);

    /**
     * Busca manutenções por técnico responsável
     *
     * @param tecnico  nome do técnico
     * @param pageable configuração da paginação
     * @return página de manutenções do técnico
     */
    Page<ManutencaoEquipamentoResponse> findByTecnico(String tecnico, Pageable pageable);

    /**
     * Busca manutenções vencidas
     *
     * @return lista de manutenções vencidas
     */
    List<ManutencaoEquipamentoResponse> findManutencoesVencidas();

    /**
     * Busca manutenções programadas para um período
     *
     * @param inicio data/hora inicial
     * @param fim    data/hora final
     * @return lista de manutenções no período
     */
    List<ManutencaoEquipamentoResponse> findManutencoesProgamadasPorPeriodo(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Busca próximas manutenções (próximos N dias)
     *
     * @param diasAfrente número de dias à frente
     * @return lista das próximas manutenções
     */
    List<ManutencaoEquipamentoResponse> findProximasManutencoes(int diasAfrente);

    /**
     * Busca manutenções críticas não iniciadas
     *
     * @return lista de manutenções críticas
     */
    List<ManutencaoEquipamentoResponse> findManutencoesCriticasNaoIniciadas();

    /**
     * Inicia uma manutenção
     *
     * @param id                 ID da manutenção
     * @param tecnicoResponsavel técnico que iniciará a manutenção
     * @return manutenção atualizada
     */
    Optional<ManutencaoEquipamentoResponse> iniciarManutencao(Long id, String tecnicoResponsavel);

    /**
     * Conclui uma manutenção
     *
     * @param id              ID da manutenção
     * @param observacoes     observações da conclusão
     * @param custoManutencao custo da manutenção
     * @return manutenção atualizada
     */
    Optional<ManutencaoEquipamentoResponse> concluirManutencao(Long id, String observacoes, Double custoManutencao);

    /**
     * Cancela uma manutenção
     *
     * @param id                 ID da manutenção
     * @param motivoCancelamento motivo do cancelamento
     * @return manutenção atualizada
     */
    Optional<ManutencaoEquipamentoResponse> cancelarManutencao(Long id, String motivoCancelamento);

    /**
     * Reagenda uma manutenção
     *
     * @param id       ID da manutenção
     * @param novaData nova data programada
     * @param motivo   motivo do reagendamento
     * @return manutenção atualizada
     */
    Optional<ManutencaoEquipamentoResponse> reagendarManutencao(Long id, LocalDateTime novaData, String motivo);

    /**
     * Busca estatísticas de manutenções por status
     *
     * @return mapa com contadores por status
     */
    Map<StatusManutencao, Long> getEstatisticasPorStatus();

    /**
     * Busca estatísticas de manutenções por tipo
     *
     * @return mapa com contadores por tipo
     */
    Map<TipoManutencao, Long> getEstatisticasPorTipo();

    /**
     * Calcula tempo médio de manutenção por tipo
     *
     * @return mapa com tempo médio por tipo
     */
    Map<TipoManutencao, Double> getTempoMedioManutencaoPorTipo();

    /**
     * Calcula custo total de manutenções por equipamento
     *
     * @return mapa com custo total por equipamento
     */
    Map<Long, Double> getCustoTotalPorEquipamento();

    /**
     * Busca última manutenção de cada equipamento
     *
     * @return lista das últimas manutenções
     */
    List<ManutencaoEquipamentoResponse> getUltimaManutencaoPorEquipamento();

    /**
     * Atualiza uma manutenção existente
     *
     * @param id      ID da manutenção
     * @param request novos dados
     * @return manutenção atualizada
     */
    Optional<ManutencaoEquipamentoResponse> update(Long id, ManutencaoEquipamentoRequest request);

    /**
     * Exclui uma manutenção
     *
     * @param id ID da manutenção
     */
    void delete(Long id);

    /**
     * Verifica se existe manutenção para um equipamento
     *
     * @param equipamentoId ID do equipamento
     * @return true se existir manutenções
     */
    boolean existsByEquipamento(Long equipamentoId);
}

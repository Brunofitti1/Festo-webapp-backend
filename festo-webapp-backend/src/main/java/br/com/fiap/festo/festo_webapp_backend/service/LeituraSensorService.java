package br.com.fiap.festo.festo_webapp_backend.service;

import br.com.fiap.festo.festo_webapp_backend.dto.request.LeituraSensorRequest;
import br.com.fiap.festo.festo_webapp_backend.dto.response.LeituraSensorResponse;
import br.com.fiap.festo.festo_webapp_backend.entity.LeituraSensor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço para gerenciamento de leituras de sensores
 *
 * @author Seu Nome
 * @version 1.0
 */
public interface LeituraSensorService {

    /**
     * Salva uma nova leitura de sensor
     *
     * @param request dados da leitura
     * @return leitura salva
     */
    LeituraSensorResponse save(LeituraSensorRequest request);

    /**
     * Busca leitura por ID
     *
     * @param id identificador da leitura
     * @return leitura encontrada
     */
    Optional<LeituraSensorResponse> findById(Long id);

    /**
     * Lista todas as leituras com paginação
     *
     * @param pageable configuração da paginação
     * @return página de leituras
     */
    Page<LeituraSensorResponse> findAll(Pageable pageable);

    /**
     * Busca leituras por equipamento
     *
     * @param equipamentoId ID do equipamento
     * @param pageable      configuração da paginação
     * @return página de leituras do equipamento
     */
    Page<LeituraSensorResponse> findByEquipamento(Long equipamentoId, Pageable pageable);

    /**
     * Busca leituras por tipo de sensor
     *
     * @param tipoSensor tipo do sensor
     * @param pageable   configuração da paginação
     * @return página de leituras do tipo
     */
    Page<LeituraSensorResponse> findByTipoSensor(String tipoSensor, Pageable pageable);

    /**
     * Busca leituras em um período
     *
     * @param inicio   data/hora inicial
     * @param fim      data/hora final
     * @param pageable configuração da paginação
     * @return página de leituras no período
     */
    Page<LeituraSensorResponse> findByPeriodo(LocalDateTime inicio, LocalDateTime fim, Pageable pageable);

    /**
     * Busca leituras críticas (fora dos parâmetros normais)
     *
     * @param equipamentoId ID do equipamento (opcional)
     * @return lista de leituras críticas
     */
    List<LeituraSensorResponse> findLeiturasCriticas(Long equipamentoId);

    /**
     * Busca últimas leituras de cada tipo de sensor para um equipamento
     *
     * @param equipamentoId ID do equipamento
     * @return lista das últimas leituras por tipo
     */
    List<LeituraSensorResponse> findUltimasLeiturasPorTipo(Long equipamentoId);

    /**
     * Calcula média de valores em um período
     *
     * @param equipamentoId ID do equipamento
     * @param tipoSensor    tipo do sensor
     * @param inicio        data/hora inicial
     * @param fim           data/hora final
     * @return valor médio
     */
    Double calcularMediaPorPeriodo(Long equipamentoId, String tipoSensor, LocalDateTime inicio, LocalDateTime fim);

    /**
     * Busca leituras com valores acima do limite
     *
     * @param tipoSensor  tipo do sensor
     * @param valorLimite valor limite
     * @return lista de leituras acima do limite
     */
    List<LeituraSensorResponse> findLeiturasCimaLimite(String tipoSensor, Double valorLimite);

    /**
     * Busca leituras com valores abaixo do limite
     *
     * @param tipoSensor  tipo do sensor
     * @param valorLimite valor limite
     * @return lista de leituras abaixo do limite
     */
    List<LeituraSensorResponse> findLeiturasAbaixoLimite(String tipoSensor, Double valorLimite);

    /**
     * Exclui leituras antigas (mais antigas que a data especificada)
     *
     * @param dataLimite data limite para exclusão
     * @return número de registros excluídos
     */
    int deleteLeituraAntigas(LocalDateTime dataLimite);

    /**
     * Busca estatísticas de leituras por equipamento
     *
     * @param equipamentoId ID do equipamento
     * @param inicio        data/hora inicial
     * @param fim           data/hora final
     * @return estatísticas das leituras
     */
    Object getEstatisticasLeituras(Long equipamentoId, LocalDateTime inicio, LocalDateTime fim);

    /**
     * Atualiza uma leitura existente
     *
     * @param id      ID da leitura
     * @param request novos dados
     * @return leitura atualizada
     */
    Optional<LeituraSensorResponse> update(Long id, LeituraSensorRequest request);

    /**
     * Exclui uma leitura
     *
     * @param id ID da leitura
     */
    void delete(Long id);

    /**
     * Verifica se existe leitura para um equipamento
     *
     * @param equipamentoId ID do equipamento
     * @return true se existir leituras
     */
    boolean existsByEquipamento(Long equipamentoId);
}

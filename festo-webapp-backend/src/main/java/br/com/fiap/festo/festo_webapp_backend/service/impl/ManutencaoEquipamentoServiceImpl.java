package br.com.fiap.festo.festo_webapp_backend.service.impl;

import br.com.fiap.festo.festo_webapp_backend.dto.request.ManutencaoEquipamentoRequest;
import br.com.fiap.festo.festo_webapp_backend.dto.response.ManutencaoEquipamentoResponse;
import br.com.fiap.festo.festo_webapp_backend.entity.ManutencaoEquipamento;
import br.com.fiap.festo.festo_webapp_backend.entity.ManutencaoEquipamento.StatusManutencao;
import br.com.fiap.festo.festo_webapp_backend.entity.ManutencaoEquipamento.TipoManutencao;
import br.com.fiap.festo.festo_webapp_backend.entity.ManutencaoEquipamento.Prioridade;
import br.com.fiap.festo.festo_webapp_backend.entity.EquipamentoPneumatico;
import br.com.fiap.festo.festo_webapp_backend.repository.ManutencaoEquipamentoRepository;
import br.com.fiap.festo.festo_webapp_backend.repository.EquipamentoPneumaticoRepository;
import br.com.fiap.festo.festo_webapp_backend.service.ManutencaoEquipamentoService;
import br.com.fiap.festo.festo_webapp_backend.mapper.ManutencaoEquipamentoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do serviço para gerenciamento de manutenções de equipamentos
 *
 * @author Seu Nome
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ManutencaoEquipamentoServiceImpl implements ManutencaoEquipamentoService {

    private final ManutencaoEquipamentoRepository manutencaoRepository;
    private final EquipamentoPneumaticoRepository equipamentoRepository;
    private final ManutencaoEquipamentoMapper mapper;

    @Override
    public ManutencaoEquipamentoResponse save(ManutencaoEquipamentoRequest request) {
        log.info("Criando nova manutenção para equipamento ID: {}", request.getEquipamentoId());

        EquipamentoPneumatico equipamento = equipamentoRepository.findById(request.getEquipamentoId())
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado: " + request.getEquipamentoId()));

        ManutencaoEquipamento manutencao = mapper.toEntity(request);
        manutencao.setEquipamento(equipamento);

        ManutencaoEquipamento savedManutencao = manutencaoRepository.save(manutencao);
        log.info("Manutenção criada com sucesso. ID: {}", savedManutencao.getId());

        return mapper.toResponse(savedManutencao);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ManutencaoEquipamentoResponse> findById(Long id) {
        log.debug("Buscando manutenção por ID: {}", id);
        return manutencaoRepository.findById(id)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManutencaoEquipamentoResponse> findAll(Pageable pageable) {
        log.debug("Listando todas as manutenções com paginação: {}", pageable);
        Page<ManutencaoEquipamento> manutencoes = manutencaoRepository.findAll(pageable);
        return manutencoes.map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManutencaoEquipamentoResponse> findByEquipamento(Long equipamentoId, Pageable pageable) {
        log.debug("Buscando manutenções do equipamento ID: {}", equipamentoId);
        List<ManutencaoEquipamento> manutencoes = manutencaoRepository
                .findByEquipamento_IdOrderByCreatedAtDesc(equipamentoId);
        return convertListToPage(manutencoes, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManutencaoEquipamentoResponse> findByStatus(StatusManutencao status, Pageable pageable) {
        log.debug("Buscando manutenções com status: {}", status);
        List<ManutencaoEquipamento> manutencoes = manutencaoRepository.findByStatusOrderByCreatedAtDesc(status);
        return convertListToPage(manutencoes, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManutencaoEquipamentoResponse> findByTipo(TipoManutencao tipo, Pageable pageable) {
        log.debug("Buscando manutenções do tipo: {}", tipo);
        List<ManutencaoEquipamento> manutencoes = manutencaoRepository.findByTipoManutencaoOrderByCreatedAtDesc(tipo);
        return convertListToPage(manutencoes, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManutencaoEquipamentoResponse> findByPrioridade(Prioridade prioridade, Pageable pageable) {
        log.debug("Buscando manutenções com prioridade: {}", prioridade);
        List<ManutencaoEquipamento> manutencoes = manutencaoRepository.findByPrioridadeOrderByCreatedAtDesc(prioridade);
        return convertListToPage(manutencoes, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManutencaoEquipamentoResponse> findByTecnico(String tecnico, Pageable pageable) {
        log.debug("Buscando manutenções do técnico: {}", tecnico);
        List<ManutencaoEquipamento> manutencoes = manutencaoRepository
                .findByTecnicoResponsavelContainingIgnoreCaseOrderByCreatedAtDesc(tecnico);
        return convertListToPage(manutencoes, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManutencaoEquipamentoResponse> findManutencoesVencidas() {
        log.debug("Buscando manutenções vencidas");
        List<ManutencaoEquipamento> manutencoes = manutencaoRepository.findManutencoesVencidas(LocalDateTime.now());
        return manutencoes.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManutencaoEquipamentoResponse> findManutencoesProgamadasPorPeriodo(LocalDateTime inicio,
            LocalDateTime fim) {
        log.debug("Buscando manutenções programadas entre {} e {}", inicio, fim);
        List<ManutencaoEquipamento> manutencoes = manutencaoRepository.findManutencoesProgamadasPorPeriodo(inicio, fim);
        return manutencoes.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManutencaoEquipamentoResponse> findProximasManutencoes(int diasAfrente) {
        log.debug("Buscando manutenções para os próximos {} dias", diasAfrente);
        LocalDateTime dataAtual = LocalDateTime.now();
        LocalDateTime dataLimite = dataAtual.plusDays(diasAfrente);
        List<ManutencaoEquipamento> manutencoes = manutencaoRepository.findProximasManutencoes(dataAtual, dataLimite);
        return manutencoes.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManutencaoEquipamentoResponse> findManutencoesCriticasNaoIniciadas() {
        log.debug("Buscando manutenções críticas não iniciadas");
        List<ManutencaoEquipamento> manutencoes = manutencaoRepository.findManutencoesCriticasNaoIniciadas();
        return manutencoes.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ManutencaoEquipamentoResponse> iniciarManutencao(Long id, String tecnicoResponsavel) {
        log.info("Iniciando manutenção ID: {} com técnico: {}", id, tecnicoResponsavel);

        return manutencaoRepository.findById(id)
                .map(manutencao -> {
                    if (manutencao.getStatus() != StatusManutencao.PROGRAMADA) {
                        throw new RuntimeException(
                                "Manutenção não pode ser iniciada. Status atual: " + manutencao.getStatus());
                    }

                    manutencao.setStatus(StatusManutencao.EM_ANDAMENTO);
                    manutencao.setDataInicio(LocalDateTime.now());
                    manutencao.setTecnicoResponsavel(tecnicoResponsavel);

                    ManutencaoEquipamento savedManutencao = manutencaoRepository.save(manutencao);
                    log.info("Manutenção iniciada com sucesso. ID: {}", savedManutencao.getId());

                    return mapper.toResponse(savedManutencao);
                });
    }

    @Override
    public Optional<ManutencaoEquipamentoResponse> concluirManutencao(Long id, String observacoes,
            Double custoManutencao) {
        log.info("Concluindo manutenção ID: {}", id);

        return manutencaoRepository.findById(id)
                .map(manutencao -> {
                    if (manutencao.getStatus() != StatusManutencao.EM_ANDAMENTO) {
                        throw new RuntimeException(
                                "Manutenção não pode ser concluída. Status atual: " + manutencao.getStatus());
                    }

                    manutencao.setStatus(StatusManutencao.CONCLUIDA);
                    manutencao.setDataConclusao(LocalDateTime.now());
                    manutencao.setObservacoesTecnicas(observacoes);

                    if (custoManutencao != null) {
                        manutencao.setCustoManutencao(java.math.BigDecimal.valueOf(custoManutencao));
                    }

                    // Calcular tempo de parada se há data de início
                    if (manutencao.getDataInicio() != null) {
                        long duracao = java.time.Duration
                                .between(manutencao.getDataInicio(), manutencao.getDataConclusao()).toMinutes();
                        manutencao.setTempoParadaMinutos((int) duracao);
                    }

                    ManutencaoEquipamento savedManutencao = manutencaoRepository.save(manutencao);
                    log.info("Manutenção concluída com sucesso. ID: {}", savedManutencao.getId());

                    return mapper.toResponse(savedManutencao);
                });
    }

    @Override
    public Optional<ManutencaoEquipamentoResponse> cancelarManutencao(Long id, String motivoCancelamento) {
        log.info("Cancelando manutenção ID: {} - Motivo: {}", id, motivoCancelamento);

        return manutencaoRepository.findById(id)
                .map(manutencao -> {
                    if (manutencao.getStatus() == StatusManutencao.CONCLUIDA) {
                        throw new RuntimeException("Manutenção já concluída não pode ser cancelada");
                    }

                    manutencao.setStatus(StatusManutencao.CANCELADA);
                    manutencao.setObservacoesTecnicas(motivoCancelamento);

                    ManutencaoEquipamento savedManutencao = manutencaoRepository.save(manutencao);
                    log.info("Manutenção cancelada com sucesso. ID: {}", savedManutencao.getId());

                    return mapper.toResponse(savedManutencao);
                });
    }

    @Override
    public Optional<ManutencaoEquipamentoResponse> reagendarManutencao(Long id, LocalDateTime novaData, String motivo) {
        log.info("Reagendando manutenção ID: {} para {}", id, novaData);

        return manutencaoRepository.findById(id)
                .map(manutencao -> {
                    if (manutencao.getStatus() != StatusManutencao.PROGRAMADA) {
                        throw new RuntimeException(
                                "Apenas manutenções programadas podem ser reagendadas. Status atual: "
                                        + manutencao.getStatus());
                    }

                    manutencao.setDataProgramada(novaData);
                    manutencao.setObservacoesTecnicas(motivo);

                    ManutencaoEquipamento savedManutencao = manutencaoRepository.save(manutencao);
                    log.info("Manutenção reagendada com sucesso. ID: {}", savedManutencao.getId());

                    return mapper.toResponse(savedManutencao);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Map<StatusManutencao, Long> getEstatisticasPorStatus() {
        log.debug("Gerando estatísticas por status");
        List<Object[]> results = manutencaoRepository.countManutencoesByStatus();
        Map<StatusManutencao, Long> estatisticas = new HashMap<>();

        for (Object[] result : results) {
            StatusManutencao status = (StatusManutencao) result[0];
            Long count = (Long) result[1];
            estatisticas.put(status, count);
        }

        return estatisticas;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<TipoManutencao, Long> getEstatisticasPorTipo() {
        log.debug("Gerando estatísticas por tipo");
        List<Object[]> results = manutencaoRepository.countManutencoesByTipo();
        Map<TipoManutencao, Long> estatisticas = new HashMap<>();

        for (Object[] result : results) {
            TipoManutencao tipo = (TipoManutencao) result[0];
            Long count = (Long) result[1];
            estatisticas.put(tipo, count);
        }

        return estatisticas;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<TipoManutencao, Double> getTempoMedioManutencaoPorTipo() {
        log.debug("Calculando tempo médio por tipo");
        List<Object[]> results = manutencaoRepository.findTempoMedioManutencaoPorTipo();
        Map<TipoManutencao, Double> tempos = new HashMap<>();

        for (Object[] result : results) {
            TipoManutencao tipo = (TipoManutencao) result[0];
            Double tempoMedio = (Double) result[1];
            tempos.put(tipo, tempoMedio);
        }

        return tempos;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Double> getCustoTotalPorEquipamento() {
        log.debug("Calculando custo total por equipamento");
        List<Object[]> results = manutencaoRepository.findCustoTotalPorEquipamento();
        Map<Long, Double> custos = new HashMap<>();

        for (Object[] result : results) {
            Long equipamentoId = (Long) result[0];
            Double custoTotal = result[1] != null ? ((java.math.BigDecimal) result[1]).doubleValue() : 0.0;
            custos.put(equipamentoId, custoTotal);
        }

        return custos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManutencaoEquipamentoResponse> getUltimaManutencaoPorEquipamento() {
        log.debug("Buscando última manutenção por equipamento");
        List<ManutencaoEquipamento> manutencoes = manutencaoRepository.findUltimaManutencaoPorEquipamento();
        return manutencoes.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ManutencaoEquipamentoResponse> update(Long id, ManutencaoEquipamentoRequest request) {
        log.info("Atualizando manutenção ID: {}", id);

        return manutencaoRepository.findById(id)
                .map(manutencao -> {
                    EquipamentoPneumatico equipamento = equipamentoRepository.findById(request.getEquipamentoId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Equipamento não encontrado: " + request.getEquipamentoId()));

                    manutencao.setEquipamento(equipamento);
                    mapper.updateEntity(manutencao, request);

                    ManutencaoEquipamento savedManutencao = manutencaoRepository.save(manutencao);
                    log.info("Manutenção atualizada com sucesso. ID: {}", savedManutencao.getId());

                    return mapper.toResponse(savedManutencao);
                });
    }

    @Override
    public void delete(Long id) {
        log.info("Excluindo manutenção ID: {}", id);

        if (!manutencaoRepository.existsById(id)) {
            throw new RuntimeException("Manutenção não encontrada: " + id);
        }

        manutencaoRepository.deleteById(id);
        log.info("Manutenção excluída com sucesso. ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEquipamento(Long equipamentoId) {
        EquipamentoPneumatico equipamento = equipamentoRepository.findById(equipamentoId)
                .orElse(null);
        if (equipamento == null) {
            return false;
        }
        return !manutencaoRepository.findByEquipamentoOrderByCreatedAtDesc(equipamento).isEmpty();
    }

    // Método auxiliar para conversão de List para Page
    private Page<ManutencaoEquipamentoResponse> convertListToPage(List<ManutencaoEquipamento> manutencoes,
            Pageable pageable) {
        List<ManutencaoEquipamentoResponse> responses = manutencoes.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responses.size());
        List<ManutencaoEquipamentoResponse> pageContent = responses.subList(start, end);

        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, responses.size());
    }
}

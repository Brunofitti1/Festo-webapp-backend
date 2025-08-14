package br.com.fiap.festo.festo_webapp_backend.service.impl;

import br.com.fiap.festo.festo_webapp_backend.dto.request.LeituraSensorRequest;
import br.com.fiap.festo.festo_webapp_backend.dto.response.LeituraSensorResponse;
import br.com.fiap.festo.festo_webapp_backend.entity.LeituraSensor;
import br.com.fiap.festo.festo_webapp_backend.entity.EquipamentoPneumatico;
import br.com.fiap.festo.festo_webapp_backend.repository.LeituraSensorRepository;
import br.com.fiap.festo.festo_webapp_backend.repository.EquipamentoPneumaticoRepository;
import br.com.fiap.festo.festo_webapp_backend.service.LeituraSensorService;
import br.com.fiap.festo.festo_webapp_backend.mapper.LeituraSensorMapper;
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
 * Implementação do serviço para gerenciamento de leituras de sensores
 *
 * @author Seu Nome
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LeituraSensorServiceImpl implements LeituraSensorService {

    private final LeituraSensorRepository leituraSensorRepository;
    private final EquipamentoPneumaticoRepository equipamentoRepository;
    private final LeituraSensorMapper mapper;

    @Override
    public LeituraSensorResponse save(LeituraSensorRequest request) {
        log.info("Salvando nova leitura de sensor para equipamento ID: {}", request.getEquipamentoId());

        EquipamentoPneumatico equipamento = equipamentoRepository.findById(request.getEquipamentoId())
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado: " + request.getEquipamentoId()));

        LeituraSensor leitura = mapper.toEntity(request);
        leitura.setEquipamento(equipamento);

        LeituraSensor savedLeitura = leituraSensorRepository.save(leitura);
        log.info("Leitura salva com sucesso. ID: {}", savedLeitura.getId());

        return mapper.toResponse(savedLeitura);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeituraSensorResponse> findById(Long id) {
        log.debug("Buscando leitura por ID: {}", id);
        return leituraSensorRepository.findById(id)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeituraSensorResponse> findAll(Pageable pageable) {
        log.debug("Listando todas as leituras com paginação: {}", pageable);
        Page<LeituraSensor> leituras = leituraSensorRepository.findAll(pageable);
        return leituras.map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeituraSensorResponse> findByEquipamento(Long equipamentoId, Pageable pageable) {
        log.debug("Buscando leituras do equipamento ID: {}", equipamentoId);
        Page<LeituraSensor> leituras = leituraSensorRepository
                .findByEquipamento_IdOrderByTimestampLeituraDesc(equipamentoId, pageable);
        return leituras.map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeituraSensorResponse> findByTipoSensor(String tipoSensor, Pageable pageable) {
        log.debug("Buscando leituras do tipo sensor: {}", tipoSensor);
        Page<LeituraSensor> leituras = leituraSensorRepository.findByTipoSensorOrderByTimestampLeituraDesc(tipoSensor,
                pageable);
        return leituras.map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeituraSensorResponse> findByPeriodo(LocalDateTime inicio, LocalDateTime fim, Pageable pageable) {
        log.debug("Buscando leituras no período de {} a {}", inicio, fim);
        Page<LeituraSensor> leituras = leituraSensorRepository
                .findByTimestampLeituraBetweenOrderByTimestampLeituraDesc(inicio, fim, pageable);
        return leituras.map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeituraSensorResponse> findLeiturasCriticas(Long equipamentoId) {
        log.debug("Buscando leituras críticas para equipamento ID: {}", equipamentoId);
        List<LeituraSensor> leituras;

        if (equipamentoId != null) {
            leituras = leituraSensorRepository.findLeiturasCriticasPorEquipamento(equipamentoId);
        } else {
            leituras = leituraSensorRepository.findLeiturasCriticas();
        }

        return leituras.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeituraSensorResponse> findUltimasLeiturasPorTipo(Long equipamentoId) {
        log.debug("Buscando últimas leituras por tipo para equipamento ID: {}", equipamentoId);
        List<LeituraSensor> leituras = leituraSensorRepository.findUltimasLeiturasPorTipo(equipamentoId);
        return leituras.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularMediaPorPeriodo(Long equipamentoId, String tipoSensor, LocalDateTime inicio,
            LocalDateTime fim) {
        log.debug("Calculando média para equipamento {}, sensor {}, período {} a {}", equipamentoId, tipoSensor, inicio,
                fim);
        return leituraSensorRepository.calcularMediaPorPeriodo(equipamentoId, tipoSensor, inicio, fim);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeituraSensorResponse> findLeiturasCimaLimite(String tipoSensor, Double valorLimite) {
        log.debug("Buscando leituras acima do limite {} para sensor {}", valorLimite, tipoSensor);
        List<LeituraSensor> leituras = leituraSensorRepository
                .findByTipoSensorAndValorGreaterThanOrderByTimestampLeituraDesc(tipoSensor, valorLimite);
        return leituras.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeituraSensorResponse> findLeiturasAbaixoLimite(String tipoSensor, Double valorLimite) {
        log.debug("Buscando leituras abaixo do limite {} para sensor {}", valorLimite, tipoSensor);
        List<LeituraSensor> leituras = leituraSensorRepository
                .findByTipoSensorAndValorLessThanOrderByTimestampLeituraDesc(tipoSensor, valorLimite);
        return leituras.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public int deleteLeituraAntigas(LocalDateTime dataLimite) {
        log.info("Excluindo leituras anteriores a {}", dataLimite);
        int deleted = leituraSensorRepository.deleteByTimestampLeituraBefore(dataLimite);
        log.info("Excluídas {} leituras antigas", deleted);
        return deleted;
    }

    @Override
    @Transactional(readOnly = true)
    public Object getEstatisticasLeituras(Long equipamentoId, LocalDateTime inicio, LocalDateTime fim) {
        log.debug("Gerando estatísticas para equipamento {}, período {} a {}", equipamentoId, inicio, fim);

        List<Object[]> estatisticas = leituraSensorRepository.getEstatisticasLeituras(equipamentoId, inicio, fim);

        Map<String, Object> resultado = new HashMap<>();
        for (Object[] linha : estatisticas) {
            String tipoSensor = (String) linha[0];
            Long totalLeituras = (Long) linha[1];
            Double valorMinimo = (Double) linha[2];
            Double valorMaximo = (Double) linha[3];
            Double valorMedio = (Double) linha[4];

            Map<String, Object> estatisticaTipo = new HashMap<>();
            estatisticaTipo.put("totalLeituras", totalLeituras);
            estatisticaTipo.put("valorMinimo", valorMinimo);
            estatisticaTipo.put("valorMaximo", valorMaximo);
            estatisticaTipo.put("valorMedio", valorMedio);

            resultado.put(tipoSensor, estatisticaTipo);
        }

        return resultado;
    }

    @Override
    public Optional<LeituraSensorResponse> update(Long id, LeituraSensorRequest request) {
        log.info("Atualizando leitura ID: {}", id);

        return leituraSensorRepository.findById(id)
                .map(leitura -> {
                    EquipamentoPneumatico equipamento = equipamentoRepository.findById(request.getEquipamentoId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Equipamento não encontrado: " + request.getEquipamentoId()));

                    leitura.setEquipamento(equipamento);
                    leitura.setTipoSensor(request.getTipoSensor());
                    leitura.setValor(request.getValor());
                    leitura.setUnidade(request.getUnidade());
                    leitura.setValorMinimo(request.getValorMinimo());
                    leitura.setValorMaximo(request.getValorMaximo());
                    leitura.setObservacoes(request.getObservacoes());
                    leitura.setTimestampLeitura(LocalDateTime.now());

                    LeituraSensor savedLeitura = leituraSensorRepository.save(leitura);
                    log.info("Leitura atualizada com sucesso. ID: {}", savedLeitura.getId());

                    return mapper.toResponse(savedLeitura);
                });
    }

    @Override
    public void delete(Long id) {
        log.info("Excluindo leitura ID: {}", id);

        if (!leituraSensorRepository.existsById(id)) {
            throw new RuntimeException("Leitura não encontrada: " + id);
        }

        leituraSensorRepository.deleteById(id);
        log.info("Leitura excluída com sucesso. ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEquipamento(Long equipamentoId) {
        return leituraSensorRepository.existsByEquipamento_Id(equipamentoId);
    }
}

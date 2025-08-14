package br.com.fiap.festo.festo_webapp_backend.service.impl;

import br.com.fiap.festo.festo_webapp_backend.dto.request.EquipamentoPneumaticoRequest;
import br.com.fiap.festo.festo_webapp_backend.dto.response.EquipamentoPneumaticoResponse;
import br.com.fiap.festo.festo_webapp_backend.entity.EquipamentoPneumatico;
import br.com.fiap.festo.festo_webapp_backend.entity.EquipamentoPneumatico.StatusEquipamento;
import br.com.fiap.festo.festo_webapp_backend.repository.EquipamentoPneumaticoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de equipamentos pneumáticos
 * 
 * @author Seu Nome
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EquipamentoPneumaticoServiceImpl {

    private final EquipamentoPneumaticoRepository equipamentoRepository;
    // TODO: Adicionar MapStruct mapper quando criado

    /**
     * Buscar todos os equipamentos ativos
     */
    @Transactional(readOnly = true)
    public List<EquipamentoPneumaticoResponse> buscarEquipamentosAtivos() {
        log.info("Buscando todos os equipamentos ativos");

        List<EquipamentoPneumatico> equipamentos = equipamentoRepository.findByAtivoTrue();

        return equipamentos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Buscar equipamento por ID
     */
    @Transactional(readOnly = true)
    public Optional<EquipamentoPneumaticoResponse> buscarPorId(Long id) {
        log.info("Buscando equipamento por ID: {}", id);

        return equipamentoRepository.findById(id)
                .filter(EquipamentoPneumatico::getAtivo)
                .map(this::convertToResponse);
    }

    /**
     * Criar novo equipamento
     */
    public EquipamentoPneumaticoResponse criarEquipamento(EquipamentoPneumaticoRequest request) {
        log.info("Criando novo equipamento: {}", request.getNome());

        EquipamentoPneumatico equipamento = convertFromRequest(request);
        equipamento.setAtivo(true);

        // Calcular próxima manutenção se não informada
        if (equipamento.getProximaManutencao() == null && equipamento.getIntervaloManutencaoHoras() != null) {
            LocalDate proximaManutencao = LocalDate.now()
                    .plusDays(equipamento.getIntervaloManutencaoHoras() / 24); // Converter horas para dias
            equipamento.setProximaManutencao(proximaManutencao);
        }

        EquipamentoPneumatico equipamentoSalvo = equipamentoRepository.save(equipamento);

        log.info("Equipamento criado com sucesso. ID: {}", equipamentoSalvo.getId());
        return convertToResponse(equipamentoSalvo);
    }

    /**
     * Atualizar equipamento
     */
    public Optional<EquipamentoPneumaticoResponse> atualizarEquipamento(Long id, EquipamentoPneumaticoRequest request) {
        log.info("Atualizando equipamento ID: {}", id);

        return equipamentoRepository.findById(id)
                .filter(EquipamentoPneumatico::getAtivo)
                .map(equipamento -> {
                    updateEquipamentoFromRequest(equipamento, request);
                    EquipamentoPneumatico equipamentoAtualizado = equipamentoRepository.save(equipamento);
                    log.info("Equipamento atualizado com sucesso. ID: {}", id);
                    return convertToResponse(equipamentoAtualizado);
                });
    }

    /**
     * Desativar equipamento (soft delete)
     */
    public boolean desativarEquipamento(Long id) {
        log.info("Desativando equipamento ID: {}", id);

        return equipamentoRepository.findById(id)
                .filter(EquipamentoPneumatico::getAtivo)
                .map(equipamento -> {
                    equipamento.setAtivo(false);
                    equipamento.setStatus(StatusEquipamento.DESATIVADO);
                    equipamentoRepository.save(equipamento);
                    log.info("Equipamento desativado com sucesso. ID: {}", id);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Buscar equipamentos com manutenção vencida
     */
    @Transactional(readOnly = true)
    public List<EquipamentoPneumaticoResponse> buscarEquipamentosComManutencaoVencida() {
        log.info("Buscando equipamentos com manutenção vencida");

        LocalDate agora = LocalDate.now();
        List<EquipamentoPneumatico> equipamentos = equipamentoRepository
                .findEquipamentosComManutencaoVencida(agora);

        return equipamentos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // TODO: Implementar métodos para:
    // - buscarEquipamentosPorStatus
    // - buscarEquipamentosPorTipo
    // - gerarRelatorioManutencao
    // - calcularIndicadoresDesempenho

    /**
     * Converter entidade para DTO de resposta
     */
    private EquipamentoPneumaticoResponse convertToResponse(EquipamentoPneumatico equipamento) {
        // TODO: Usar MapStruct quando configurado
        return EquipamentoPneumaticoResponse.builder()
                .id(equipamento.getId())
                .nome(equipamento.getNome())
                .tipo(equipamento.getTipo())
                .modelo(equipamento.getModelo())
                .fabricante(equipamento.getFabricante())
                .numeroSerie(equipamento.getNumeroSerie())
                .status(equipamento.getStatus().toString())
                .pressaoOperacao(equipamento.getPressaoOperacao())
                .temperaturaOperacao(equipamento.getTemperaturaOperacao())
                .ciclosRealizados(equipamento.getCiclosRealizados())
                .dataInstalacao(equipamento.getDataInstalacao())
                .dataUltimaManutencao(equipamento.getDataUltimaManutencao())
                .proximaManutencao(equipamento.getProximaManutencao())
                .intervaloManutencaoHoras(equipamento.getIntervaloManutencaoHoras())
                .observacoes(equipamento.getObservacoes())
                .ativo(equipamento.getAtivo())
                .createdAt(equipamento.getCreatedAt())
                .updatedAt(equipamento.getUpdatedAt())
                .diasParaProximaManutencao(calcularDiasParaProximaManutencao(equipamento))
                .statusSaude(calcularStatusSaude(equipamento))
                .scoreConfiabilidade(calcularScoreConfiabilidade(equipamento))
                .build();
    }

    /**
     * Converter DTO de request para entidade
     */
    private EquipamentoPneumatico convertFromRequest(EquipamentoPneumaticoRequest request) {
        // TODO: Usar MapStruct quando configurado
        return EquipamentoPneumatico.builder()
                .nome(request.getNome())
                .tipo(request.getTipo())
                .modelo(request.getModelo())
                .fabricante(request.getFabricante())
                .numeroSerie(request.getNumeroSerie())
                .status(StatusEquipamento.valueOf(request.getStatus()))
                .pressaoOperacao(request.getPressaoOperacao())
                .temperaturaOperacao(request.getTemperaturaOperacao())
                .ciclosRealizados(request.getCiclosRealizados())
                .dataInstalacao(request.getDataInstalacao())
                .dataUltimaManutencao(request.getDataUltimaManutencao())
                .proximaManutencao(request.getProximaManutencao())
                .intervaloManutencaoHoras(request.getIntervaloManutencaoHoras())
                .observacoes(request.getObservacoes())
                .ativo(request.getAtivo())
                .build();
    }

    /**
     * Atualizar entidade com dados do request
     */
    private void updateEquipamentoFromRequest(EquipamentoPneumatico equipamento, EquipamentoPneumaticoRequest request) {
        equipamento.setNome(request.getNome());
        equipamento.setTipo(request.getTipo());
        equipamento.setModelo(request.getModelo());
        equipamento.setFabricante(request.getFabricante());
        equipamento.setNumeroSerie(request.getNumeroSerie());
        equipamento.setStatus(StatusEquipamento.valueOf(request.getStatus()));
        equipamento.setPressaoOperacao(request.getPressaoOperacao());
        equipamento.setTemperaturaOperacao(request.getTemperaturaOperacao());
        equipamento.setCiclosRealizados(request.getCiclosRealizados());
        equipamento.setDataInstalacao(request.getDataInstalacao());
        equipamento.setDataUltimaManutencao(request.getDataUltimaManutencao());
        equipamento.setProximaManutencao(request.getProximaManutencao());
        equipamento.setIntervaloManutencaoHoras(request.getIntervaloManutencaoHoras());
        equipamento.setObservacoes(request.getObservacoes());
    }

    /**
     * Calcular dias para próxima manutenção
     */
    private Integer calcularDiasParaProximaManutencao(EquipamentoPneumatico equipamento) {
        if (equipamento.getProximaManutencao() == null) {
            return null;
        }

        LocalDate agora = LocalDate.now();
        return (int) ChronoUnit.DAYS.between(agora, equipamento.getProximaManutencao());
    }

    /**
     * Calcular status de saúde do equipamento
     */
    private String calcularStatusSaude(EquipamentoPneumatico equipamento) {
        // Lógica simplificada - pode ser expandida
        if (equipamento.getStatus() == StatusEquipamento.FALHA) {
            return "CRITICO";
        }

        Integer dias = calcularDiasParaProximaManutencao(equipamento);
        if (dias != null) {
            if (dias < 0)
                return "CRITICO";
            if (dias < 7)
                return "ATENCAO";
            if (dias < 30)
                return "BOM";
        }

        return "OTIMO";
    }

    /**
     * Calcular score de confiabilidade (0.0 a 1.0)
     */
    private Double calcularScoreConfiabilidade(EquipamentoPneumatico equipamento) {
        // Lógica simplificada baseada em status e dias para manutenção
        double score = 1.0;

        if (equipamento.getStatus() == StatusEquipamento.FALHA) {
            score = 0.0;
        } else if (equipamento.getStatus() == StatusEquipamento.MANUTENCAO) {
            score = 0.3;
        } else {
            Integer dias = calcularDiasParaProximaManutencao(equipamento);
            if (dias != null && dias < 0) {
                score = 0.1; // Manutenção vencida
            } else if (dias != null && dias < 7) {
                score = 0.6; // Manutenção muito próxima
            } else if (dias != null && dias < 30) {
                score = 0.8; // Manutenção próxima
            }
        }

        return score;
    }
}

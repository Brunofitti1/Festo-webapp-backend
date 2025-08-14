package br.com.fiap.festo.festo_webapp_backend.controller;

import br.com.fiap.festo.festo_webapp_backend.dto.request.LeituraSensorRequest;
import br.com.fiap.festo.festo_webapp_backend.dto.response.LeituraSensorResponse;
import br.com.fiap.festo.festo_webapp_backend.service.LeituraSensorService;
import br.com.fiap.festo.festo_webapp_backend.entity.LeituraSensor.NivelAlerta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controller REST para gerenciamento de leituras de sensores
 *
 * @author Seu Nome
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/leituras")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LeituraSensorController {

    private final LeituraSensorService leituraService;

    /**
     * Lista todas as leituras com paginação
     */
    @GetMapping
    public ResponseEntity<Page<LeituraSensorResponse>> listarLeituras(Pageable pageable) {
        log.info("Listando leituras com paginação: {}", pageable);
        try {
            Page<LeituraSensorResponse> leituras = leituraService.findAll(pageable);
            return ResponseEntity.ok(leituras);
        } catch (Exception e) {
            log.error("Erro ao listar leituras", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca leitura por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<LeituraSensorResponse> buscarPorId(@PathVariable Long id) {
        log.info("Buscando leitura por ID: {}", id);
        try {
            Optional<LeituraSensorResponse> leitura = leituraService.findById(id);
            return leitura.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Erro ao buscar leitura por ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cria nova leitura
     */
    @PostMapping
    public ResponseEntity<LeituraSensorResponse> criarLeitura(@RequestBody LeituraSensorRequest request) {
        log.info("Criando nova leitura: {}", request);
        try {
            LeituraSensorResponse leitura = leituraService.save(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(leitura);
        } catch (Exception e) {
            log.error("Erro ao criar leitura", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista leituras por equipamento
     */
    @GetMapping("/equipamento/{equipamentoId}")
    public ResponseEntity<Page<LeituraSensorResponse>> listarPorEquipamento(
            @PathVariable Long equipamentoId,
            Pageable pageable) {
        log.info("Listando leituras do equipamento ID: {}", equipamentoId);
        try {
            Page<LeituraSensorResponse> leituras = leituraService.findByEquipamento(equipamentoId, pageable);
            return ResponseEntity.ok(leituras);
        } catch (Exception e) {
            log.error("Erro ao listar leituras do equipamento: {}", equipamentoId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista leituras críticas (fora dos limites normais)
     */
    @GetMapping("/criticas")
    public ResponseEntity<List<LeituraSensorResponse>> listarLeiturasCriticas(
            @RequestParam(required = false) Long equipamentoId) {
        log.info("Listando leituras críticas para equipamento: {}", equipamentoId);
        try {
            List<LeituraSensorResponse> leituras = leituraService.findLeiturasCriticas(equipamentoId);
            return ResponseEntity.ok(leituras);
        } catch (Exception e) {
            log.error("Erro ao listar leituras críticas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista leituras por tipo de sensor
     */
    @GetMapping("/tipo/{tipoSensor}")
    public ResponseEntity<Page<LeituraSensorResponse>> listarPorTipoSensor(
            @PathVariable String tipoSensor,
            Pageable pageable) {
        log.info("Listando leituras do tipo: {}", tipoSensor);
        try {
            Page<LeituraSensorResponse> leituras = leituraService.findByTipoSensor(tipoSensor, pageable);
            return ResponseEntity.ok(leituras);
        } catch (Exception e) {
            log.error("Erro ao listar leituras por tipo: {}", tipoSensor, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista leituras por período
     */
    @GetMapping("/periodo")
    public ResponseEntity<Page<LeituraSensorResponse>> listarPorPeriodo(
            @RequestParam LocalDateTime dataInicio,
            @RequestParam LocalDateTime dataFim,
            Pageable pageable) {
        log.info("Listando leituras entre {} e {}", dataInicio, dataFim);
        try {
            Page<LeituraSensorResponse> leituras = leituraService.findByPeriodo(dataInicio, dataFim, pageable);
            return ResponseEntity.ok(leituras);
        } catch (Exception e) {
            log.error("Erro ao listar leituras por período", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exclui leitura por ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirLeitura(@PathVariable Long id) {
        log.info("Excluindo leitura ID: {}", id);
        try {
            leituraService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Erro ao excluir leitura: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }
}

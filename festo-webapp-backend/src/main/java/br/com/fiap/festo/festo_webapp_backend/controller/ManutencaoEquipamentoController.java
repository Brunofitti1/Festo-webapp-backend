package br.com.fiap.festo.festo_webapp_backend.controller;

import br.com.fiap.festo.festo_webapp_backend.dto.request.ManutencaoEquipamentoRequest;
import br.com.fiap.festo.festo_webapp_backend.dto.response.ManutencaoEquipamentoResponse;
import br.com.fiap.festo.festo_webapp_backend.service.ManutencaoEquipamentoService;
import br.com.fiap.festo.festo_webapp_backend.entity.ManutencaoEquipamento.StatusManutencao;
import br.com.fiap.festo.festo_webapp_backend.entity.ManutencaoEquipamento.TipoManutencao;
import br.com.fiap.festo.festo_webapp_backend.entity.ManutencaoEquipamento.Prioridade;
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
import java.util.Map;

/**
 * Controller REST para gerenciamento de manutenções de equipamentos
 *
 * @author Seu Nome
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/manutencoes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ManutencaoEquipamentoController {

    private final ManutencaoEquipamentoService manutencaoService;

    /**
     * Lista todas as manutenções com paginação
     */
    @GetMapping
    public ResponseEntity<Page<ManutencaoEquipamentoResponse>> listarManutencoes(Pageable pageable) {
        log.info("Listando manutenções com paginação: {}", pageable);
        try {
            Page<ManutencaoEquipamentoResponse> manutencoes = manutencaoService.findAll(pageable);
            return ResponseEntity.ok(manutencoes);
        } catch (Exception e) {
            log.error("Erro ao listar manutenções", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca manutenção por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ManutencaoEquipamentoResponse> buscarPorId(@PathVariable Long id) {
        log.info("Buscando manutenção por ID: {}", id);
        try {
            return manutencaoService.findById(id)
                    .map(manutencao -> ResponseEntity.ok(manutencao))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Erro ao buscar manutenção por ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cria nova manutenção
     */
    @PostMapping
    public ResponseEntity<ManutencaoEquipamentoResponse> criarManutencao(
            @RequestBody ManutencaoEquipamentoRequest request) {
        log.info("Criando nova manutenção para equipamento ID: {}", request.getEquipamentoId());
        try {
            ManutencaoEquipamentoResponse manutencaoCriada = manutencaoService.save(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(manutencaoCriada);
        } catch (Exception e) {
            log.error("Erro ao criar manutenção", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Atualiza manutenção existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ManutencaoEquipamentoResponse> atualizarManutencao(
            @PathVariable Long id,
            @RequestBody ManutencaoEquipamentoRequest request) {
        log.info("Atualizando manutenção ID: {}", id);
        try {
            return manutencaoService.update(id, request)
                    .map(manutencao -> ResponseEntity.ok(manutencao))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Erro ao atualizar manutenção ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca manutenções por equipamento
     */
    @GetMapping("/equipamento/{equipamentoId}")
    public ResponseEntity<Page<ManutencaoEquipamentoResponse>> buscarPorEquipamento(
            @PathVariable Long equipamentoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Buscando manutenções para equipamento ID: {}", equipamentoId);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ManutencaoEquipamentoResponse> manutencoes = manutencaoService.findByEquipamento(equipamentoId,
                    pageable);
            return ResponseEntity.ok(manutencoes);
        } catch (Exception e) {
            log.error("Erro ao buscar manutenções por equipamento ID: {}", equipamentoId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca manutenções vencidas
     */
    @GetMapping("/vencidas")
    public ResponseEntity<List<ManutencaoEquipamentoResponse>> buscarManutencoesVencidas() {
        log.info("Buscando manutenções vencidas");
        try {
            List<ManutencaoEquipamentoResponse> manutencoes = manutencaoService.findManutencoesVencidas();
            return ResponseEntity.ok(manutencoes);
        } catch (Exception e) {
            log.error("Erro ao buscar manutenções vencidas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Inicia manutenção
     */
    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<ManutencaoEquipamentoResponse> iniciarManutencao(
            @PathVariable Long id,
            @RequestParam String tecnico) {
        log.info("Iniciando manutenção ID: {} com técnico: {}", id, tecnico);
        try {
            return manutencaoService.iniciarManutencao(id, tecnico)
                    .map(manutencao -> ResponseEntity.ok(manutencao))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Erro ao iniciar manutenção ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Conclui manutenção
     */
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<ManutencaoEquipamentoResponse> concluirManutencao(
            @PathVariable Long id,
            @RequestParam(required = false) String observacoes,
            @RequestParam(required = false) Double custo) {
        log.info("Concluindo manutenção ID: {}", id);
        try {
            return manutencaoService.concluirManutencao(id, observacoes, custo)
                    .map(manutencao -> ResponseEntity.ok(manutencao))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Erro ao concluir manutenção ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exclui manutenção
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarManutencao(@PathVariable Long id) {
        log.info("Excluindo manutenção ID: {}", id);
        try {
            manutencaoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Erro ao excluir manutenção ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

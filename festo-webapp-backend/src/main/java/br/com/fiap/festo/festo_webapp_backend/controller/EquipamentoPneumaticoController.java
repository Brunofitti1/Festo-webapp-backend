package br.com.fiap.festo.festo_webapp_backend.controller;

import br.com.fiap.festo.festo_webapp_backend.dto.request.EquipamentoPneumaticoRequest;
import br.com.fiap.festo.festo_webapp_backend.dto.response.EquipamentoPneumaticoResponse;
import br.com.fiap.festo.festo_webapp_backend.service.impl.EquipamentoPneumaticoServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações de equipamentos pneumáticos
 * 
 * @author Seu Nome
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/equipamentos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // TODO: Configurar CORS adequadamente
public class EquipamentoPneumaticoController {

    private final EquipamentoPneumaticoServiceImpl equipamentoService;

    /**
     * GET /api/v1/equipamentos
     * Buscar todos os equipamentos ativos
     */
    @GetMapping
    public ResponseEntity<List<EquipamentoPneumaticoResponse>> buscarTodosEquipamentos() {
        log.info("GET /api/v1/equipamentos - Buscando todos os equipamentos");

        try {
            List<EquipamentoPneumaticoResponse> equipamentos = equipamentoService.buscarEquipamentosAtivos();
            return ResponseEntity.ok(equipamentos);
        } catch (Exception e) {
            log.error("Erro ao buscar equipamentos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/v1/equipamentos/{id}
     * Buscar equipamento por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoPneumaticoResponse> buscarEquipamentoPorId(@PathVariable Long id) {
        log.info("GET /api/v1/equipamentos/{} - Buscando equipamento por ID", id);

        try {
            return equipamentoService.buscarPorId(id)
                    .map(equipamento -> ResponseEntity.ok(equipamento))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Erro ao buscar equipamento por ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /api/v1/equipamentos
     * Criar novo equipamento
     */
    @PostMapping
    public ResponseEntity<EquipamentoPneumaticoResponse> criarEquipamento(
            @RequestBody EquipamentoPneumaticoRequest request) {
        log.info("POST /api/v1/equipamentos - Criando novo equipamento: {}", request.getNome());

        try {
            // TODO: Adicionar validação dos dados de entrada
            EquipamentoPneumaticoResponse equipamentoCriado = equipamentoService.criarEquipamento(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(equipamentoCriado);
        } catch (Exception e) {
            log.error("Erro ao criar equipamento: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PUT /api/v1/equipamentos/{id}
     * Atualizar equipamento existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<EquipamentoPneumaticoResponse> atualizarEquipamento(
            @PathVariable Long id,
            @RequestBody EquipamentoPneumaticoRequest request) {
        log.info("PUT /api/v1/equipamentos/{} - Atualizando equipamento", id);

        try {
            return equipamentoService.atualizarEquipamento(id, request)
                    .map(equipamento -> ResponseEntity.ok(equipamento))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Erro ao atualizar equipamento ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE /api/v1/equipamentos/{id}
     * Desativar equipamento (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarEquipamento(@PathVariable Long id) {
        log.info("DELETE /api/v1/equipamentos/{} - Desativando equipamento", id);

        try {
            boolean desativado = equipamentoService.desativarEquipamento(id);
            return desativado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro ao desativar equipamento ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/v1/equipamentos/manutencao/vencida
     * Buscar equipamentos com manutenção vencida
     */
    @GetMapping("/manutencao/vencida")
    public ResponseEntity<List<EquipamentoPneumaticoResponse>> buscarEquipamentosComManutencaoVencida() {
        log.info("GET /api/v1/equipamentos/manutencao/vencida - Buscando equipamentos com manutenção vencida");

        try {
            List<EquipamentoPneumaticoResponse> equipamentos = equipamentoService
                    .buscarEquipamentosComManutencaoVencida();
            return ResponseEntity.ok(equipamentos);
        } catch (Exception e) {
            log.error("Erro ao buscar equipamentos com manutenção vencida: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // TODO: Implementar endpoints adicionais:
    // - GET /api/v1/equipamentos/status/{status} - Buscar por status
    // - GET /api/v1/equipamentos/tipo/{tipo} - Buscar por tipo
    // - GET /api/v1/equipamentos/dashboard/estatisticas - Dashboard com
    // estatísticas
    // - GET /api/v1/equipamentos/relatorios/manutencao - Relatório de manutenção
    // - POST /api/v1/equipamentos/{id}/manutencao - Registrar manutenção
}

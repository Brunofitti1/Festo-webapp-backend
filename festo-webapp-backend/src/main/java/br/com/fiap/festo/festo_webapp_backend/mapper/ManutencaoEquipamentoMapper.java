package br.com.fiap.festo.festo_webapp_backend.mapper;

import br.com.fiap.festo.festo_webapp_backend.dto.request.ManutencaoEquipamentoRequest;
import br.com.fiap.festo.festo_webapp_backend.dto.response.ManutencaoEquipamentoResponse;
import br.com.fiap.festo.festo_webapp_backend.entity.ManutencaoEquipamento;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper para conversões entre Entity, Request e Response de
 * ManutencaoEquipamento
 *
 * @author Seu Nome
 * @version 1.0
 */
@Component
public class ManutencaoEquipamentoMapper {

    /**
     * Converte Request para Entity
     *
     * @param request dados da request
     * @return entity convertida
     */
    public ManutencaoEquipamento toEntity(ManutencaoEquipamentoRequest request) {
        if (request == null) {
            return null;
        }

        ManutencaoEquipamento manutencao = new ManutencaoEquipamento();
        manutencao.setTipoManutencao(ManutencaoEquipamento.TipoManutencao.valueOf(request.getTipoManutencao()));
        manutencao.setDescricao(request.getDescricao());
        manutencao.setDataProgramada(request.getDataProgramada());
        manutencao.setPrioridade(ManutencaoEquipamento.Prioridade.valueOf(request.getPrioridade()));
        manutencao.setTecnicoResponsavel(request.getTecnicoResponsavel());
        manutencao.setObservacoesTecnicas(request.getObservacoesTecnicas());
        manutencao.setPecasSubstituidas(request.getPecasSubstituidas());
        manutencao.setTempoParadaMinutos(request.getTempoParadaMinutos());
        manutencao.setCustoManutencao(request.getCustoManutencao());
        manutencao.setStatus(ManutencaoEquipamento.StatusManutencao.PROGRAMADA);
        manutencao.setCreatedAt(LocalDateTime.now());
        // equipamento será setado no service

        return manutencao;
    }

    /**
     * Converte Entity para Response
     *
     * @param manutencao entity
     * @return response convertida
     */
    public ManutencaoEquipamentoResponse toResponse(ManutencaoEquipamento manutencao) {
        if (manutencao == null) {
            return null;
        }

        ManutencaoEquipamentoResponse response = new ManutencaoEquipamentoResponse();
        response.setId(manutencao.getId());
        response.setEquipamentoId(manutencao.getEquipamento() != null ? manutencao.getEquipamento().getId() : null);
        response.setNomeEquipamento(manutencao.getEquipamento() != null ? manutencao.getEquipamento().getNome() : null);
        response.setTipoManutencao(manutencao.getTipoManutencao().name());
        response.setDescricao(manutencao.getDescricao());
        response.setStatus(manutencao.getStatus().name());
        response.setDataProgramada(manutencao.getDataProgramada());
        response.setDataInicio(manutencao.getDataInicio());
        response.setDataConclusao(manutencao.getDataConclusao());
        response.setPrioridade(manutencao.getPrioridade().name());
        response.setTecnicoResponsavel(manutencao.getTecnicoResponsavel());
        response.setObservacoesTecnicas(manutencao.getObservacoesTecnicas());
        response.setPecasSubstituidas(manutencao.getPecasSubstituidas());
        response.setCustoManutencao(manutencao.getCustoManutencao());
        response.setTempoParadaMinutos(manutencao.getTempoParadaMinutos());
        response.setCreatedAt(manutencao.getCreatedAt());
        response.setUpdatedAt(manutencao.getUpdatedAt());

        // Definir cor do status
        switch (manutencao.getStatus()) {
            case PROGRAMADA -> response.setStatusCor("BLUE");
            case EM_ANDAMENTO -> response.setStatusCor("ORANGE");
            case CONCLUIDA -> response.setStatusCor("GREEN");
            case CANCELADA -> response.setStatusCor("RED");
            case AGUARDANDO_PECAS -> response.setStatusCor("YELLOW");
        }

        // Verificar se está atrasada
        if (manutencao.getStatus() == ManutencaoEquipamento.StatusManutencao.PROGRAMADA &&
                manutencao.getDataProgramada() != null &&
                manutencao.getDataProgramada().isBefore(LocalDateTime.now())) {
            response.setAtrasada(true);
        } else {
            response.setAtrasada(false);
        }

        // Calcular duração se houver data de início e conclusão
        if (manutencao.getDataInicio() != null && manutencao.getDataConclusao() != null) {
            long duracao = java.time.Duration.between(manutencao.getDataInicio(), manutencao.getDataConclusao())
                    .toMinutes();
            response.setDuracaoMinutos(duracao);
        }

        return response;
    }

    /**
     * Atualiza uma entity existente com dados da request
     *
     * @param manutencao entity existente
     * @param request    novos dados
     */
    public void updateEntity(ManutencaoEquipamento manutencao, ManutencaoEquipamentoRequest request) {
        if (manutencao == null || request == null) {
            return;
        }

        manutencao.setTipoManutencao(ManutencaoEquipamento.TipoManutencao.valueOf(request.getTipoManutencao()));
        manutencao.setDescricao(request.getDescricao());
        manutencao.setDataProgramada(request.getDataProgramada());
        manutencao.setPrioridade(ManutencaoEquipamento.Prioridade.valueOf(request.getPrioridade()));
        manutencao.setTecnicoResponsavel(request.getTecnicoResponsavel());
        manutencao.setObservacoesTecnicas(request.getObservacoesTecnicas());
        manutencao.setPecasSubstituidas(request.getPecasSubstituidas());
        manutencao.setTempoParadaMinutos(request.getTempoParadaMinutos());
        manutencao.setCustoManutencao(request.getCustoManutencao());
        manutencao.setUpdatedAt(LocalDateTime.now());
        // equipamento será atualizado no service se necessário
        // status não deve ser alterado aqui, apenas através de métodos específicos
    }
}

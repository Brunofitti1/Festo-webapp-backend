package br.com.fiap.festo.festo_webapp_backend.mapper;

import br.com.fiap.festo.festo_webapp_backend.dto.request.LeituraSensorRequest;
import br.com.fiap.festo.festo_webapp_backend.dto.response.LeituraSensorResponse;
import br.com.fiap.festo.festo_webapp_backend.entity.LeituraSensor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper para conversões entre Entity, Request e Response de LeituraSensor
 *
 * @author Seu Nome
 * @version 1.0
 */
@Component
public class LeituraSensorMapper {

    /**
     * Converte Request para Entity
     *
     * @param request dados da request
     * @return entity convertida
     */
    public LeituraSensor toEntity(LeituraSensorRequest request) {
        if (request == null) {
            return null;
        }

        LeituraSensor leitura = new LeituraSensor();
        leitura.setTipoSensor(request.getTipoSensor());
        leitura.setValor(request.getValor());
        leitura.setUnidade(request.getUnidade());
        leitura.setValorMinimo(request.getValorMinimo());
        leitura.setValorMaximo(request.getValorMaximo());
        leitura.setObservacoes(request.getObservacoes());
        leitura.setTimestampLeitura(LocalDateTime.now());
        // equipamento será setado no service

        return leitura;
    }

    /**
     * Converte Entity para Response
     *
     * @param leitura entity
     * @return response convertida
     */
    public LeituraSensorResponse toResponse(LeituraSensor leitura) {
        if (leitura == null) {
            return null;
        }

        LeituraSensorResponse response = new LeituraSensorResponse();
        response.setId(leitura.getId());
        response.setEquipamentoId(leitura.getEquipamento() != null ? leitura.getEquipamento().getId() : null);
        response.setNomeEquipamento(leitura.getEquipamento() != null ? leitura.getEquipamento().getNome() : null);
        response.setTipoSensor(leitura.getTipoSensor());
        response.setValor(leitura.getValor());
        response.setUnidade(leitura.getUnidade());
        response.setValorMinimo(leitura.getValorMinimo());
        response.setValorMaximo(leitura.getValorMaximo());
        response.setAlertaAtivo(leitura.getAlertaAtivo());
        response.setNivelAlerta(leitura.getNivelAlerta() != null ? leitura.getNivelAlerta().name() : null);
        response.setTimestampLeitura(leitura.getTimestampLeitura());
        response.setObservacoes(leitura.getObservacoes());

        // Definir cor do status baseado no alerta
        if (leitura.getAlertaAtivo() != null && leitura.getAlertaAtivo()) {
            if (leitura.getNivelAlerta() != null) {
                switch (leitura.getNivelAlerta()) {
                    case CRITICO -> response.setStatusCor("RED");
                    case ALTO -> response.setStatusCor("ORANGE");
                    case MEDIO -> response.setStatusCor("YELLOW");
                    default -> response.setStatusCor("GREEN");
                }
            } else {
                response.setStatusCor("RED");
            }
        } else {
            response.setStatusCor("GREEN");
        }

        return response;
    }

    /**
     * Atualiza uma entity existente com dados da request
     *
     * @param leitura entity existente
     * @param request novos dados
     */
    public void updateEntity(LeituraSensor leitura, LeituraSensorRequest request) {
        if (leitura == null || request == null) {
            return;
        }

        leitura.setTipoSensor(request.getTipoSensor());
        leitura.setValor(request.getValor());
        leitura.setUnidade(request.getUnidade());
        leitura.setValorMinimo(request.getValorMinimo());
        leitura.setValorMaximo(request.getValorMaximo());
        leitura.setObservacoes(request.getObservacoes());
        leitura.setTimestampLeitura(LocalDateTime.now());
        // equipamento será atualizado no service se necessário
    }
}

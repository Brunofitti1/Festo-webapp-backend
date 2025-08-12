package br.com.fiap.festo.festo_webapp_backend.config;

import br.com.fiap.festo.festo_webapp_backend.entity.EquipamentoPneumatico;
import br.com.fiap.festo.festo_webapp_backend.entity.EquipamentoPneumatico.StatusEquipamento;
import br.com.fiap.festo.festo_webapp_backend.repository.EquipamentoPneumaticoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Carregador de dados de exemplo para demonstração
 * 
 * @author Seu Nome
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final EquipamentoPneumaticoRepository equipamentoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (equipamentoRepository.count() == 0) {
            log.info("Carregando dados de exemplo...");
            criarEquipamentosExemplo();
            log.info("Dados de exemplo carregados com sucesso!");
        }
    }

    private void criarEquipamentosExemplo() {
        LocalDateTime agora = LocalDateTime.now();

        // Equipamento 1 - Cilindro Pneumático
        EquipamentoPneumatico cilindro1 = EquipamentoPneumatico.builder()
                .nome("Cilindro Pneumático CP-200")
                .tipo("CILINDRO")
                .modelo("DSBC-63-200-PPVA-N3")
                .fabricante("Festo")
                .numeroSerie("CP200-001")
                .status(StatusEquipamento.OPERANDO)
                .pressaoOperacao(6.0)
                .temperaturaOperacao(23.5)
                .ciclosRealizados(125000L)
                .dataInstalacao(agora.minusMonths(6))
                .dataUltimaManutencao(agora.minusDays(15))
                .proximaManutencao(agora.plusDays(15))
                .intervaloManutencaoHoras(720) // 30 dias
                .observacoes("Funcionamento normal. Última inspeção OK.")
                .ativo(true)
                .build();

        // Equipamento 2 - Válvula Direcional
        EquipamentoPneumatico valvula1 = EquipamentoPneumatico.builder()
                .nome("Válvula Direcional VD-01")
                .tipo("VALVULA")
                .modelo("CPE18-M1H-5J-1/4")
                .fabricante("Festo")
                .numeroSerie("VD001-002")
                .status(StatusEquipamento.OPERANDO)
                .pressaoOperacao(8.0)
                .temperaturaOperacao(28.2)
                .ciclosRealizados(89000L)
                .dataInstalacao(agora.minusMonths(4))
                .dataUltimaManutencao(agora.minusDays(45))
                .proximaManutencao(agora.minusDays(5)) // Manutenção vencida
                .intervaloManutencaoHoras(1440) // 60 dias
                .observacoes("Requer atenção - manutenção vencida")
                .ativo(true)
                .build();

        // Equipamento 3 - Compressor
        EquipamentoPneumatico compressor1 = EquipamentoPneumatico.builder()
                .nome("Compressor Industrial CI-500")
                .tipo("COMPRESSOR")
                .modelo("MSE6-E2M")
                .fabricante("Festo")
                .numeroSerie("CI500-003")
                .status(StatusEquipamento.OPERANDO)
                .pressaoOperacao(10.0)
                .temperaturaOperacao(45.8)
                .ciclosRealizados(58000L)
                .dataInstalacao(agora.minusMonths(8))
                .dataUltimaManutencao(agora.minusDays(7))
                .proximaManutencao(agora.plusDays(53))
                .intervaloManutencaoHoras(1440) // 60 dias
                .observacoes("Performance excelente")
                .ativo(true)
                .build();

        // Equipamento 4 - Sensor de Pressão (com falha)
        EquipamentoPneumatico sensor1 = EquipamentoPneumatico.builder()
                .nome("Sensor de Pressão SP-100")
                .tipo("SENSOR")
                .modelo("SPTW-P25R-G14-A-M12")
                .fabricante("Festo")
                .numeroSerie("SP100-004")
                .status(StatusEquipamento.FALHA)
                .pressaoOperacao(0.0) // Falha no sensor
                .temperaturaOperacao(22.1)
                .ciclosRealizados(0L)
                .dataInstalacao(agora.minusMonths(2))
                .dataUltimaManutencao(agora.minusDays(3))
                .proximaManutencao(agora.plusDays(27))
                .intervaloManutencaoHoras(720) // 30 dias
                .observacoes("FALHA CRÍTICA - Sensor não responde")
                .ativo(true)
                .build();

        // Equipamento 5 - Cilindro em Manutenção
        EquipamentoPneumatico cilindro2 = EquipamentoPneumatico.builder()
                .nome("Cilindro Rotativo CR-150")
                .tipo("CILINDRO")
                .modelo("DRQ-40-180-PPVA-A")
                .fabricante("Festo")
                .numeroSerie("CR150-005")
                .status(StatusEquipamento.MANUTENCAO)
                .pressaoOperacao(0.0) // Parado para manutenção
                .temperaturaOperacao(20.0)
                .ciclosRealizados(95000L)
                .dataInstalacao(agora.minusMonths(5))
                .dataUltimaManutencao(agora) // Em manutenção agora
                .proximaManutencao(agora.plusDays(30))
                .intervaloManutencaoHoras(720) // 30 dias
                .observacoes("Manutenção preventiva em andamento")
                .ativo(true)
                .build();

        // Salvar equipamentos
        List<EquipamentoPneumatico> equipamentos = Arrays.asList(
                cilindro1, valvula1, compressor1, sensor1, cilindro2);

        List<EquipamentoPneumatico> equipamentosSalvos = equipamentoRepository.saveAll(equipamentos);

        log.info("Criados {} equipamentos de exemplo", equipamentosSalvos.size());
    }
}

package com.example.pedidoservice;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);
    private final Counter pedidosCriadosCounter;
    private final Counter pedidosFalhaCounter;
    private final DistributionSummary valorMedioPedidosSummary;
    private final Random random = new Random();

    public PedidoService(MeterRegistry registry) {
        // Quantidade de pedidos criados (Counter)
        this.pedidosCriadosCounter = Counter.builder("pedidos.criados.total")
                .description("Total de pedidos criados")
                .tag("status", "sucesso")
                .register(registry);

        // Taxa de falha no processamento (Counter com tag)
        this.pedidosFalhaCounter = Counter.builder("pedidos.processamento.falha")
                .description("Total de pedidos que falharam no processamento")
                .tag("status", "falha")
                .register(registry);

        // Valor médio dos pedidos (DistributionSummary)
        this.valorMedioPedidosSummary = DistributionSummary.builder("pedidos.valor.medio")
                .description("Distribuição do valor dos pedidos")
                .baseUnit("BRL")
                .register(registry);
    }

    public void processarPedido(BigDecimal valor) throws Exception {
        // Simulação de falha de 10%
        if (random.nextDouble() < 0.1) {
            pedidosFalhaCounter.increment();
            log.error("Falha ao processar pedido de valor: {}", valor);
            throw new Exception("Erro simulado no processamento do pedido.");
        }

        // Simulação de sucesso
        pedidosCriadosCounter.increment();
        valorMedioPedidosSummary.record(valor.doubleValue());
        log.info("Pedido processado com sucesso. Valor: {}", valor);
    }
}

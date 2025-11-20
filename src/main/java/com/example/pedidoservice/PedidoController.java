package com.example.pedidoservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private static final Logger log = LoggerFactory.getLogger(PedidoController.class);
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<String> criarPedido(@RequestParam BigDecimal valor) {
        String pedidoId = UUID.randomUUID().toString();
        log.info("Recebendo novo pedido. ID: {}, Valor: {}", pedidoId, valor);

        try {
            // Simulação de um log contextualizado
            log.info("Log contextualizado - pedidoId: {}, usuario: user-123, status: processando", pedidoId);
            pedidoService.processarPedido(valor);
            log.info("Log contextualizado - pedidoId: {}, usuario: user-123, status: finalizado", pedidoId);
            return ResponseEntity.ok("Pedido " + pedidoId + " criado com sucesso.");
        } catch (Exception e) {
            log.error("Log contextualizado - pedidoId: {}, usuario: user-123, status: falha", pedidoId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

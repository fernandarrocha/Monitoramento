# 📦 Pedido Service Monitoring

Este projeto é uma aplicação de demonstração em **Spring Boot** configurada para monitoramento completo utilizando a stack **Prometheus** e **Grafana**. O objetivo principal é ilustrar a instrumentação de uma aplicação Java com **Micrometer** e a visualização de métricas e alertas em um ambiente de contêineres.

## 🚀 Tecnologias Utilizadas

| Componente | Tecnologia | Descrição |
| :--- | :--- | :--- |
| **Serviço** | Spring Boot 3.2.0 (Java 17) | Aplicação de microsserviço de pedidos. |
| **Métricas** | Spring Boot Actuator + Micrometer | Exposição de métricas no formato Prometheus. |
| **Coleta** | Prometheus | Servidor de monitoramento e coleta de séries temporais. |
| **Visualização** | Grafana | Plataforma de análise e visualização de métricas. |
| **Orquestração** | Docker e Docker Compose | Gerenciamento e execução dos contêineres. |

## 🛠️ Pré-requisitos

Para executar este projeto, você precisará ter instalado em sua máquina:

*   **Docker**
*   **Docker Compose**

## ⚙️ Configuração e Execução

O projeto é totalmente configurado para ser executado com um único comando via Docker Compose.

### 1. Build e Inicialização

Navegue até o diretório raiz do projeto e execute o comando:

```bash
docker-compose up --build -d
```

Este comando irá:
1.  Construir a imagem Docker da aplicação `pedido-service` (serviço `app`).
2.  Baixar e iniciar os contêineres do `prometheus` e `grafana`.
3.  Conectar todos os serviços na rede `monitor-net`.

### 2. Acessando os Serviços

Após a inicialização, os serviços estarão disponíveis nas seguintes portas:

| Serviço | URL | Credenciais Padrão |
| :--- | :--- | :--- |
| **Pedido Service (App)** | `http://localhost:8080` | N/A |
| **Prometheus** | `http://localhost:9090` | N/A |
| **Grafana** | `http://localhost:3000` | Usuário: `admin`, Senha: `admin` |

### 3. Endpoints da Aplicação

A aplicação Spring Boot expõe os seguintes endpoints:

*   **Endpoint de Negócio:** `http://localhost:8080/pedidos` (Simula a lógica de pedidos)
*   **Métricas do Actuator:** `http://localhost:8080/actuator/prometheus` (Endpoint de métricas consumido pelo Prometheus)
*   **Health Check:** `http://localhost:8080/actuator/health`

## 📊 Monitoramento e Alertas

### Prometheus

O arquivo `prometheus.yml` está configurado para:
*   Coletar métricas do próprio Prometheus (`localhost:9090`).
*   Coletar métricas do `pedido-service` (serviço `app:8080`) no path `/actuator/prometheus`.

### Regras de Alerta

O arquivo `alert.rules.yml` define as seguintes regras de alerta:

| Alerta | Expressão (PromQL) | Severidade | Descrição |
| :--- | :--- | :--- | :--- |
| `HighErrorRate` | `rate(http_server_requests_seconds_count{status="5xx"}[1m]) > 0.05` | `critical` | Dispara se a taxa de erros HTTP 5xx for superior a 5% no último minuto. |
| `HighLatency` | `histogram_quantile(0.95, sum(rate(http_server_requests_seconds_bucket{uri="/pedidos"}[5m])) by (le)) > 1` | `warning` | Dispara se o 95º percentil da latência do endpoint `/pedidos` exceder 1 segundo. |

### Grafana

O Grafana é a interface de visualização. Você deve configurá-lo para:
1.  Adicionar o Prometheus como **Data Source** (URL: `http://prometheus:9090`).
2.  Importar ou criar **Dashboards** para visualizar as métricas do `pedido-service`.

## 🛑 Parando e Removendo

Para parar e remover todos os contêineres, volumes e redes criados pelo Docker Compose, execute:

```bash
docker-compose down -v
```

## 📄 Estrutura do Projeto

```
.
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/pedidoservice/
│   │   │       ├── PedidoController.java
│   │   │       ├── PedidoService.java
│   │   │       └── PedidoServiceApplication.java
│   │   └── resources/
│   │       └── application.yml           # Configuração do Actuator
├── pom.xml                             # Dependências Maven (Spring Boot, Actuator, Micrometer)
├── Dockerfile                          # Definição da imagem Docker da aplicação
├── docker-compose.yml                  # Orquestração de App, Prometheus e Grafana
├── prometheus.yml                      # Configuração de jobs de coleta do Prometheus
└── alert.rules.yml                     # Regras de alerta do Prometheus
```

---
*README gerado por Manus AI*

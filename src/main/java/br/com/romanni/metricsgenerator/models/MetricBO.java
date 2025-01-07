package br.com.romanni.metricsgenerator.models;

import java.util.List;

public record MetricBO(
        List<Costumer> costumers,
        String month,
        String signature,
        int totalMembros,
        int membrosNovosRenovacaoProxima,
        int membrosAntigosRenovacaoProxima,
        int totalMembrosRenovacaoProxima,
        int renovacoesAntigasEfetuadas,
        int novasVendasEfetuadas,
        double porcentagemRenovacoes) {
}

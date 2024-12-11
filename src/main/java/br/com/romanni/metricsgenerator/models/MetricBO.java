package br.com.romanni.metricsgenerator.models;

public record MetricBO(
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

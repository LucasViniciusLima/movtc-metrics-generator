package br.com.romanni.metricsgenerator.business;

import br.com.romanni.metricsgenerator.models.ChartData;
import br.com.romanni.metricsgenerator.models.Costumer;
import br.com.romanni.metricsgenerator.models.MetricBO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataPDFGenerator {
    private DataPDFGenerator() {}

    public static JasperPrint generateJasperPrint(String signatureName, MetricBO metricBO, List<Costumer> costumersToPrint) throws JRException {
        JasperReport jasReport = (JasperReport) JRLoader.loadObject(
                new File("src/main/resources/Metrics_A4_Landscape.jasper"));

        double percentRenovacoes = BigDecimal.valueOf(metricBO.porcentagemRenovacoes())
                .setScale(2, RoundingMode.HALF_EVEN)
                .doubleValue();

        List<ChartData> chartDataList = List.of(
                new ChartData("Efetuadas", metricBO.porcentagemRenovacoes()),
                new ChartData("Pendentes", (100 - metricBO.porcentagemRenovacoes())));

        Map<String, Object> params = new HashMap<>();
        params.put("signatureLevel", signatureName);
        params.put("totalMembros", metricBO.totalMembros());
        params.put("membrosNovosRenovacaoProxima", metricBO.membrosNovosRenovacaoProxima());
        params.put("membrosAntigosRenovacaoProxima", metricBO.membrosAntigosRenovacaoProxima());
        params.put("totalMembrosRenovacaoProxima", metricBO.totalMembrosRenovacaoProxima());
        params.put("renovacoesAntigasEfetuadas", metricBO.renovacoesAntigasEfetuadas());
        params.put("novasVendasEfetuadas", metricBO.novasVendasEfetuadas());
        params.put("porcentagemRenovacoes", percentRenovacoes);
        params.put("month", metricBO.month());
        params.put("chartDataList", chartDataList);

        JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(costumersToPrint);
        return JasperFillManager.fillReport(jasReport, params, datasource);
    }
}

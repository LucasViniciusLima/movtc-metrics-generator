package br.com.romanni.metricsgenerator.business;

import br.com.romanni.metricsgenerator.models.ChartData;
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

    public static JasperPrint generatePDF(MetricBO metricBO) throws JRException {
        JasperReport jasReport = (JasperReport) JRLoader.loadObject(
                new File("src/main/resources/Metrics_A4_Landscape.jasper"));

        Map<String, Object> params = new HashMap<>();
        params.put("signatureLevel", metricBO.signature());
        params.put("totalMembros", metricBO.totalMembros());
        params.put("membrosNovosRenovacaoProxima", metricBO.membrosNovosRenovacaoProxima());
        params.put("membrosAntigosRenovacaoProxima", metricBO.membrosAntigosRenovacaoProxima());
        params.put("totalMembrosRenovacaoProxima", metricBO.totalMembrosRenovacaoProxima());
        params.put("renovacoesAntigasEfetuadas", metricBO.renovacoesAntigasEfetuadas());
        params.put("novasVendasEfetuadas", metricBO.novasVendasEfetuadas());


        //fixme
        BigDecimal bdPercentRenovacoes = new BigDecimal(metricBO.porcentagemRenovacoes()).setScale(2, RoundingMode.HALF_EVEN);
        params.put("porcentagemRenovacoes", bdPercentRenovacoes.doubleValue());
        params.put("month", metricBO.month());

        //fixme refactor everything


        List<ChartData> chartDataList = List.of(
                new ChartData("Efetuadas", metricBO.porcentagemRenovacoes()),
                new ChartData("Pendentes", (100 - metricBO.porcentagemRenovacoes())));
        params.put("chartDataList", chartDataList);

        JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(metricBO.costumers());

        return JasperFillManager.fillReport(jasReport, params, datasource);
    }
}

package br.com.romanni.metricsgenerator.business;

import br.com.romanni.metricsgenerator.enums.Headers;
import br.com.romanni.metricsgenerator.enums.SignatureLevel;
import br.com.romanni.metricsgenerator.factories.CostumerFactory;
import br.com.romanni.metricsgenerator.models.Costumer;
import br.com.romanni.metricsgenerator.models.MetricBO;
import br.com.romanni.metricsgenerator.utils.MOVTCMetricsDateUtil;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DataProcess {

    public void processCSV(String fileName) throws IOException, JRException {
        LocalDateTime initialDate = LocalDateTime.now();
        List<Costumer> costumers = getCostumersFromFile(fileName);

        var jasperPrints = generateJasperPrintFromEachSignatureAndStatus(costumers);

        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrints));

        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new FileOutputStream(getExportPath(fileName))));
        exporter.exportReport();

        Duration duration = Duration.between(initialDate, LocalDateTime.now());
        System.out.println("\nQuantidade de linhas lidas = " + costumers.size());
        System.out.println(String.format("\n%d segundos e %d nanosegundos.\n\n", duration.getSeconds(), duration.getNano()));
    }

    private static String getExportPath(String fileName) {
        String exportPath = fileName.substring(0, fileName.length() - 3);
        return exportPath + "pdf";
    }

    private List<Costumer> getCostumersFromFile(String fileName) throws IOException {
        CostumerFactory costumerFactory = new CostumerFactory();
        Reader in = new FileReader(fileName);

        Iterable<CSVRecord> records = CSVFormat.RFC4180.builder()
                .setHeader(Headers.class)
                .setSkipHeaderRecord(true)
                .build()
                .parse(in);

        return StreamSupport.stream(records.spliterator(), false)
                .map(costumerFactory::create)
                .toList();
    }

    private List<JasperPrint> generateJasperPrintFromEachSignatureAndStatus(List<Costumer> costumers) {
        List<MetricBO> metricBOList = List.of(
                createMovtBO(costumers, SignatureLevel.NEOPSICOLOGIA_MOVIMENTO_TRANSFORMACIONAL),
                createMovtBO(costumers, SignatureLevel.AUTOCONHECIMENTO_E_ATENDIMENTO_INDIVIDUAL),
                createMovtBO(costumers, SignatureLevel.PRIME),
                createMovtBO(costumers, SignatureLevel.GRUPO_VIRTUDE),
                createMovtBO(costumers, SignatureLevel.PRIME_PLUS_MOVT));

        return metricBOList.stream()
                .flatMap(mBO -> {
                    try {
                        return getRenewedAndRecoverJasperPrint(mBO);
                    } catch (JRException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    private Stream<JasperPrint> getRenewedAndRecoverJasperPrint(MetricBO metricBO) throws JRException {
        String signatureRecoverName = String.format("[PENDENTES / EM RECUPERAÇÃO] %s", metricBO.signature());
        JasperPrint costumersRenewed = DataPDFGenerator.generateJasperPrint(metricBO.signature(), metricBO, metricBO.costumersRenewed());
        JasperPrint costumersInRecover = DataPDFGenerator.generateJasperPrint(signatureRecoverName, metricBO, metricBO.costumersInRecover());
        return List.of(
                costumersRenewed,
                costumersInRecover
        ).stream();
    }

    private MetricBO createMovtBO(List<Costumer> costumers, SignatureLevel signatureLevel) {
        String monthBr = MOVTCMetricsDateUtil
                .getActualDate()
                .getMonth()
                .getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));

        System.out.println(String.format("\n\n%s", signatureLevel));
        System.out.println(String.format("\nMês de %s", monthBr));

        var totalCostumerList = getCostumerFilteredBySignature(costumers, signatureLevel);
        var inRecoverFirstYearList = getSignaturesInRecoverFirstYear(costumers, signatureLevel);
        var inRecoverNotFirstYearList = getSignaturesInRecoverNotFirstYear(costumers, signatureLevel);
        var inRecoverTotalSignaturesList = getSignaturesInRecover(costumers, signatureLevel);
        var renewedSignaturesList = getSignaturesRecentRenewed(costumers, signatureLevel);
        var recentPurchasesList = getSignaturesRecentPurchases(costumers, signatureLevel);
        var signatureRenewPercent = getSignaturesRenewPercent(inRecoverTotalSignaturesList.size(), renewedSignaturesList.size());

        System.out.println("Porcentagem de renovações: " + signatureRenewPercent);

        return new MetricBO(
                inRecoverTotalSignaturesList,
                renewedSignaturesList,
                monthBr,
                signatureLevel.toString(),
                totalCostumerList.size(),
                inRecoverFirstYearList.size(),
                inRecoverNotFirstYearList.size(),
                inRecoverTotalSignaturesList.size(),
                renewedSignaturesList.size(),
                recentPurchasesList.size(),
                signatureRenewPercent);
    }

    private List<Costumer> getCostumerFilteredBySignature(List<Costumer> costumers, SignatureLevel signatureLevel) {
        final var costumersFiltered = costumers.stream()
                .filter(c -> c.getSignatureLevel().equals(signatureLevel))
                .toList();

        System.out.println(String.format("Total de membros: %d", costumersFiltered.size()));
        return costumersFiltered;
    }

    private List<Costumer> getSignaturesInRecover(List<Costumer> costumers, SignatureLevel signatureLevel) {
        final var costumersFiltered = costumers.stream()
                .filter(c -> c.getSignatureLevel().equals(signatureLevel))
                .filter(c -> MOVTCMetricsDateUtil.isInRecoveryMonthTime(c.getExpirationDate()))
                .toList();

        System.out.println(String.format("Total de membros com renovação próxima: %d", costumersFiltered.size()));
        return costumersFiltered;
    }

    private List<Costumer> getSignaturesInRecoverNotFirstYear(List<Costumer> costumers, SignatureLevel signatureLevel) {
        final var costumersFiltered = costumers.stream()
                .filter(c -> c.getSignatureLevel().equals(signatureLevel))
                .filter(c -> MOVTCMetricsDateUtil.isInRecoveryMonthTime(c.getExpirationDate()) && !MOVTCMetricsDateUtil.isSignatureFirstYear(c))
                .toList();

        System.out.println(String.format("Membros antigos com renovação próxima: %d", costumersFiltered.size()));
        return costumersFiltered;
    }

    private List<Costumer> getSignaturesInRecoverFirstYear(List<Costumer> costumers, SignatureLevel signatureLevel) {
        final var costumersFiltered = costumers.stream()
                .filter(c -> c.getSignatureLevel().equals(signatureLevel))
                .filter(c -> MOVTCMetricsDateUtil.isInRecoveryMonthTime(c.getExpirationDate()) && MOVTCMetricsDateUtil.isSignatureFirstYear(c))
                .toList();

        System.out.println(String.format("Membros novos (primeiro ano) com renovação próxima: %d", costumersFiltered.size()));
        return costumersFiltered;
    }

    private List<Costumer> getSignaturesRecentRenewed(List<Costumer> costumers, SignatureLevel signatureLevel) {
        final var costumersFiltered = costumers.stream()
                .filter(c -> c.getSignatureLevel().equals(signatureLevel))
                .filter(c -> (c.getExpirationDate() != null && MOVTCMetricsDateUtil.isRecentRenewed(c)))
                .toList();

        System.out.println(String.format("Renovações (antigos) efetuadas: %d", costumersFiltered.size()));
        return costumersFiltered;
    }

    private List<Costumer> getSignaturesRecentPurchases(List<Costumer> costumers, SignatureLevel signatureLevel) {
        final var costumersFiltered = costumers.stream()
                .filter(c -> c.getSignatureLevel().equals(signatureLevel))
                .filter(c -> (c.getExpirationDate() != null && MOVTCMetricsDateUtil.isRecentPurchase(c)))
                .toList();

        System.out.println(String.format("Novas vendas efetuadas: %d", costumersFiltered.size()));
        return costumersFiltered;
    }

    private double getSignaturesRenewPercent(int openSignatures, int renewedSignatures) {
        double totalSignatures = (double) openSignatures + (double) renewedSignatures;

        if (totalSignatures <= 0) {
            return 0.0;
        }
        return (renewedSignatures * 100) / totalSignatures;
    }

}

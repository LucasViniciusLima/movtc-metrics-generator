package br.com.romanni.metricsgenerator;

import br.com.romanni.metricsgenerator.enums.Headers;
import br.com.romanni.metricsgenerator.enums.SignatureLevel;
import br.com.romanni.metricsgenerator.factories.CostumerFactory;
import br.com.romanni.metricsgenerator.models.Costumer;
import br.com.romanni.metricsgenerator.utils.MOVTCMetricsDateUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class App {


  public static void main(String[] args) throws IOException {
    LocalDateTime initialDate = LocalDateTime.now();
    CostumerFactory costumerFactory = new CostumerFactory();
    List<Costumer> costumers = new ArrayList<>();

    Reader in = new FileReader("src/main/resources/assinantes.csv");

    Iterable<CSVRecord> records = CSVFormat.RFC4180.builder()
            .setHeader(Headers.class)
            .build()
            .parse(in);

    boolean skipedHeader = false;

    for (CSVRecord record: records) {
      if (!skipedHeader) {
        skipedHeader = true;
        continue;
      }

      final var costumer = costumerFactory.create(record);
      costumers.add(costumer);
      //System.out.println(costumer);
    }

    /*
    showCostumerStatisticsFilteredBySignature(costumers, SignatureLevel.NEOPSICOLOGIA_MOVIMENTO_TRANSFORMACIONAL);
    showCostumerStatisticsFilteredBySignature(costumers, SignatureLevel.PRIME_PLUS_MOVT);
    showCostumerStatisticsFilteredBySignature(costumers, SignatureLevel.GRUPO_VIRTUDE);
    showCostumerStatisticsFilteredBySignature(costumers, SignatureLevel.PRIME);*/

    showSignaturesInRecover(costumers, SignatureLevel.NEOPSICOLOGIA_MOVIMENTO_TRANSFORMACIONAL);

    Duration duration = Duration.between(initialDate, LocalDateTime.now());

    System.out.println("\nQuantidade de linhas lidas = "+ costumers.size());
    System.out.println(String.format("\n%d segundos e %d nanosegundos.\n\n", duration.getSeconds(), duration.getNano()));
  }

  private static void showCostumerStatisticsFilteredBySignature(List<Costumer> costumers, SignatureLevel signatureLevel) {
    final var costumersFiltered = costumers.stream()
            .filter(c -> c.getSignatureLevel().equals(signatureLevel))
            .toList();

    System.out.println(String.format("%s -> { members: %d }",signatureLevel, costumersFiltered.size()));
  }

  private static void showSignaturesInRecover(List<Costumer> costumers, SignatureLevel signatureLevel) {
    final var costumersFiltered = costumers.stream()
            .filter(c -> c.getSignatureLevel().equals(signatureLevel))
            .filter(c -> MOVTCMetricsDateUtil.isInRecoveryMonthTime(c.getExpirationDate()))
            .toList();
    costumersFiltered.forEach(costumer -> System.out.println("ExpirationDate = " + costumer.getExpirationDate()));
    System.out.println(String.format("%s -> { members: %d }",signatureLevel, costumersFiltered.size()));
  }


}

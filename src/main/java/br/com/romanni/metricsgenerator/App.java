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
    showCostumerCountFilteredBySignature(costumers, SignatureLevel.NEOPSICOLOGIA_MOVIMENTO_TRANSFORMACIONAL);
    showCostumerCountFilteredBySignature(costumers, SignatureLevel.PRIME_PLUS_MOVT);
    showCostumerCountFilteredBySignature(costumers, SignatureLevel.GRUPO_VIRTUDE);
    showCostumerCountFilteredBySignature(costumers, SignatureLevel.PRIME);*/

    showData(costumers, SignatureLevel.NEOPSICOLOGIA_MOVIMENTO_TRANSFORMACIONAL);
    showData(costumers, SignatureLevel.AUTOCONHECIMENTO_E_ATENDIMENTO_INDIVIDUAL);
    showData(costumers, SignatureLevel.GRUPO_VIRTUDE);
    showData(costumers, SignatureLevel.PRIME);
    showData(costumers, SignatureLevel.PRIME_PLUS_MOVT);

    Duration duration = Duration.between(initialDate, LocalDateTime.now());

    System.out.println("\nQuantidade de linhas lidas = "+ costumers.size());
    System.out.println(String.format("\n%d segundos e %d nanosegundos.\n\n", duration.getSeconds(), duration.getNano()));
  }

  private static void showData(List<Costumer> costumers, SignatureLevel signatureLevel) {
    showCostumerCountFilteredBySignature(costumers, signatureLevel);
    showSignaturesInRecover(costumers, signatureLevel);
  }

  private static void showCostumerCountFilteredBySignature(List<Costumer> costumers, SignatureLevel signatureLevel) {
    final var costumersFiltered = costumers.stream()
            .filter(c -> c.getSignatureLevel().equals(signatureLevel))
            .toList();

    System.out.println(String.format("%s -> { total members: %d }",signatureLevel, costumersFiltered.size()));
  }

  private static void showSignaturesInRecover(List<Costumer> costumers, SignatureLevel signatureLevel) {
    final var costumersFiltered = costumers.stream()
            .filter(c -> c.getSignatureLevel().equals(signatureLevel))
            .filter(c -> MOVTCMetricsDateUtil.isInRecoveryMonthTime(c.getExpirationDate()))
            .toList();

    System.out.println(String.format("%s -> { members in expiration date: %d }",signatureLevel, costumersFiltered.size()));
  }

  /*
  os que são novos create date
  %venda nova data criação = data de criação  + 1 ano = data expiração.

  % de renovação e numero de renovação
  (data expiração - data atual)
  1 ano ou 11 meses
  * */


}

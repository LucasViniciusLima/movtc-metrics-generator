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
import java.util.List;
import java.util.stream.StreamSupport;

public class App {


  public static void main(String[] args) throws IOException {
    LocalDateTime initialDate = LocalDateTime.now();

    List<Costumer> costumers = getCostumersFromFile("src/main/resources/assinantes.csv");

    showData(costumers, SignatureLevel.NEOPSICOLOGIA_MOVIMENTO_TRANSFORMACIONAL);
    showData(costumers, SignatureLevel.AUTOCONHECIMENTO_E_ATENDIMENTO_INDIVIDUAL);
    showData(costumers, SignatureLevel.GRUPO_VIRTUDE);
    showData(costumers, SignatureLevel.PRIME);
    showData(costumers, SignatureLevel.PRIME_PLUS_MOVT);

    Duration duration = Duration.between(initialDate, LocalDateTime.now());

    System.out.println("\nQuantidade de linhas lidas = "+ costumers.size());
    System.out.println(String.format("\n%d segundos e %d nanosegundos.\n\n", duration.getSeconds(), duration.getNano()));
  }

  private static List<Costumer> getCostumersFromFile(String fileName) throws IOException{
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

  private static void showData(List<Costumer> costumers, SignatureLevel signatureLevel) {
    System.out.println(String.format("\n\n%s", signatureLevel));
    showCostumerCountFilteredBySignature(costumers, signatureLevel);
    showSignaturesInRecoverFirstYear(costumers, signatureLevel);
    showSignaturesInRecoverNotFirstYear(costumers, signatureLevel);
    showSignaturesInRecover(costumers, signatureLevel);
  }

  private static void showCostumerCountFilteredBySignature(List<Costumer> costumers, SignatureLevel signatureLevel) {
    final var costumersFiltered = costumers.stream()
            .filter(c -> c.getSignatureLevel().equals(signatureLevel))
            .toList();

    System.out.println(String.format("Total members: %d", costumersFiltered.size()));
  }

  private static void showSignaturesInRecover(List<Costumer> costumers, SignatureLevel signatureLevel) {
    final var costumersFiltered = costumers.stream()
            .filter(c -> c.getSignatureLevel().equals(signatureLevel))
            .filter(c -> MOVTCMetricsDateUtil.isInRecoveryMonthTime(c.getExpirationDate()))
            .toList();

    System.out.println(String.format("Total Members in expiration date: %d", costumersFiltered.size()));
  }

  private static void showSignaturesInRecoverNotFirstYear(List<Costumer> costumers, SignatureLevel signatureLevel) {
    final var costumersFiltered = costumers.stream()
            .filter(c -> c.getSignatureLevel().equals(signatureLevel))
            .filter(c -> MOVTCMetricsDateUtil.isInRecoveryMonthTime(c.getExpirationDate()))
            .filter(c -> !MOVTCMetricsDateUtil.isSignatureFirstYear(c))
            .toList();

    System.out.println(String.format("Members in expiration, not first purchase date: %d", costumersFiltered.size()));
  }

  private static void showSignaturesInRecoverFirstYear(List<Costumer> costumers, SignatureLevel signatureLevel) {
    final var costumersFiltered = costumers.stream()
            .filter(c -> c.getSignatureLevel().equals(signatureLevel))
            .filter(c -> MOVTCMetricsDateUtil.isInRecoveryMonthTime(c.getExpirationDate()))
            .filter(c -> MOVTCMetricsDateUtil.isSignatureFirstYear(c))
            .toList();

    System.out.println(String.format("First signature time, members in expiration date: %d", costumersFiltered.size()));
  }

  /*

  % de renovação e numero de renovação
  (data expiração - data atual)
  1 ano ou 11 meses
  * */


}

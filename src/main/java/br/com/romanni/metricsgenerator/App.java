package br.com.romanni.metricsgenerator;

import br.com.romanni.metricsgenerator.enums.Headers;
import br.com.romanni.metricsgenerator.factories.CostumerFactory;
import br.com.romanni.metricsgenerator.models.Costumer;
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

    for (CSVRecord record: records) {
      final var costumer = costumerFactory.create(record);
      costumers.add(costumer);
      System.out.println(costumer);
    }

    Duration duration = Duration.between(initialDate, LocalDateTime.now());

    System.out.println("\nQuantidade de linhas lidas = "+ costumers.size());
    System.out.println(String.format("%d segundos e %d nanosegundos.", duration.getSeconds(), duration.getNano()));
  }

}

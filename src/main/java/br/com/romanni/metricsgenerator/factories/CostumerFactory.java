package br.com.romanni.metricsgenerator.factories;

import br.com.romanni.metricsgenerator.enums.CostumerStatus;
import br.com.romanni.metricsgenerator.enums.Headers;
import br.com.romanni.metricsgenerator.enums.SignatureLevel;
import br.com.romanni.metricsgenerator.models.Costumer;
import br.com.romanni.metricsgenerator.models.DateIntRecord;
import lombok.NoArgsConstructor;
import org.apache.commons.csv.CSVRecord;

import java.time.LocalDateTime;

@NoArgsConstructor
public class CostumerFactory {

    public Costumer create(CSVRecord costumerCSVRecord){
        if (columnIsMappedOnCSV(costumerCSVRecord, Headers.PHONE_NUMBER)) {//fixme doesnt verify correctly
            return createSimplifiedCostumer(costumerCSVRecord);
        }

        return Costumer.builder()
                .fullName(costumerCSVRecord.get(Headers.NOME_COMPLETO))
                .email(costumerCSVRecord.get(Headers.E_MAIL))
                .actualLoginAt(columnIsMappedOnCSV(costumerCSVRecord, Headers.LOGIN_ATUAL_EM) ? this.mapToLocalDateTime(costumerCSVRecord.get(Headers.LOGIN_ATUAL_EM)) : null)
                .lastSeenAt(columnIsMappedOnCSV(costumerCSVRecord, Headers.ULTIMA_VEZ_VISTO) ? this.mapToLocalDateTime(costumerCSVRecord.get(Headers.ULTIMA_VEZ_VISTO)): null)
                .signatureLevel(columnIsMappedOnCSV(costumerCSVRecord, Headers.NIVEL_DE_ASSINATURA) ? SignatureLevel.getSignatureLevel(costumerCSVRecord.get(Headers.NIVEL_DE_ASSINATURA)): SignatureLevel.INVALID)
                .status(columnIsMappedOnCSV(costumerCSVRecord, Headers.STATUS) ? CostumerStatus.getCostumerStatus(costumerCSVRecord.get(Headers.STATUS)): null)
                .expirationDate(columnIsMappedOnCSV(costumerCSVRecord, Headers.DATA_DE_EXPIRACAO) ? this.mapLocalDateStringToLocalDateTime(costumerCSVRecord.get(Headers.DATA_DE_EXPIRACAO)): null)
                .createdDate(columnIsMappedOnCSV(costumerCSVRecord, Headers.DATA_DE_CRIACA) ? this.mapToLocalDateTime(costumerCSVRecord.get(Headers.DATA_DE_CRIACA)): null)
                .phone("vazio")
                .build();
    }

    public Costumer createSimplifiedCostumer(CSVRecord costumerCSVRecord) {
        return Costumer.builder()
                .fullName(costumerCSVRecord.get(Headers.NOME_COMPLETO))
                .email(costumerCSVRecord.get(Headers.E_MAIL))
                .phone(Boolean.TRUE.equals(phoneExist(costumerCSVRecord)) ? costumerCSVRecord.get(Headers.PHONE_NUMBER) : "nnn")
                .build();
    }

    private LocalDateTime mapLocalDateStringToLocalDateTime(String dateString){
        if (dateString.length() < 10) {
            return null;
        }

        String[] dateParts = dateString.split("-");

        if (dateParts.length != 3) {
            return null;
        }

        if (dateParts[2].length() > 2) {
            dateParts[2] = dateParts[2].substring(0, 2);
        }

        final var dateIntRecord = this.mapToDateIntRecordShort(dateParts);

        return LocalDateTime.of(
                dateIntRecord.year(),
                dateIntRecord.month(),
                dateIntRecord.day(),
                dateIntRecord.hour(),
                dateIntRecord.minute(),
                dateIntRecord.second());
    }

    private LocalDateTime mapToLocalDateTime(String date) {
        if (date.length() < 19) {
            return null;
        }
        String[] dateParts = date.split(" ");
        String[] localDateParts = dateParts[0].split("-");
        String[] timeParts = dateParts[1].split(":");

        if (localDateParts.length != 3 || timeParts.length != 3) {
            return null;
        }

        final var dateIntRecord = this.mapToDateIntRecordFull(localDateParts, timeParts);

        return LocalDateTime.of(
                dateIntRecord.year(),
                dateIntRecord.month(),
                dateIntRecord.day(),
                dateIntRecord.hour(),
                dateIntRecord.minute(),
                dateIntRecord.second());
    }

    private DateIntRecord mapToDateIntRecordFull(String[] localDateParts, String[] timeParts) {
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        int second = Integer.parseInt(timeParts[2]);

        final var dateIntShort = this.mapToDateIntRecordShort(localDateParts);
        return new DateIntRecord(dateIntShort.year(), dateIntShort.month(), dateIntShort.day(), hour, minute, second);
    }

    private DateIntRecord mapToDateIntRecordShort(String[] localDateParts) {
        int year = Integer.parseInt(localDateParts[0]);
        int month = Integer.parseInt(localDateParts[1]);
        int day = Integer.parseInt(localDateParts[2]);
        return new DateIntRecord(year, month, day, 0, 0, 0);
    }

    private boolean phoneExist(CSVRecord costumerCSVRecord){
        if (!columnIsMappedOnCSV(costumerCSVRecord, Headers.PHONE_NUMBER)){
            return false;
        }
        String phoneNumber = costumerCSVRecord.get(Headers.PHONE_NUMBER);
        return phoneNumber != null && !phoneNumber.isBlank() && !phoneNumber.isEmpty();
    }

    private boolean columnIsMappedOnCSV(CSVRecord costumerCSVRecord, Headers header) {
        return costumerCSVRecord.isMapped(header.getValue());
    }

}

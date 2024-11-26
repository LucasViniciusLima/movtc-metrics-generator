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

    public Costumer create(CSVRecord record){
        return Costumer.builder()
                .fullName(record.get(Headers.NOME_COMPLETO))
                .email(record.get(Headers.E_MAIL))
                .actualLoginAt(this.mapToLocalDateTime(record.get(Headers.LOGIN_ATUAL_EM)))
                .lastSeenAt(this.mapToLocalDateTime(record.get(Headers.ULTIMA_VEZ_VISTO)))
                .signatureLevel(SignatureLevel.getSignatureLevel(record.get(Headers.NIVEL_DE_ASSINATURA)))
                .status(CostumerStatus.getCostumerStatus(record.get(Headers.STATUS)))
                .expirationDate(this.mapLocalDateStringToLocalDateTime(record.get(Headers.DATA_DE_EXPIRACAO)))
                .createdDate(this.mapToLocalDateTime(record.get(Headers.DATA_DE_CRIACA)))
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







}

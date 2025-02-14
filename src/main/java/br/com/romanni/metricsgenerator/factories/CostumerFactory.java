package br.com.romanni.metricsgenerator.factories;

import br.com.romanni.metricsgenerator.enums.CostumerStatus;
import br.com.romanni.metricsgenerator.enums.HeaderBase;
import br.com.romanni.metricsgenerator.enums.HeaderSignature;
import br.com.romanni.metricsgenerator.enums.SignatureLevel;
import br.com.romanni.metricsgenerator.models.Costumer;
import br.com.romanni.metricsgenerator.models.DateIntRecord;
import lombok.NoArgsConstructor;
import org.apache.commons.csv.CSVRecord;

import java.time.LocalDateTime;

@NoArgsConstructor
public class CostumerFactory {

    public Costumer create(CSVRecord costumerCSVRecord) {
        if (isBaseCostumerSimplified(costumerCSVRecord)) {
            return createSimplifiedCostumer(costumerCSVRecord);
        }

        return Costumer.builder()
                .fullName(costumerCSVRecord.get(HeaderSignature.NOME_COMPLETO))
                .email(costumerCSVRecord.get(HeaderSignature.E_MAIL))
                .actualLoginAt(this.mapToLocalDateTime(costumerCSVRecord.get(HeaderSignature.LOGIN_ATUAL_EM)))
                .lastSeenAt(this.mapToLocalDateTime(costumerCSVRecord.get(HeaderSignature.ULTIMA_VEZ_VISTO)))
                .signatureLevel(SignatureLevel.getSignatureLevel(costumerCSVRecord.get(HeaderSignature.NIVEL_DE_ASSINATURA)))
                .status(CostumerStatus.getCostumerStatus(costumerCSVRecord.get(HeaderSignature.STATUS)))
                .expirationDate(this.mapLocalDateStringToLocalDateTime(costumerCSVRecord.get(HeaderSignature.DATA_DE_EXPIRACAO)))
                .createdDate(this.mapToLocalDateTime(costumerCSVRecord.get(HeaderSignature.DATA_DE_CRIACAO)))
                .phone("vazio")
                .build();
    }

    public Costumer createSimplifiedCostumer(CSVRecord costumerCSVRecord) {
        return Costumer.builder()
                .fullName(costumerCSVRecord.get(HeaderSignature.NOME_COMPLETO))
                .email(costumerCSVRecord.get(HeaderSignature.E_MAIL))
                .phone(Boolean.TRUE.equals(phoneExist(costumerCSVRecord)) ? costumerCSVRecord.get(HeaderBase.PHONE_NUMBER) : "vazio")
                .build();
    }

    private LocalDateTime mapLocalDateStringToLocalDateTime(String dateString) {
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

    private boolean phoneExist(CSVRecord costumerCSVRecord) {
        if (!columnIsMappedOnCSV(costumerCSVRecord, HeaderBase.PHONE_NUMBER.name())) {
            return false;
        }
        String phoneNumber = costumerCSVRecord.get(HeaderBase.PHONE_NUMBER);
        return phoneNumber != null && !phoneNumber.isBlank() && !phoneNumber.isEmpty();
    }

    private boolean columnIsMappedOnCSV(CSVRecord costumerCSVRecord, String header) {
        return costumerCSVRecord.isMapped(header);
    }

    private boolean isBaseCostumerSimplified(CSVRecord costumerCSVRecord) {
        boolean signatureNotMapped = !columnIsMappedOnCSV(costumerCSVRecord, HeaderSignature.NIVEL_DE_ASSINATURA.name());
        boolean phoneNumberMapped = costumerCSVRecord.isMapped(HeaderBase.PHONE_NUMBER.name());
        return signatureNotMapped && phoneNumberMapped;
    }

}

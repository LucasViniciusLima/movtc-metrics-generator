package br.com.romanni.metricsgenerator.factories;

import br.com.romanni.metricsgenerator.enums.CostumerStatus;
import br.com.romanni.metricsgenerator.enums.Headers;
import br.com.romanni.metricsgenerator.enums.SignatureLevel;
import br.com.romanni.metricsgenerator.models.Costumer;
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
                .expirationDate(this.mapToLocalDateTime(record.get(Headers.DATA_DE_EXPIRACAO)))
                .createdDate(this.mapToLocalDateTime(record.get(Headers.DATA_DE_CRIACA)))
                .build();
    }

    private LocalDateTime mapToLocalDateTime(String date) {
        return LocalDateTime.now();//fixme
    }







}

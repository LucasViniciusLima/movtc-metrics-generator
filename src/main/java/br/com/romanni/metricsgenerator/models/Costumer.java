package br.com.romanni.metricsgenerator.models;

import br.com.romanni.metricsgenerator.enums.CostumerStatus;
import br.com.romanni.metricsgenerator.enums.SignatureLevel;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Costumer {

    private String fullName;
    private String email;
    private String phone = "vazio";
    private LocalDateTime actualLoginAt;
    private LocalDateTime lastSeenAt;
    private SignatureLevel signatureLevel;
    private CostumerStatus status;
    private LocalDateTime expirationDate;
    private LocalDateTime createdDate;

}

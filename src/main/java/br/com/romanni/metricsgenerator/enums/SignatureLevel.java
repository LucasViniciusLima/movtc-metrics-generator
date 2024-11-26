package br.com.romanni.metricsgenerator.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum SignatureLevel {
    INVALID("INVALID"),
    NEOPSICOLOGIA_MOVIMENTO_TRANSFORMACIONAL("Neopsicologia - Movimento Transformacional"),
    PRIME_PLUS_MOVT("Prime + MOVT"),
    AUTOCONHECIMENTO_E_ATENDIMENTO_INDIVIDUAL("Autoconhecimento e atendimento individual"),
    GRUPO_VIRTUDE("Grupo Virtude"),
    PRIME("PRIME");

    private final String value;

    public static SignatureLevel getSignatureLevel(String code) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getValue(), code))
                .findFirst().orElse(INVALID);
    }

    @Override
    public String toString(){
        return this.value;
    }

}

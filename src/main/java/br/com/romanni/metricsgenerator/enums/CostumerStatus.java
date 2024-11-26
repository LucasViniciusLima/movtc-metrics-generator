package br.com.romanni.metricsgenerator.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum CostumerStatus {
    INVALID("INVALID"),
    EXPIRADO("Expirado"),
    ATIVADO("Ativado"),
    DESATIVADO("Desativado"),
    PENDENTE("Pendente");

    private final String value;

    public static CostumerStatus getCostumerStatus(String code) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getValue(), code))
                .findFirst().orElse(INVALID);
    }

    @Override
    public String toString(){
        return this.value;
    }
}

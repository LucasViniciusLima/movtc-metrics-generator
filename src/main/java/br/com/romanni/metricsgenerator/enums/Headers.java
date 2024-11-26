package br.com.romanni.metricsgenerator.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Headers {
    NOME_COMPLETO("Nome completo"),
    E_MAIL("E-mail"),
    LOGIN_ATUAL_EM("Login atual em"),
    ULTIMA_VEZ_VISTO("Última vez visto"),
    NIVEL_DE_ASSINATURA("Nível de Assinatura"),
    STATUS("Status"),
    DATA_DE_EXPIRACAO("Data de expiração"),
    DATA_DE_CRIACA("Data de criação");

    private final String value;

    @Override
    public String toString(){
        return this.value;
    }
}

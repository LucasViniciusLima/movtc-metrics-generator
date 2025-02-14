package br.com.romanni.metricsgenerator.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HeaderBase {
    ID("Id"),
    NOME_COMPLETO("Nome completo"),
    E_MAIL("E-mail"),
    LOGIN_ATUAL_EM("Login atual em"),
    ULTIMA_VEZ_VISTO("Última vez visto"),
    CONTADOR_LOGINS("Contador de logins"),
    CRIADO_EM("Criado em"),
    CPF_CNPJ("CPF/CNPJ"),
    PHONE_NUMBER("Telefone");

    private final String value;

    @Override
    public String toString(){
        return this.value;
    }
}

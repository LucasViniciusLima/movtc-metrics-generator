package br.com.romanni.metricsgenerator.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Headers {
    ID("Id"),
    NOME_COMPLETO("Nome completo"),
    E_MAIL("E-mail"),
    LOGIN_ATUAL_EM("Login atual em"),
    ULTIMA_VEZ_VISTO("Última vez visto"),
    NIVEL_DE_ASSINATURA("Nível de Assinatura"),
    STATUS("Status"),
    DATA_DE_EXPIRACAO("Data de expiração"),
    DATA_DE_CRIACA("Data de criação"),
    PHONE_NUMBER("Telefone"),
    CONTADOR_LOGINS("Contador de logins"),
    CRIADO_EM("Criado em"),
    CPF_CNPJ("CPF/CNPJ");

    private final String value;

    @Override
    public String toString(){
        return this.value;
    }
}

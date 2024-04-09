package br.com.dbc.vemser.iShirts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MensagemRetorno {

    CADASTRO_COM_SUCESSO("Cadastro realizado com sucesso"),
    EDITADO_COM_SUCESSO("Editado com sucesso"),
    EXCLUIDA_COM_SUCESSO("Deletado com sucesso");

    private String mensagemRetorno;
}

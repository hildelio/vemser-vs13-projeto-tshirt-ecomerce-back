package br.com.dbc.vemser.iShirts.dto.usuario;

import br.com.dbc.vemser.iShirts.dto.pessoa.PessoaCreateDTO;
import br.com.dbc.vemser.iShirts.model.Pessoa;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioUpdateDTO {
    @NotBlank
    @Schema(description = "E-mail do Usuário", required = true, example = "cliente@dbccompany.com.br")
    @Pattern(regexp = "^[A-Za-z]{2,}@[A-Za-z]{2,}\\.[A-Za-z]{2,}$",
            message = "E-mail não cumpre o formato requerido.")
    private String email;
}

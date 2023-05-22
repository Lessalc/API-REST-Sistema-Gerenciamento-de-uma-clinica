package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;

public record DadosCadastroMedico(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank @Email String email,
        @NotBlank(message = "{telefone.obrigatorio}") String telefone,
        @NotBlank @Pattern(regexp = "\\d{4,6}", message="{crm.invalido}")
        String crm,
        @NotNull Especialidade especialidade,
        @NotNull @Valid DadosEndereco endereco
) {
}

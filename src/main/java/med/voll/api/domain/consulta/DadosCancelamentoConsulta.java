package med.voll.api.domain.consulta;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record  DadosCancelamentoConsulta(
        @NotNull
        Long consulta_id,
        @NotNull
        MotivoCancelamento motivo
) {
}

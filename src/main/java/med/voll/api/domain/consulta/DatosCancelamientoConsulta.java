package med.voll.api.domain.consulta;

import jakarta.validation.constraints.NotNull;

public record DatosCancelamientoConsulta(@NotNull MotivoCancelamiento motivoCancelamiento) {
}

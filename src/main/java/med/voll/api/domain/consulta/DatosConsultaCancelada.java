package med.voll.api.domain.consulta;

import java.time.LocalDateTime;

public record DatosConsultaCancelada(Long id, Long idPaciente, Long idMedico, LocalDateTime fecha, MotivoCancelamiento motivoCancelamiento) {
    public DatosConsultaCancelada(Consulta consulta) {
        this(consulta.getId(), consulta.getPaciente().getId(), consulta.getMedico().getId(), consulta.getFecha(), consulta.getMotivoCancelamiento());
    }
}

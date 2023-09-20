package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PacienteSinConsulta implements ValidadorDeConsultas {

    @Autowired
    ConsultaRepository repository;

    public void validar(DatosAgendarConsulta datos){
        LocalDateTime primerHorario = datos.fecha().withHour(7);
        LocalDateTime ultimoHorario = datos.fecha().withHour(18);

        Boolean pacienteConConsulta = repository.existsByPacienteIdAndFechaBetween(datos.idPaciente(), primerHorario, ultimoHorario);

        if(pacienteConConsulta){
            throw new ValidationException("El paciente ya tiene una consulta para ese dia");
        }

    }

}

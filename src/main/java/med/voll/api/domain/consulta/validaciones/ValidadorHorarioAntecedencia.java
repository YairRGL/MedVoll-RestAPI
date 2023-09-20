package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosCancelamientoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioAntecedencia implements ValidadorCancelamientoDeConsulta {

    @Autowired
    private ConsultaRepository repository;


    @Override
    public void validar(Long id, DatosCancelamientoConsulta datos) {
        Consulta consulta = repository.getReferenceById(id);
        LocalDateTime ahora = LocalDateTime.now();
        long diferenciaEnHoras = Duration.between(ahora, consulta.getFecha()).toHours();

        if(diferenciaEnHoras<24){
            throw new ValidationException("La consulta solo puede ser cancelada con al menos 24 horas de antelacion");
        }

    }
}

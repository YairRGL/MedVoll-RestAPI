package med.voll.api.domain.consulta.validaciones;

import med.voll.api.domain.consulta.DatosCancelamientoConsulta;

public interface ValidadorCancelamientoDeConsulta {
    void validar(Long id, DatosCancelamientoConsulta datos);
}

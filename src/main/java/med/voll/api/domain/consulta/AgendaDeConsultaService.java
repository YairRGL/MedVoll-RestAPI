package med.voll.api.domain.consulta;

import med.voll.api.domain.consulta.validaciones.ValidadorCancelamientoDeConsulta;
import med.voll.api.domain.consulta.validaciones.ValidadorDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errores.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private List<ValidadorDeConsultas> validadores;

    @Autowired
    private List<ValidadorCancelamientoDeConsulta> validadoresCancelamiento;

    public DatosDetallesConsulta agendar(DatosAgendarConsulta datos){
        if(!pacienteRepository.findById(datos.idPaciente()).isPresent()){
            throw new ValidacionDeIntegridad("Este id para el paciente no fue encontrado");
        }
        
        if(datos.idMedico()!=null && !medicoRepository.existsById(datos.idMedico())){
            throw new ValidacionDeIntegridad("Este id para el medico no fue encontrado");
        }

        validadores.forEach(validadorDeConsultas -> validadorDeConsultas.validar(datos));

        Medico medico = seleccionarMedico(datos);

        if(medico==null){
            throw new ValidacionDeIntegridad("No existen medicos disponibles para este horario y especialidad");
        }

        Paciente paciente = pacienteRepository.findById(datos.idPaciente()).get();

        Consulta consulta = new Consulta(medico, paciente, datos.fecha());

        consultaRepository.save(consulta);

        return new DatosDetallesConsulta(consulta);
    }

    public DatosConsultaCancelada cancelar(Long id, DatosCancelamientoConsulta datos){
        if(!consultaRepository.existsById(id)){
            throw new ValidacionDeIntegridad("Id de la consulta no existe");
        }

        validadoresCancelamiento.forEach(v -> v.validar(id, datos));

        Consulta consulta = consultaRepository.getReferenceById(id);
        consulta.cancelar(datos.motivoCancelamiento());
        return new DatosConsultaCancelada(consulta);
    }

    private Medico seleccionarMedico(DatosAgendarConsulta datos) {
        if(datos.idMedico()!=null){
            return medicoRepository.getReferenceById(datos.idMedico());
        }

        if(datos.especialidad()==null){
            throw new ValidacionDeIntegridad("Debe seleccionarse una especialidad para el medico");
        }

        return medicoRepository.seleccionarMedicoConEspecialidadEnFecha(datos.especialidad(), datos.fecha());
    }

}

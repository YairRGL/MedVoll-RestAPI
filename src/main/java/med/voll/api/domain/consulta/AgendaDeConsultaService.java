package med.voll.api.domain.consulta;

import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errores.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgendaDeConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;


    public void agendar(DatosAgendarConsulta datos){
        if(pacienteRepository.findById(datos.idPaciente()).isPresent()){
            throw new ValidacionDeIntegridad("Este id para el paciente no fue encontrado");
        }
        
        if(datos.idMedico()!=null && medicoRepository.existsById(datos.idMedico())){
            throw new ValidacionDeIntegridad("Este id para el medico no fue encontrado");
        }

        Medico medico = seleccionarMedico(datos);
        Paciente paciente = pacienteRepository.findById(datos.idPaciente()).get();

        Consulta consulta = new Consulta(null, medico, paciente, datos.fecha());

        consultaRepository.save(consulta);
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

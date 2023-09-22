package med.voll.api.domain.medico;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.paciente.DatosRegistroPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    MedicoRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Deberia retornar nulo cuando el medico se encuentre en consulta con otro paciente en ese horario")
    void seleccionarMedicoConEspecialidadEnFechaEscenario1() {
        //Given
        LocalDateTime proximoLunes10H = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);

        Medico medico = registrarMedico("Jose", "jose@gmail.com", "1112223334", "12345", Especialidad.ORTOPEDIA);
        Paciente paciente = registrarPaciente("Antonio", "antonio@gmail.com", "1112223334", "12312");
        registrarConsulta(medico, paciente, proximoLunes10H);

        //When
        Medico medicoLibre = repository.seleccionarMedicoConEspecialidadEnFecha(Especialidad.ORTOPEDIA, proximoLunes10H);

        //Then
        assertThat(medicoLibre).isNull();


    }

    @Test
    @DisplayName("Deberia retornar un medico cuando realice la consulta en la base de datos para ese horario")
    void seleccionarMedicoConEspecialidadEnFechaEscenario2() {
        LocalDateTime proximoLunes10H = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);

        Medico medico = registrarMedico("Jose", "jose@gmail.com", "1112223334", "12345", Especialidad.ORTOPEDIA);

        Medico medicoLibre = repository.seleccionarMedicoConEspecialidadEnFecha(Especialidad.ORTOPEDIA, proximoLunes10H);

        assertThat(medicoLibre).isEqualTo(medico);


    }

    private void registrarConsulta(Medico medico, Paciente paciente, LocalDateTime fecha){
        em.persist(new Consulta(medico, paciente, fecha));
    }

    private Medico registrarMedico(String nombre, String email, String telefono, String documento, Especialidad especialidad){
        Medico medico = new Medico(datosMedico(nombre, email, telefono, documento, especialidad));
        em.persist(medico);
        return medico;
    }

    private DatosRegistroMedico datosMedico(String nombre, String email, String telefono, String documento, Especialidad especialidad){
        return new DatosRegistroMedico(nombre, email, telefono, documento , especialidad, datosDireccion());
    }

    private Paciente registrarPaciente(String nombre, String email, String telefono, String documento) {
        Paciente paciente = new Paciente(datosPaciente(nombre, email, telefono, documento));
        em.persist(paciente);
        return paciente;
    }

    private DatosRegistroPaciente datosPaciente(String nombre, String email, String telefono, String documento) {
        return new DatosRegistroPaciente(nombre, email, telefono, documento, datosDireccion());
    }

    private DatosDireccion datosDireccion(){
        return new DatosDireccion(
                "Juarez",
                "Margaritas",
                "Acapulco",
                "123",
                "12"
        );
    }

}
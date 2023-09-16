package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroPaciente datos, UriComponentsBuilder uriBuilder){
        Paciente paciente = new Paciente(datos);
        repository.save(paciente);

        URI uri = uriBuilder.path("pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosDetallesPaciente((paciente)));
    }

    @GetMapping
    public ResponseEntity<Page<DatosListaPaciente>> listar(@PageableDefault(size = 10, sort = {"nombre"}) Pageable paginacion){
        Page<DatosListaPaciente> page = repository.findAllByActivoTrue(paginacion).map(DatosListaPaciente::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity actualizar(@RequestBody @Valid DatosActualizacionPaciente datos){
        Paciente paciente = repository.getReferenceById(datos.id());
        paciente.actualizarInformacion(datos);
        return ResponseEntity.ok(new DatosDetallesPaciente(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminar(@PathVariable Long id){
        Paciente paciente = repository.getReferenceById(id);
        paciente.eliminar();

        return ResponseEntity.ok(new DatosDetallesPaciente(paciente));
    }

    @GetMapping("/{id}")
    public ResponseEntity detallar(@PathVariable Long id){
        Paciente paciente = repository.getReferenceById(id);
        return ResponseEntity.ok(new DatosDetallesPaciente(paciente));
    }

}

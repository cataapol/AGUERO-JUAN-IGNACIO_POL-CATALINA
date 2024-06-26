package com.backend.ClinicaOdontologica.service.impl;


import com.backend.ClinicaOdontologica.dto.entrada.TurnoEntradaDto;
import com.backend.ClinicaOdontologica.dto.salida.OdontologoSalidaDto;
import com.backend.ClinicaOdontologica.dto.salida.PacienteSalidaDto;
import com.backend.ClinicaOdontologica.dto.salida.TurnoSalidaDto;
import com.backend.ClinicaOdontologica.entity.Turno;
import com.backend.ClinicaOdontologica.exception.BadRequestException;
import com.backend.ClinicaOdontologica.exception.ResourceNotFoundException;
import com.backend.ClinicaOdontologica.repository.ITurnoRepository;
import com.backend.ClinicaOdontologica.service.ITurnoService;
import com.backend.ClinicaOdontologica.utils.JsonPrinter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurnoService implements ITurnoService {

    private final Logger LOGGER = LoggerFactory.getLogger(PacienteService.class);

    private ITurnoRepository turnoRepository;

    private ModelMapper modelMapper;

    private PacienteService pacienteService;

    private OdontologoService odontologoService;


    public TurnoService(ITurnoRepository turnoRepository, ModelMapper modelMapper, PacienteService pacienteService, OdontologoService odontologoService) {
        this.turnoRepository = turnoRepository;
        this.modelMapper = modelMapper;
        this.pacienteService = pacienteService;
        this.odontologoService = odontologoService;
    }

    @Override
    public TurnoSalidaDto registrar(TurnoEntradaDto turnoEntradaDto) throws BadRequestException {

        TurnoSalidaDto turnoSalidaDto;

        PacienteSalidaDto paciente = pacienteService.buscarPorId(turnoEntradaDto.getPacienteId());
        OdontologoSalidaDto odontologo = odontologoService.buscarOdontologoPorId(turnoEntradaDto.getOdontologoId());

        if (odontologo == null || paciente == null) {


            if(odontologo == null && paciente == null) {
                LOGGER.error("No existe el paciente ni el dontólogo, no se pudo crear el turno ");
                throw new BadRequestException("No se pudo crear el turno, no existe el paciente ni el odontólogo  ");

            } else if(paciente == null){

                LOGGER.error("El paciente no se encuentra en la base de datos");
                throw new BadRequestException("El paciente no se encuentra en la base de datos");

            } else{

                LOGGER.error("El odontologo no se encuentra en la base de datos");
                throw new BadRequestException("El odontologo no se encuentra en la base de datos");

            }

        } else {

            Turno turnoNuevo = turnoRepository.save(modelMapper.map(turnoEntradaDto, Turno.class));

            turnoSalidaDto = entidadASalidaDto(turnoNuevo);

            LOGGER.info("Turno registrado con éxito: {}", turnoSalidaDto);
        }

        return turnoSalidaDto;
    }



    @Override
    public TurnoSalidaDto buscarPorId(Long id) {
        Turno turnoEntity = turnoRepository.findById(id).orElse(null);

        TurnoSalidaDto turnoEncontrado = null;

        if(turnoEncontrado != null) {

            turnoEncontrado =  modelMapper.map(turnoEntity, TurnoSalidaDto.class);

            LOGGER.info("Se ha encontrado el turno: {}", JsonPrinter.toString(turnoEncontrado));

        } else LOGGER.error("No se ha encontrado el turno...");

        return turnoEncontrado;
    }




    @Override
    public List<TurnoSalidaDto> listarTodos() {

       List<TurnoSalidaDto> turnos = turnoRepository.findAll().stream()
               .map(this::entidadASalidaDto).toList();

       return turnos;
    }


    @Override
    public void eliminarTurno(Long id) throws ResourceNotFoundException{

        if (turnoRepository.findById(id) != null){
            turnoRepository.deleteById(id);
            LOGGER.warn("El turno con id {} eliminado! ", id);
        } else {
            throw new ResourceNotFoundException("No existe registro de turno con el id " + id);
        }

    }

    @Override
    public TurnoSalidaDto modificarTurno(TurnoEntradaDto turnoEntradaDto, Long id) throws ResourceNotFoundException {

        Turno turnoRecibido = modelMapper.map(turnoEntradaDto, Turno.class);

        Turno turnoAActualizar = turnoRepository.findById(id).orElse(null);

        TurnoSalidaDto turnoSalidaDto;

        if (turnoAActualizar != null) {
            turnoAActualizar.setFechaYHora(turnoRecibido.getFechaYHora());
            turnoAActualizar.setPaciente(turnoRecibido.getPaciente());
            turnoAActualizar.setOdontologo(turnoRecibido.getOdontologo());
            turnoRepository.save(turnoAActualizar);

            turnoSalidaDto = entidadASalidaDto(turnoAActualizar);

            LOGGER.warn("Turno actualizado: {}", turnoSalidaDto);

        } else {
            LOGGER.error("No fue posible actualizar los datos ya que el turno no se encuentra registrado");
            throw new ResourceNotFoundException("No fue posible actualizar los datos ya que el turno no se encuentra registrado");
        }

        return turnoSalidaDto;
    }



    public List<TurnoSalidaDto> obtenerTodosLosTurnos() {
        List<Turno> turnos = turnoRepository.findAll();
        List<TurnoSalidaDto> turnosSalida = turnos.stream()
                .map(turno -> modelMapper.map(turno, TurnoSalidaDto.class))
                .toList();

        return turnosSalida;
    }


    private PacienteSalidaDto pacienteSalidaDtoATurnoDto(Long id){
        return pacienteService.buscarPorId(id);
    }
    private OdontologoSalidaDto odontologoSalidaDtoATurnoDto(Long id){
        return odontologoService.buscarOdontologoPorId(id);
    }

    private TurnoSalidaDto entidadASalidaDto(Turno turno) {
        TurnoSalidaDto turnoSalidaDto = modelMapper.map(turno, TurnoSalidaDto.class);
        turnoSalidaDto.setPacienteSalidaDto(pacienteSalidaDtoATurnoDto(turno.getPaciente().getId()));
        turnoSalidaDto.setOdontologoSalidaDto(odontologoSalidaDtoATurnoDto(turno.getOdontologo().getId()));
        return turnoSalidaDto;
    }
}

package com.backend.ClinicaOdontologica.service;

import com.backend.ClinicaOdontologica.dto.entrada.TurnoEntradaDto;
import com.backend.ClinicaOdontologica.dto.salida.TurnoSalidaDto;
import com.backend.ClinicaOdontologica.exception.BadRequestException;
import com.backend.ClinicaOdontologica.exception.ResourceNotFoundException;

import java.util.List;

public interface ITurnoService {

    TurnoSalidaDto registrar(TurnoEntradaDto turnoEntradaDto) throws BadRequestException;

    TurnoSalidaDto buscarPorId(Long id);

    List<TurnoSalidaDto> listarTodos();

    void eliminarTurno(Long id) throws ResourceNotFoundException;


    TurnoSalidaDto modificarTurno(TurnoEntradaDto turnoEntradaDto, Long id) throws ResourceNotFoundException;
}

package com.backend.ClinicaOdontologica.controller;

import com.backend.ClinicaOdontologica.dto.entrada.OdontologoEntradaDto;
import com.backend.ClinicaOdontologica.dto.salida.OdontologoSalidaDto;
import com.backend.ClinicaOdontologica.exception.BadRequestException;
import com.backend.ClinicaOdontologica.exception.ResourceNotFoundException;
import com.backend.ClinicaOdontologica.service.IOdontologoService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/odontologos")
public class OdontologoController {


    private IOdontologoService odontologoService;

    public OdontologoController(IOdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }






    @PostMapping("registrar")
    public ResponseEntity<OdontologoSalidaDto> registrarOdontologo(@RequestBody @Valid OdontologoEntradaDto odontologo) throws BadRequestException{
        return new ResponseEntity<>(odontologoService.registraOdontologo(odontologo), HttpStatus.CREATED);
    }



    @GetMapping("/buscar/{id}")
    public ResponseEntity<OdontologoSalidaDto> buscarOdontologoPorID(@PathVariable Long id) {
        return new ResponseEntity<>(odontologoService.buscarOdontologoPorId(id), HttpStatus.OK);
    }


    @GetMapping("/buscarTodos")
    public ResponseEntity<List<OdontologoSalidaDto>> listarTodosLosPacientes() {
        return new ResponseEntity<>(odontologoService.listarTodosLosOdontologos(),HttpStatus.OK);
    }



    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarOdontologo(@PathVariable Long id) throws ResourceNotFoundException{
        odontologoService.eliminarOdontologoPorId(id);
        return new ResponseEntity<>("Odontologo eliminado correctamente", HttpStatus.NO_CONTENT);
    }


    @PutMapping("/actualizar/{id}")
    public ResponseEntity<OdontologoSalidaDto> modificarOdontologo(@RequestBody @Valid OdontologoEntradaDto odontologo, @PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>( odontologoService.modificarOdontologo(odontologo, id), HttpStatus.CREATED);
    }


}

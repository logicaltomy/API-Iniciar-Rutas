package cl.condor.iniciar_rutas_api.controller;

import cl.condor.iniciar_rutas_api.model.AbrirRuta;
import cl.condor.iniciar_rutas_api.service.AbrirRutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/abrir-ruta")
public class AbrirRutaController {

    @Autowired
    private AbrirRutaService abrirRutaService;

    @GetMapping
    public ResponseEntity<List<AbrirRuta>> getAllAbrirRuta() {
        List<AbrirRuta> lista = abrirRutaService.findAll();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AbrirRuta> getAbrirRutaById(@PathVariable Integer id) {
        try {
            AbrirRuta abrirRuta = abrirRutaService.findById(id);
            return ResponseEntity.ok(abrirRuta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Se agrego esto con el fin de que sea ocupado por API Logros (No esta probado)
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<AbrirRuta>> getAbrirRutaByIdUsuario(@PathVariable Integer id) {
        try {
            List<AbrirRuta> abrirRuta = abrirRutaService.findByIdUsuario(id);
            return ResponseEntity.ok(abrirRuta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<AbrirRuta> createAbrirRuta(@RequestBody AbrirRuta abrirRuta) {
        try {
            AbrirRuta saved = abrirRutaService.save(abrirRuta);
            return ResponseEntity.status(201).body(saved);
        }catch (WebClientRequestException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

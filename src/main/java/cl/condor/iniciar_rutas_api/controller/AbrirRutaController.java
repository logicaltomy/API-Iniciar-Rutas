package cl.condor.iniciar_rutas_api.controller;

import cl.condor.iniciar_rutas_api.model.AbrirRuta;
import cl.condor.iniciar_rutas_api.service.AbrirRutaService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
        summary = "Listar todas las rutas inicializadas",
        description = "Retorna todas las rutas que han sido inicializadas por cualquier usuario.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de rutas inicializadas obtenida exitosamente."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No hay rutas inicializadas.")
        }
    )
    @GetMapping
    public ResponseEntity<List<AbrirRuta>> getAllAbrirRuta() {
        List<AbrirRuta> lista = abrirRutaService.findAll();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(
        summary = "Obtener inicialización de ruta por ID",
        description = "Devuelve la inicialización de ruta correspondiente al ID especificado.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Inicialización encontrada."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Inicialización no encontrada.")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AbrirRuta> getAbrirRutaById(@PathVariable Integer id) {
        try {
            AbrirRuta abrirRuta = abrirRutaService.findById(id);
            return ResponseEntity.ok(abrirRuta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Listar rutas inicializadas por usuario",
        description = "Devuelve todas las rutas que ha realizado el usuario especificado por su ID.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de rutas del usuario obtenida exitosamente."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario o rutas no encontradas.")
        }
    )
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<AbrirRuta>> getAbrirRutaByIdUsuario(@PathVariable Integer id) {
        try {
            List<AbrirRuta> abrirRuta = abrirRutaService.findByIdUsuario(id);
            return ResponseEntity.ok(abrirRuta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Registrar inicio de ruta",
        description = "Crea una nueva inicialización de ruta para un usuario. La fecha de inicio se asigna automáticamente.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "JSON con los datos de la inicialización de ruta.",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\n  \"id_usuario\": 1,\n  \"id_ruta\": 2\n}"
                )
            )
        ),
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Inicialización de ruta creada exitosamente."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "Servicio externo no disponible.")
        }
    )
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

    @Operation(
        summary = "Marcar fin de ruta",
        description = "Actualiza la fecha de fin de la ruta, diferenciando si está en progreso o finalizada.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ruta marcada como finalizada exitosamente."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Inicialización de ruta no encontrada."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "Servicio externo no disponible.")
        }
    )
    @PatchMapping("/marcarFin/{id}")
    public ResponseEntity<AbrirRuta> marcarFin(@PathVariable Integer id) {
        try {
            AbrirRuta abrirRuta =  abrirRutaService.marcarFin(id);
            return ResponseEntity.ok(abrirRuta);
        }catch (WebClientRequestException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }catch (RuntimeException e) {
            if (e.getMessage().equals("AbrirRuta no encontrada")){
                return  ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

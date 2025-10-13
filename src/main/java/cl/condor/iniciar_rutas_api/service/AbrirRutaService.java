package cl.condor.iniciar_rutas_api.service;

import cl.condor.iniciar_rutas_api.model.AbrirRuta;
import cl.condor.iniciar_rutas_api.repository.AbrirRutaRepository;
import cl.condor.iniciar_rutas_api.webclient.EstadoClient;
import cl.condor.iniciar_rutas_api.webclient.RutaClient;
import cl.condor.iniciar_rutas_api.webclient.UsuarioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional

public class AbrirRutaService {
    @Autowired
    private AbrirRutaRepository abrirRutaRepository;
    //para posibilitar los clientes que otorgarán su fk mediante el webclient
    @Autowired
    private EstadoClient estadoClient;
    @Autowired
    private RutaClient rutaClient;
    @Autowired
    private UsuarioClient usuarioClient;

    public List<AbrirRuta> findAll() {
        return abrirRutaRepository.findAll();
    }

    public AbrirRuta findById(Integer id) {
        return abrirRutaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AbrirRuta no encontrada"));
    }

    //Se agrego esto con el fin de que sea ocupado por API Logros (No esta probado)
    public List<AbrirRuta> findByIdUsuario(Integer id) {
        List<AbrirRuta> rutas = abrirRutaRepository.findAbrirRutaByIdUsuario(id);

        if (rutas.isEmpty()) {
            throw new RuntimeException("No se encontraron rutas para el usuario con id " + id);
        }

        return rutas;
    }

    // AbrirRuta depende de muchas tablas para crearla
    // Un total de 3 tablas/clases instanciadas y mapeadas
    public AbrirRuta save(AbrirRuta abrirRuta) {
        // mapeando los string para traer sus id a partir de web client
        // dichos campos a rellenar ya se declararon previamente en el model
        if (abrirRuta.getIdUsuario() == null)
            throw new RuntimeException("id_usuario es obligatorio");
        if (abrirRuta.getIdRuta() == null)
            throw new RuntimeException("id_ruta es obligatorio");
        if (abrirRuta.getIdEstado() == null)
            throw new RuntimeException("id_estado es obligatorio");

        usuarioClient.getUsuarioById(abrirRuta.getIdUsuario());
        rutaClient.getRutaById(abrirRuta.getIdRuta());
        estadoClient.getEstadosById(abrirRuta.getIdEstado());


        return abrirRutaRepository.save(abrirRuta);

    }

    // si dejamos abrirRuta como null, el service la completa con now()

    // Iniciar ruta es un valor por defecto
    public AbrirRuta marcarInicio(Integer id) {
        AbrirRuta abrirRuta = findById(id);
        abrirRuta.setFInicio(java.time.LocalDateTime.now());
        return abrirRutaRepository.save(abrirRuta); // UPDATE
    }
    // a futuro se tiene que corregir
    // puesto que es un valor que depende de lo que el usuario ingrese en el aplicativo movil
    public AbrirRuta marcarFin(Integer id) {
        AbrirRuta abrirRuta = findById(id);
        abrirRuta.setFFinal(java.time.LocalDateTime.now());
        return abrirRutaRepository.save(abrirRuta); // UPDATE
    }
}

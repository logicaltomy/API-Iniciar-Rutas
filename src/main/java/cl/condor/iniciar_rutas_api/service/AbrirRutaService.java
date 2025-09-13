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
import java.util.Map;

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


    // AbrirRuta depende de muchas tablas para crearla
    // Un total de 3 tablas/clases instanciadas y mapeadas
    public AbrirRuta save(AbrirRuta abrirRuta) {
        // mapeando los string para traer sus id a partir de web client
        // dichos campos a rellenar ya se declararon previamente en el model
        Map<String, Object> estado = estadoClient.getEstadosById(abrirRuta.getIdEstado());
        Map<String, Object> ruta = rutaClient.getRutaById(abrirRuta.getIdRuta());
        Map<String, Object> usuario = usuarioClient.getUsuarioById(abrirRuta.getIdUsuario());
        if (estado == null || estado.isEmpty()) {
            throw new RuntimeException("Estado no encontrado, no se puede guardar la apertura de la ruta");
        }
        if (ruta == null || ruta.isEmpty()) {
            throw new RuntimeException("Ruta no encontrada, no se puede guardar la apertura de la ruta");
        }
        if (usuario == null || usuario.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado,no se puede guardar la apertura de la ruta");
        }

        return abrirRutaRepository.save(abrirRuta);
    }
}

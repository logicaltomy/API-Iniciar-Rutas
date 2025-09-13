package cl.condor.iniciar_rutas_api.service;

import cl.condor.iniciar_rutas_api.model.AbrirRuta;
import cl.condor.iniciar_rutas_api.repository.AbrirRutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AbrirRutaService {

    @Autowired
    private AbrirRutaRepository abrirRutaRepository;

    public List<AbrirRuta> findAll() {
        return abrirRutaRepository.findAll();
    }

    public AbrirRuta findById(Integer id) {
        return abrirRutaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AbrirRuta no encontrada"));
    }

    public AbrirRuta save(AbrirRuta abrirRuta) {
        if (abrirRuta.getIdUsuario() == null || !abrirRutaRepository.existsById(abrirRuta.getIdUsuario())) {
            throw new RuntimeException("La apertura no es realizable puesto que no está vinculada a un id de usuario válido");
        }
        if (abrirRuta.getIdRuta() == null || !abrirRutaRepository.existsById(abrirRuta.getIdRuta())) {
            throw new RuntimeException("La apertura no es realizable puesto que no está vinculada a una id de ruta válida");
        }
        if (abrirRuta.getIdEstado() == null || !abrirRutaRepository.existsById(abrirRuta.getIdEstado())) {
            throw new RuntimeException("La apertura no es realizable puesto que no está vinculada a un id de estado válido");
        }
        return abrirRutaRepository.save(abrirRuta);
    }
}

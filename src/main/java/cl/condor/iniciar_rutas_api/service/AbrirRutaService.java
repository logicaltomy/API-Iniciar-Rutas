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
        return abrirRutaRepository.save(abrirRuta);
    }
}

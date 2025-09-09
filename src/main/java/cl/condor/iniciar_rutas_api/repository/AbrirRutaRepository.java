package cl.condor.iniciar_rutas_api.repository;
import cl.condor.iniciar_rutas_api.model.AbrirRuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbrirRutaRepository extends JpaRepository<AbrirRuta, Integer> {
}

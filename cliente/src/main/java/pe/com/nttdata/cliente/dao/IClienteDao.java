package pe.com.nttdata.cliente.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.com.nttdata.cliente.model.Cliente;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IClienteDao extends JpaRepository<Cliente,Integer> {
    @Query("select c from Cliente c where c.nombre = :nombre")
    List<Cliente> findByNombre(@Param("nombre") String nombre);
    //los querys no son requeridos cuando se usa @Entity
    List<Cliente> findByApellidoPaterno(String apellidoPaterno);
    @Query("select c from Cliente c where c.apellidoMaterno = :apellidoMaterno")
    List<Cliente> findByApellidoMaterno(String apellidoMaterno);
    @Query("select c from Cliente c where c.fechaNacimiento = :fechaNacimiento")
    List<Cliente> findByFechaNacimiento(LocalDate fechaNacimiento);

}

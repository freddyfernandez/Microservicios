package pe.com.nttdata.cliente.service.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.nttdata.cliente.controller.NotificacionRequest;
import pe.com.nttdata.cliente.dao.IClienteDao;
import pe.com.nttdata.cliente.model.Cliente;
import pe.com.nttdata.cliente.service.IClienteService;
import pe.com.nttdata.clientefeign.validar.cliente.ClienteCheckClient;
import pe.com.nttdata.clientefeign.validar.cliente.ClienteCheckResponse;
import pe.com.nttdata.clientequeues.rabbitmq.RabbitMQMessageProducer;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ClienteServiceImpl implements IClienteService {
    private final IClienteDao clienteDao;
    //private final RestTemplate restTemplate;
    private final ClienteCheckClient clienteCheckClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    public List<Cliente> listarClientes() {
        return clienteDao.findAll();
    }

    public Cliente registrarCliente(Cliente cliente) {
        //Se instancia directamente al model sin pasar por el record
        /*Cliente cliente = Cliente.builder()
                .nombre(clienteRequest.nombre())
                .apellidoPaterno(clienteRequest.apellidoPaterno())
                .apellidoMaterno(clienteRequest.apellidoMaterno())
                .email(clienteRequest.email())
                .fechaNacimiento(clienteRequest.fechaNacimiento())
                .build();*/
        Cliente clienteResponse = clienteDao.save(cliente);

        /*ClienteCheckResponse clienteCheckResponse = restTemplate.getForObject(
                //"http://localhost:8081/api/v1/cliente-check/{clienteId}",
                "http://VALIDARCLIENTE/api/v1/cliente-check/{clienteId}",
                ClienteCheckResponse.class,
                clienteResponse.getId()
        );*/
        return clienteResponse;
    }
    @CircuitBreaker(name="validarclienteCB", fallbackMethod = "fallValidarclienteCB")
    public String validarCliente(Cliente cliente) {

        ClienteCheckResponse clienteCheckResponse = clienteCheckClient.validarCliente(cliente.getId());

        if (clienteCheckResponse.esEstafador()) {
            throw new IllegalStateException("Cliente es un estafador!!");
        }
        return "OK";
    }

    public String fallValidarclienteCB(Cliente cliente,Exception e){
        return "NO_OK";
    }
    public void notificarCliente(Cliente cliente) {
        NotificacionRequest notificacionRequest = new NotificacionRequest(
                cliente.getId(),
                cliente.getEmail(),
                String.format("Hola %s, bienvenidos a NTTData...",
                        cliente.getNombre())
        );
        rabbitMQMessageProducer.publish(
                notificacionRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }

    public Cliente modificarCliente(Cliente cliente) {
        /*
        Cliente cliente = Cliente.builder()
                .id(clienteRequest.id())
                .nombre(clienteRequest.nombre())
                .apellidoPaterno(clienteRequest.apellidoPaterno())
                .apellidoMaterno(clienteRequest.apellidoMaterno())
                .email(clienteRequest.email())
                .fechaNacimiento(clienteRequest.fechaNacimiento())
                .build();*/
        return clienteDao.save(cliente);
    }

    public int eliminarCliente(Integer id) {
        clienteDao.deleteById(id);
        return 0;
    }

    public Cliente obtenerCliente(Integer id) {
        return clienteDao.findById(id).get();
    }

    public List<Cliente> listarClientesPorNombre(String nombre) {
        List<Cliente> ClientesXnombreResponse = clienteDao.findByNombre(nombre);
        NotificacionRequest notificacionRequest = new NotificacionRequest(
                ClientesXnombreResponse.get(0).getId(),
                ClientesXnombreResponse.get(0).getEmail(),
                String.format("Estimado %s, tiene una tarjeta preaprobada",
                        ClientesXnombreResponse.get(0).getNombre())
        );
        rabbitMQMessageProducer.publish(
                notificacionRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
        return ClientesXnombreResponse;
    }

    public List<Cliente> listarClientesPorApellidoPaterno(String apellidoPaterno) {
        return clienteDao.findByApellidoPaterno(apellidoPaterno);
    }

    public List<Cliente> listarClientesPorApellidoMaterno(String apellidoMaterno) {
        return clienteDao.findByApellidoMaterno(apellidoMaterno);
    }

   public List<Cliente> listarClientesPorFechaNacimiento(LocalDate fechaNacimiento) {
        return clienteDao.findByFechaNacimiento(fechaNacimiento);
    }
}

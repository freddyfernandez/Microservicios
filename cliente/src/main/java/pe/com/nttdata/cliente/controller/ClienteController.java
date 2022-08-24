package pe.com.nttdata.cliente.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.nttdata.cliente.service.IClienteService;
import pe.com.nttdata.cliente.model.Cliente;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/cliente")
@AllArgsConstructor
public class ClienteController {

    private final IClienteService clienteService;

    @GetMapping
    public ResponseEntity<?> listarClientes() {
        List<Cliente> clientes = clienteService.listarClientes();
        log.info("listar clientes");
        return new ResponseEntity<>(clientes, clientes.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> obtenerCliente(@PathVariable("id") Integer id) {
        log.info("obtener cliente: ", id);
        return new ResponseEntity<>(clienteService.obtenerCliente(id), HttpStatus.OK);
    }
    //Sin validacion
    /*@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrarCliente(@RequestBody ClienteRequest clienteRequest) {
        log.info("nuevo registro de cliente {}", clienteRequest);
        Cliente cliente = clienteService.registrarCliente(clienteRequest);
        return new ResponseEntity<ClienteRequest>(new ClienteRequest(cliente.getId(), clienteRequest.nombre(), clienteRequest.apellidoPaterno(), clienteRequest.apellidoMaterno() , clienteRequest.email(), clienteRequest.fechaNacimiento()), HttpStatus.OK);
    }*/
    //Con validacion
    //@valid captura los errores antes de enviarlo al base de datos
    @PostMapping
    public ResponseEntity<?> registrarCliente(@Valid @RequestBody Cliente cliente) {
        log.info("nuevo registro de cliente {}", cliente);
        Cliente newCliente = clienteService.registrarCliente(cliente);
        String validarResultado=clienteService.validarCliente(newCliente);
        if(validarResultado.equals("OK"))
        {
            clienteService.notificarCliente(newCliente);
            return new ResponseEntity<ClienteRequest>(new ClienteRequest(newCliente.getId(), cliente.getNombre(), cliente.getApellidoPaterno(), cliente.getApellidoMaterno() , cliente.getEmail(), cliente.getFechaNacimiento()), HttpStatus.OK);

        }
        return new ResponseEntity("Servicio validarCliente no disponible",HttpStatus.OK);
    }

    /*@PutMapping
    public ResponseEntity<?> modificarCliente(@RequestBody ClienteRequest clienteRequest) {
        log.info("modificar datos de cliente {}", clienteRequest);
        clienteService.modificarCliente(clienteRequest);
        return new ResponseEntity<ClienteRequest>(clienteRequest, HttpStatus.OK);
    }*/

    @PutMapping
    public ResponseEntity<?> modificarCliente(@Valid @RequestBody Cliente cliente) {
        log.info("modificar datos de cliente {}", cliente);
        clienteService.modificarCliente(cliente);
        return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public void eliminarCliente(@PathVariable("id") Integer id) {
        log.info("eliminar cliente: ", id);
        clienteService.eliminarCliente(id);
    }
    //Response adicionales
    //para diferenciar los @mapping usar @requetParam
    //@GetMapping(value = "/xNombre/{name}")
    @GetMapping(params ="nombre")
    public ResponseEntity<?> obtenerClientexNombre(@RequestParam String nombre) {
        log.info("obtener cliente: ", nombre);
        return new ResponseEntity<>(clienteService.listarClientesPorNombre(nombre), HttpStatus.OK);
    }
    //@GetMapping(value = "/xAPaterno/{apellidoPaterno}")
    @GetMapping(params ="apellidoPaterno")
    public ResponseEntity<?> obtenerClientexAP(@RequestParam String apellidoPaterno) {
        log.info("obtener cliente: ", apellidoPaterno);
        return new ResponseEntity<>(clienteService.listarClientesPorApellidoPaterno(apellidoPaterno), HttpStatus.OK);
    }

    //@GetMapping(value = "/xAMaterno/{apellidoMaterno}")
    @GetMapping(params ="apellidoMaterno")
    public ResponseEntity<?> obtenerClientexAM(@RequestParam String apellidoMaterno) {
        log.info("obtener cliente: ", apellidoMaterno);
        return new ResponseEntity<>(clienteService.listarClientesPorApellidoMaterno(apellidoMaterno), HttpStatus.OK);
    }

    //@GetMapping(value = "/xAMaterno/{apellidoMaterno}")
    @GetMapping(params ="fechaNacimiento")
    public ResponseEntity<?> obtenerClientexFN(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento) {
        log.info("obtener cliente: ", fechaNacimiento);
        return new ResponseEntity<>(clienteService.listarClientesPorFechaNacimiento(fechaNacimiento), HttpStatus.OK);
    }




}

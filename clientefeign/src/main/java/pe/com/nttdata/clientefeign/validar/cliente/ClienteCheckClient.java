package pe.com.nttdata.clientefeign.validar.cliente;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//para el modulo de validarCliente
//@FeignClient("validarcliente")// viene del nombre del service del properties
@FeignClient(
        name = "validarcliente",
        url = "${clientefeign.validarcliente.url}"
)
public interface ClienteCheckClient {
    @GetMapping(path = "api/v1/cliente-check/{clienteId}")//viene del la uri del controller
    ClienteCheckResponse validarCliente(@PathVariable("clienteId") Integer clienteId);// viene del check controller
}
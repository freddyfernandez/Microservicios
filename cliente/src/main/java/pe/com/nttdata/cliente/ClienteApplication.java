package pe.com.nttdata.cliente;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(
        scanBasePackages = {
                "pe.com.nttdata.cliente", //para comunicar con el modulo cliente
                "pe.com.nttdata.clientequeues" // para comunicar con el modulo queues
        }
)
@EnableFeignClients(
        basePackages = "pe.com.nttdata.clientefeign" //para comunicar con el modulo clientefeign del paquete clientefeign

)
@PropertySources({
        @PropertySource("classpath:clientefeign-${spring.profiles.active}.properties")
})
public class ClienteApplication {

    public static void main(String[] args){
        SpringApplication.run(ClienteApplication.class, args);
    }

}

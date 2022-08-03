package pe.com.nttdata.cliente.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cliente {
    @Id
    @SequenceGenerator(
            name = "cliente_id_sequence",
            sequenceName = "cliente_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cliente_id_sequence"
    )
    @Column(name = "cliente_id")
    private Integer id;
    @Column(name = "cliente_nombre")
    @NotEmpty(message = "{NotEmpty.cliente.nombre}")
    @Pattern(regexp="[a-zA-Z]{2,20}",message = "{Pattern.cliente.nombre}")

    private String nombre;
    @Column(name = "cliente_apellidoPaterno")
    @NotEmpty
    @Pattern(regexp="[a-zA-Z]{2,20}")
    private String apellidoPaterno;
    @Column(name = "cliente_apellidoMaterno")
    @NotEmpty
    @Pattern(regexp="[a-zA-Z]{2,20}")
    private String apellidoMaterno;
    @Column(name = "cliente_email")
    @NotEmpty
    @Email
    private String email;
    @Column(name = "cliente_fechaNacimiento")
    //@DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    //@JsonFormat(pattern="yyyy-MM-dd")//Date con milisegundos inespecificos
    private LocalDate fechaNacimiento;// con milisegundos especificos
    @ManyToOne
    @JoinColumn(name="pais_id", nullable = false)
    @NotNull(message = "{NotNull.cliente.pais}")
    private Pais pais;
}

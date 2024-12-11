package pl.infoshare.clinicweb.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String pesel;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean verified = false;
    private boolean formFilled = false;


}

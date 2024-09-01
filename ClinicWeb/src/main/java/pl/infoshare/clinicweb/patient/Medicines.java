package pl.infoshare.clinicweb.patient;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Medicines {

    private String medicineName;
    private int numberOfMedicine;
    private String diagnosis;


}

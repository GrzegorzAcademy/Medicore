package pl.infoshare.clinicweb.doctor;


public enum Specialization {

    NOT_CHOSEN("Nie wybrano"),
    INTERNIST("Internista"),
    CHIRURGY("Chirurg"),
    DERMATOLOGY("Dermatolog"),
    NEUROLOGY("Neurolog"),
    ONCOLOGY("Onkolog");


    final String description;

    public String getDescription() {
        return description;
    }

    Specialization(String description) {
        this.description = description;
    }

}

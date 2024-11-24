package pl.infoshare.clinicweb.user.entity;

public enum Role {
    PATIENT("Pacjent"),
    DOCTOR("Lekarz"),
    ADMIN("Admin"),
    REGISTRATION_IN_PROGRESS("Rejestracja w toku");

    private final String roleDescription;

    Role(String roleDescription) {

        this.roleDescription = roleDescription;

    }
    public String getRoleDescription() {

        return roleDescription;
    }


}

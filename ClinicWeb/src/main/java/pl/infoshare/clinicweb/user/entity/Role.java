package pl.infoshare.clinicweb.user.entity;

public enum Role {
    PATIENT("Pacjent"),
    DOCTOR("Lekarz"),
    ADMIN("Admin");

    private final String roleDescription;

    Role(String roleDescription) {

        this.roleDescription = roleDescription;

    }
    public String getRoleDescription() {

        return roleDescription;
    }


}

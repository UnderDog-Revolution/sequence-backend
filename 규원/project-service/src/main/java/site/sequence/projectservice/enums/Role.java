package site.sequence.projectservice.enums;

public enum Role {
    UX_UI_DESIGN,
    BX_DESIGN,
    FRONT_END,
    BACK_END,
    PM;

    public static Role stringToRole(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No enum constant for role: " + role, e);
        }
    }
}

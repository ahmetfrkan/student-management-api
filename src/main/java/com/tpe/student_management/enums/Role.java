package com.tpe.student_management.enums;

public enum Role {
    ADMIN("Admin"),
    MANAGER("Dean"),
    ASSISTANT_MANAGER("ViceDean"),
    TEACHER("Teacher"),
    STUDENT("Student");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}

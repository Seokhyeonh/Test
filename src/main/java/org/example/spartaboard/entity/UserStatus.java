package org.example.spartaboard.entity;

public enum UserStatus {

    USER(Authority.USER), // 사용자 권한
    ADMIN(Authority.ADMIN), // 관리자 권한
    ACTIVE(Authority.ACTIVE), //사용자 활성(로그인 가능)
    INACTIVE(Authority.INACTIVE); //사용자 비활성(탈퇴, 로그인 불가능)

    private final String authority;

    UserStatus(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String ACTIVE = "회원";
        public static final String INACTIVE = "탈퇴";
    }

}

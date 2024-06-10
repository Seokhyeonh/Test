package org.example.spartaboard.Security;

import lombok.RequiredArgsConstructor;
import org.example.spartaboard.entity.User;
import org.example.spartaboard.entity.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final User user;
    public User getUser() {
        return user;
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override//사용자의 상태에 따라 접근할 수 있는지 결정
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserStatus status = user.getStatus();;
        String authority = status.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();//한 사용자가 여러 권한을 가질 수 있으므로 배열타입
        authorities.add(simpleGrantedAuthority);
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


//    /**
//     * 계정 비활성화.
//     *
//     * @return 사용 여부
//     * @apiNote 사용할 경우 true를 리턴하도록 재정의.
//     */
    @Override
    public boolean isEnabled() {
        return true;
    } // 이 계정을 사용 할 것이냐
    // false 회원가입을 해도 안먹힘
}
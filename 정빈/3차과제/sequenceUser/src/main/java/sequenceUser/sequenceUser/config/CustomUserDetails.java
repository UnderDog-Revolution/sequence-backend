package sequenceUser.sequenceUser.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final String userId;

    public CustomUserDetails(String userId) {
        this.userId = userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 권한 리스트 반환 (필요 시 수정)
    }

    @Override
    public String getPassword() {
        return null; // 비밀번호는 JWT 인증에서 필요하지 않음
    }

    @Override
    public String getUsername() {
        return userId; // 사용자 ID 반환
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

    @Override
    public boolean isEnabled() {
        return true;
    }
}

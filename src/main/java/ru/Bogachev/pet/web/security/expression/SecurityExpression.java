package ru.Bogachev.pet.web.security.expression;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.Bogachev.pet.domain.user.Role;

@Service("securityExpression")
@RequiredArgsConstructor
public class SecurityExpression {

    public boolean canAccessUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        return hasAnyRole(authentication, Role.ROLE_ADMIN);
    }

    private boolean hasAnyRole(
            final Authentication authentication,
            final Role... roles
    ) {
        for (Role role : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                    role.name()
            );
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }

}

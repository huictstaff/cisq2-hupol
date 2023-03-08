package nl.hu.cisq2.hupol.security.domain;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record UserProfile(String username, Collection<? extends GrantedAuthority> authorities) {
}

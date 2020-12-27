package com.unipi.giguniverse.security;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.unipi.giguniverse.security.ApplicationUserPermission.VENUE_READ;
import static com.unipi.giguniverse.security.ApplicationUserPermission.VENUE_WRITE;

@AllArgsConstructor
public enum ApplicationUserRole{
    ATTENDANT(Sets.newHashSet(VENUE_READ)),
    OWNER(Sets.newHashSet(VENUE_READ, VENUE_WRITE)),
    ADMIN(Sets.newHashSet());

    private final Set<ApplicationUserPermission> permissions;

    public Set<ApplicationUserPermission> getPermissions(){
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return permissions;
    }

}

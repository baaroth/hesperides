package org.hesperides.core.domain.security.entities;


import lombok.AllArgsConstructor;
import lombok.Value;
import org.hesperides.core.domain.security.entities.springauthorities.ApplicationProdRole;
import org.hesperides.core.domain.security.entities.springauthorities.DirectoryGroupDN;
import org.hesperides.core.domain.security.entities.springauthorities.GlobalRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * représente un utilisateur d'hesperide.
 */
@Value
@AllArgsConstructor
public class User {

    String name;
    boolean isGlobalProd;
    boolean isGlobalTech;
    List<String> roles;
    List<String> directoryGroupDNs;

    public User(Authentication authentication) {
        this(authentication.getName(), authentication.getAuthorities());
    }

    public User(String username, Collection<? extends GrantedAuthority> springAuthorities) {
        this.name = username;
        this.isGlobalProd = isGlobalProd(springAuthorities);
        this.isGlobalTech = isGlobalTech(springAuthorities);
        this.roles = getRoles(springAuthorities);
        this.directoryGroupDNs = getDirectoryGroupDNs(springAuthorities);
    }

    private static boolean isGlobalProd(Collection<? extends GrantedAuthority> springAuthorities) {
        return hasGlobalRole(springAuthorities, GlobalRole.IS_PROD);
    }

    private static boolean isGlobalTech(Collection<? extends GrantedAuthority> springAuthorities) {
        return hasGlobalRole(springAuthorities, GlobalRole.IS_TECH);
    }

    private static boolean hasGlobalRole(Collection<? extends GrantedAuthority> springAuthorities, String userRole) {
        boolean hasGlobalRole = false;
        if (springAuthorities != null && userRole != null) {
            for (GrantedAuthority authority : springAuthorities) {
                if (userRole.equalsIgnoreCase(authority.getAuthority())) {
                    hasGlobalRole = true;
                    break;
                }
            }
        }
        return hasGlobalRole;
    }

    private static List<String> getRoles(Collection<? extends GrantedAuthority> springAuthorities) {
        return springAuthorities.stream()
                .filter(springAuthority -> springAuthority instanceof GlobalRole || springAuthority instanceof ApplicationProdRole)
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private static List<String> getDirectoryGroupDNs(Collection<? extends GrantedAuthority> springAuthorities) {
        return springAuthorities.stream()
                .filter(springAuthority -> springAuthority instanceof DirectoryGroupDN)
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public boolean hasAtLeastOneDirectoryGroup(Map<String, List<String>> directoryGroups) {
        boolean hasDirectoryGroup = false;
        if (!CollectionUtils.isEmpty(directoryGroups)) {
            List<String> flatDirectoryGroups = directoryGroups.values().stream().flatMap(List::stream).collect(Collectors.toList());
            hasDirectoryGroup = !Collections.disjoint(flatDirectoryGroups, this.getDirectoryGroupDNs());
        }
        return hasDirectoryGroup;
    }

    public List<String> getDirectoryGroupCNs() {
        return directoryGroupDNs.stream()
                .map(DirectoryGroupDN::extractCnFromDn)
                .collect(Collectors.toList());
    }
}
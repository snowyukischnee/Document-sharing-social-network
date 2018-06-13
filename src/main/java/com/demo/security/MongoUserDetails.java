package com.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MongoUserDetails implements UserDetails {

    protected String username;
    protected String password;
    protected List<GrantedAuthority> grantedAuthorityList;

    public MongoUserDetails(String username, String password, String[] grantedAuthorityList) {
        this.username = username;
        this.password = password;
        this.grantedAuthorityList = AuthorityUtils.createAuthorityList(grantedAuthorityList);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

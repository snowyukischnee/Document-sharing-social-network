package com.swd.security;

import org.bson.types.ObjectId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    protected ObjectId _id;
    protected String username;
    protected String password;
    protected List<GrantedAuthority> grantedAuthorityList;
    protected boolean enabled;

    public CustomUserDetails(ObjectId _id, String username, String password, String[] grantedAuthorityList, boolean enabled) {
        this._id = _id;
        this.username = username;
        this.password = password;
        this.grantedAuthorityList = AuthorityUtils.createAuthorityList(grantedAuthorityList);
        this.enabled = enabled;
    }

    public ObjectId get_id() { return _id; }

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
        return enabled;
    }
}

package edu.bethlehem.scinexus.SecurityConfig;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsImpl extends UserDetails {
    public Long id = 0L;

    Long getId();

}

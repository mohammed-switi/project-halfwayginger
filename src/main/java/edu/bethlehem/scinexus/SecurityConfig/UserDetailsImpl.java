package edu.bethlehem.scinexus.Config;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

public interface UserDetailsImpl extends UserDetails {
    @Getter
    public Long id = 0L;

}

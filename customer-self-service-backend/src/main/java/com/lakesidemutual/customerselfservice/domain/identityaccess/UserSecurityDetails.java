package com.lakesidemutual.customerselfservice.domain.identityaccess;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * UserSecurityDetails is an adapter class which makes the login credentials of a specific UserLogin
 * available through Spring's UserDetails interface.
 */
public class UserSecurityDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Boolean accountNonExpired = true;
	private Boolean accountNonLocked = true;
	private Collection<? extends GrantedAuthority> authorities;
	private Boolean credentialsNonExpired = true;
	private String email;
	private Boolean enabled = true;
	private Long id;
	private String password;

	public UserSecurityDetails(Long id, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.setId(id);
		this.setUsername(email);
		this.setPassword(password);
		this.setEmail(email);
		this.setAuthorities(authorities);
	}

	@JsonIgnore
	public Boolean getAccountNonExpired() {
		return this.accountNonExpired;
	}

	@JsonIgnore
	public Boolean getAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@JsonIgnore
	public Boolean getCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	public String getEmail() {
		return this.email;
	}

	@JsonIgnore
	public Boolean getEnabled() {
		return this.enabled;
	}

	public Long getId() {
		return this.id;
	}

	@Override
	@JsonIgnore
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.getAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.getAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.getCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return this.getEnabled();
	}

	public void setAccountNonExpired(Boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.password = username;
	}

}

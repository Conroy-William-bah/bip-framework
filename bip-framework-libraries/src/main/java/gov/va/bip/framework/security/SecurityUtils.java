package gov.va.bip.framework.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Contains various reusable utility functions related to security in web app.
 *
 * @author jshrader
 * @author jluck
 * @author akulkarni
 */
public class SecurityUtils {

	/**
	 * protected constructor to prevent instantiation, but allow subclassing.
	 */
	protected SecurityUtils() {
	}

	/**
	 * Returns the user id existing in the ThreadLocal SecurityContextHolder This will therefore obviously only work if
	 * the user has been previously authenticated/setup in spring security.
	 *
	 * @return String the user id
	 */
	public static final String getUserId() {
		if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return SecurityContextHolder.getContext().getAuthentication().getName();
		} else {
			return null;
		}
	}

	/**
	 * Get the PersonTraits user principal from the Spring SecurityContext
	 *
	 * @return user principal
	 */
	public static final PersonTraits getPersonTraits() {
		if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PersonTraits) {
			return (PersonTraits) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} else {
			return null;
		}
	}

	/**
	 * Get the current users's roles from the Spring SecurityContext
	 *
	 * @return roles
	 */
	@SuppressWarnings("unchecked")
	public static final Collection<GrantedAuthority> getAuthorities() {
		if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
			return (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		} else {
			return Collections.unmodifiableList(new ArrayList<GrantedAuthority>());
		}
	}

	/**
	 * Checks if is user in role.
	 *
	 * @param role the role
	 * @return true, if is user in role
	 */
	public static final boolean isUserInRole(final String role) {
		final Collection<? extends GrantedAuthority> authorities = SecurityUtils.getAuthorities();

		if (authorities != null) {
			// loop through the list of granted authorities
			for (final GrantedAuthority ga : authorities) {
				if (ga.toString().equals(role)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Clear the current security context
	 */
	public static final void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		SecurityContextHolder.clearContext();
	}

}

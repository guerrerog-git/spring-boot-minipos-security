package net.tecgurus.minipos.security.response;

import java.io.Serializable;

/**
 * This class is required for creating a response containing the JWT to be returned to the user
 * @author Dell E6530
 *
 */
public class JwtResponse implements Serializable {
	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	private String perfil;

	public JwtResponse(String jwttoken, String perfil) {
		this.jwttoken = jwttoken;
		this.perfil =perfil;
	}

	
	
	public String getPerfil() {
		return perfil;
	}





	public String getToken() {
		return this.jwttoken;
	}
}
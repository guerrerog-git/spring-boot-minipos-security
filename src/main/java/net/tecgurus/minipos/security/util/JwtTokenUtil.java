package net.tecgurus.minipos.security.util;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil {
	
	@Value("${jwt.secret}")
	private String secret;
	
	//Generar el token a traves del usuario y password
	public String generateToken(UserDetails userDetails) {
		//Claims es información adicional que puedes agregar al token
		//Subject al nombre del usuario (email)
		return Jwts.builder().setClaims(new HashMap<>()).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(60).toInstant())) //60 minutos
				.signWith(getSigningKey()).compact();
	}
	
	//Obtiene la clave secreta en bytes desencriptada
	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(this.secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	
	//true si el token es valido y esta vigente.
	public boolean validateToken(String token, UserDetails userDetails) {
		String username = getUsernameFromToken(token);
		boolean exito = false;
		if (username.equals(userDetails.getUsername())) {
			exito = true;
		} else {
			return false;
		}
		
		Date expirationDate = getExpirationDateFromToken(token);
		if (!expirationDate.before(new Date())) {
			exito = true;
		} else {
			return false;
		}
		
		return true;
	}
	
	//Obtiene la fecha de expiracion del token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	//Obtener el nombre de usuario que esta almacenado en el token.
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	//Obtener información adicional (subject, perfil, etc) que esta almacenada en el token
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	//Toda la información que tenga almacenada el token
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJwt(token).getBody();
	}
	
	
	
}












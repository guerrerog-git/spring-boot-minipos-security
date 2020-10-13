package net.tecgurus.minipos.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.tecgurus.minipos.security.request.JwtRequest;
import net.tecgurus.minipos.security.response.JwtResponse;
import net.tecgurus.minipos.security.service.JwtUserDetailsService;
import net.tecgurus.minipos.security.util.JwtTokenUtil;

@RestController
@CrossOrigin(origins = "*")
/**
 * API POST /authenticate mediante JwtAuthenticationController. La API POST obtiene el nombre de usuario y la contraseña en el cuerpo. 
 * Usando Spring Authentication Manager, autenticamos el nombre de usuario y la contraseña. 
 * Si las credenciales son válidas, se crea un token JWT utilizando JWTTokenUtil y se le proporciona al cliente.
 * @author Dell E6530
 *
 */
public class JwtAuthenticationController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private JwtUserDetailsService userDetailsService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(token, userDetails.getAuthorities().iterator().next().toString()));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
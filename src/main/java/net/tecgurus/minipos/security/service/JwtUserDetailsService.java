package net.tecgurus.minipos.security.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.tecgurus.minipos.model.Usuario;
import net.tecgurus.minipos.repository.UsuarioRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		List<GrantedAuthority> authorities
	      = new ArrayList<>();
		
		Usuario usuario = usuarioRepository.findByEmail(username);
		
		if (usuario != null) {
			authorities.add(new SimpleGrantedAuthority(usuario.getPerfil()));
			return new User(usuario.getEmail(), usuario.getPassword(),
					authorities);
		} else {
			throw new UsernameNotFoundException("User not found with email: " + username);
		}
	}
}
package com.escuelaflorece.sistema_gestion.service;

import com.escuelaflorece.sistema_gestion.model.Usuario;
import com.escuelaflorece.sistema_gestion.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        boolean esAdmin = usuario.getRol() != null && usuario.getRol() == 1;

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // Rol
        authorities.add(new SimpleGrantedAuthority(esAdmin ? "ROLE_ADMIN" : "ROLE_DOCENTE"));

        // Permisos según rol (como pide el PDF)
        if (esAdmin) {
            authorities.add(new SimpleGrantedAuthority("USUARIO_LEER"));
            authorities.add(new SimpleGrantedAuthority("USUARIO_CREAR"));
            authorities.add(new SimpleGrantedAuthority("USUARIO_ACTUALIZAR"));
            authorities.add(new SimpleGrantedAuthority("USUARIO_ELIMINAR"));
            authorities.add(new SimpleGrantedAuthority("ESTUDIANTE_LEER"));
            authorities.add(new SimpleGrantedAuthority("ESTUDIANTE_CREAR"));
            authorities.add(new SimpleGrantedAuthority("ESTUDIANTE_ACTUALIZAR"));
            authorities.add(new SimpleGrantedAuthority("ESTUDIANTE_ELIMINAR"));
            authorities.add(new SimpleGrantedAuthority("REPORTE_VER"));
        } else {
            // Docente
            authorities.add(new SimpleGrantedAuthority("ESTUDIANTE_LEER"));
            authorities.add(new SimpleGrantedAuthority("ASISTENCIA_REGISTRAR"));
            authorities.add(new SimpleGrantedAuthority("NOTA_REGISTRAR"));
            authorities.add(new SimpleGrantedAuthority("REPORTE_VER"));
        }

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getContrasena())
                .authorities(authorities)
                .build();
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuario.setEstado("Activo");
        return usuarioRepository.save(usuario);
    }

    public Usuario guardarDesdeVista(Usuario usuario) {
        if (usuario.getId() == null ||
            (usuario.getContrasena() != null && !usuario.getContrasena().startsWith("$2a$"))) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }
        if (usuario.getEstado() == null) usuario.setEstado("Activo");
        return usuarioRepository.save(usuario);
    }
}
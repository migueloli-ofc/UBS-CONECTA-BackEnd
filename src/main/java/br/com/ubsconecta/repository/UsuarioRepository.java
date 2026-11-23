package br.com.ubsconecta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ubsconecta.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	Usuario findByLogin(String Login);
	}
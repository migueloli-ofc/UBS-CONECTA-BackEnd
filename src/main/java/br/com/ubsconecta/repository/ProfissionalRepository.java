package br.com.ubsconecta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ubsconecta.model.Profissional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Integer> {

	Profissional findByIdUsuario(Integer IdUsuario);
}
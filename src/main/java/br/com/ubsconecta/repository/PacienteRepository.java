package br.com.ubsconecta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ubsconecta.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

	Paciente findByIdUsuario(Integer IdUsuario);
}
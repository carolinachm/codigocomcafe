package br.com.codigocomcafe.repository;

import br.com.codigocomcafe.model.CategoriaModel;



import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends CrudRepository<CategoriaModel, Long> {
    Optional<CategoriaModel> findByNome(String nome);

}

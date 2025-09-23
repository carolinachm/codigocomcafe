package br.com.codigocomcafe.repository;

import br.com.codigocomcafe.model.CategoriaModel;
import br.com.codigocomcafe.model.PostModel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostModel, Long> {
    // O nome do método precisa bater com o nome do campo na entidade
    List<PostModel> findByCategoriaModel(CategoriaModel categoriaModel);
}

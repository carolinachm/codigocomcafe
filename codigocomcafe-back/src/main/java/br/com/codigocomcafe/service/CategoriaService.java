package br.com.codigocomcafe.service;

import br.com.codigocomcafe.model.CategoriaModel;
import br.com.codigocomcafe.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public CategoriaModel cadastrarCategoria(CategoriaModel categoria) {
        return categoriaRepository.save(categoria);
    }
    public Iterable<CategoriaModel> buscarTodasCategorias() {
        return categoriaRepository.findAll();
    }
    public CategoriaModel buscarPorId(Long id) {
        return categoriaRepository.findById(id).get();
    }
    public CategoriaModel atualizarCategoria(CategoriaModel categoria) {
        return categoriaRepository.save(categoria);
    }
    public void  excluirCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }

}

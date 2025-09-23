package br.com.codigocomcafe.service;

import br.com.codigocomcafe.model.CategoriaModel;
import br.com.codigocomcafe.model.PostModel;
import br.com.codigocomcafe.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostModel cadastraPost(PostModel postModel) {
        return postRepository.save(postModel);
    }

    public List<PostModel> buscarTodosPosts() {
        return postRepository.findAll();
    }

    public List<PostModel> buscarPorCategoria(CategoriaModel categoria) {
        return postRepository.findByCategoriaModel(categoria);
    }

    public Optional<PostModel> buscarPorId(Long id) {
        return postRepository.findById(id);
    }

    public PostModel atualizarPost(PostModel postModel) {
        if (postModel.getId() == null || !postRepository.existsById(postModel.getId())) {
            throw new IllegalArgumentException("Post não encontrado para atualização");
        }
        return postRepository.save(postModel);
    }

    public void deletarPorId(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post não encontrado para exclusão");
        }
        postRepository.deleteById(id);
    }
}

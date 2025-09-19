package br.com.codigocomcafe.service;

import br.com.codigocomcafe.model.PostModel;
import br.com.codigocomcafe.repository.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public PostModel cadastraPost(PostModel postModel) {
        return postRepository.save(postModel);
    }

    public List<PostModel> buscarTodosPosts() {
        return postRepository.findAll();
    }

    public Optional<PostModel> buscarPorId(Long id) {
        return postRepository.findById(id);
    }

    public PostModel atualizarPost(PostModel postModel) {
        return postRepository.save(postModel);
    }

    public void deletarPorId(Long id) {
        postRepository.deleteById(id);
    }
}

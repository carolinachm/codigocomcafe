package br.com.codigocomcafe.controller;

import br.com.codigocomcafe.model.CategoriaModel;
import br.com.codigocomcafe.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categoria", description = "Endpoints para gerenciamento de categorias")
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // ------------------- CREATE -------------------
    @Operation(summary = "Cadastrar nova categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/categorias")
    public ResponseEntity<CategoriaModel> cadastrarCategoria(@RequestBody CategoriaModel categoriaModel) {
        CategoriaModel novaCategoria = categoriaService.cadastrarCategoria(categoriaModel);
        return new ResponseEntity<>(novaCategoria, HttpStatus.CREATED);
    }

    // ------------------- READ ALL -------------------
    @Operation(summary = "Listar todas as categorias")
    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaModel>> buscarTodasCategorias() {
        Iterable<CategoriaModel> todasCategorias = categoriaService.buscarTodasCategorias();
        return new ResponseEntity<>((List<CategoriaModel>) todasCategorias, HttpStatus.OK);
    }

    // ------------------- READ BY ID -------------------
    @Operation(summary = "Buscar categoria por ID")
    @GetMapping("/categorias/{id}")
    public ResponseEntity<CategoriaModel> buscarPorId(@PathVariable Long id) {
        CategoriaModel categoria = categoriaService.buscarPorId(id);
        if (categoria != null) {
            return new ResponseEntity<>(categoria, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ------------------- UPDATE -------------------
    @Operation(summary = "Atualizar categoria existente")
    @PutMapping("/categorias/{id}")
    public ResponseEntity<CategoriaModel> atualizarCategoria(
            @PathVariable Long id,
            @RequestBody CategoriaModel categoriaModel) {

        categoriaModel.setId(id); // garante que estamos atualizando o ID correto
        CategoriaModel atualizarCategoria = categoriaService.atualizarCategoria(categoriaModel);
        return new ResponseEntity<>(atualizarCategoria, HttpStatus.OK);
    }

    // ------------------- DELETE -------------------
    @Operation(summary = "Deletar categoria por ID")
    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long id) {
        categoriaService.excluirCategoria(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

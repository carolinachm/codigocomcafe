import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { CategoriaService } from '../../service/categoria-service';
import { CategoriaModel } from '../../model/CategoriaModel';

@Component({
  selector: 'app-categoria',
  templateUrl: './categoria.html',
  styleUrls: ['./categoria.css'],
  imports: [ReactiveFormsModule]  // <- era "styleUrl"
})
export class Categoria implements OnInit {

  btnCadastrar: boolean = true;


  // ---------------------------
  // Formulário reativo tipado
  // ---------------------------
  categoria = new FormGroup({
    id: new FormControl<number | null>(null),
    nome: new FormControl<string | null>(null, [Validators.required, Validators.minLength(3)]),
    descricao: new FormControl<string | null>(null, [Validators.required, Validators.minLength(3)]),
    tipoCategoria: new FormControl<string | null>('', [Validators.required, Validators.minLength(3)])
  });

  // Vetor para armazenar categorias
  vetor: CategoriaModel[] = [];

  // 🔹 Lista de tipos de categoria (para o select)
  // ? dúvida - pode fazer isso?
  tiposCategoria = [
    { id: 'TUTORIAL', nome: 'Tutorial' },
    { id: 'DICAS', nome: 'Dicas' },
    { id: 'BLOG', nome: 'Blog' },
    { id: 'NOTICIAS', nome: 'Notícias' },
    { id: 'LANÇAMENTOS', nome: 'Lançamentos' },
    { id: 'REVIEWS', nome: 'Reviews' },
    { id: 'COMPARATIVOS', nome: 'Comparativos' },
    { id: 'OPINIAO', nome: 'Opinião' },
    { id: 'CARREIRA', nome: 'Carreira' },
    { id: 'EVENTOS', nome: 'Eventos' },
    { id: 'SEGURANCA', nome: 'Segurança' },
    { id: 'INTELIGENCIA_ARTIFICIAL', nome: 'Inteligência Artificial' },
    { id: 'CLOUD_COMPUTING', nome: 'Cloud Computing' },
    { id: 'PROGRAMACAO', nome: 'Programação' },
    { id: 'BANCO_DE_DADOS', nome: 'Banco de Dados' },
    { id: 'FRONTEND', nome: 'Frontend' },
    { id: 'BACKEND', nome: 'Backend' },
    { id: 'DEVOPS', nome: 'DevOps' },
    { id: 'MOBILE', nome: 'Mobile' },
    { id: 'GADGETS', nome: 'Gadgets' }
  ];



  // Índice da categoria selecionada
  indiceCategoriaSelecionada: number = -1;

  constructor(private service: CategoriaService) { }

  ngOnInit(): void {
    this.listar();
  }

  // ---------------------------
  // Listar categorias
  // ---------------------------
  listar(): void {
    this.service.listarTodos().subscribe(categorias => {
      console.log('Categorias recebidas do backend:', categorias);
      this.vetor = categorias;
    });
  }

  // ---------------------------
  // Cadastrar nova categoria
  // ---------------------------
  cadastrar(): void {
    const novaCategoria: CategoriaModel = this.categoria.value as CategoriaModel;
    
    delete novaCategoria.id;

    this.service.cadastrar(novaCategoria).subscribe(res => {
      this.vetor.push(res);
      this.categoria.reset();
    })

  }

  // ---------------------------
  // Selecionar categoria para edição
  // ---------------------------
  selecionar(indice: number): void {
  this.indiceCategoriaSelecionada = indice;
  const id = this.vetor[indice].id;

  if (id != null) {
    this.service.buscarPorId(id).subscribe(res => {
      this.categoria.setValue({
        id: res.id ?? null,
        nome: res.nome ?? null,
        descricao: res.descricao ?? null,
        tipoCategoria: res.tipoCategoria ?? ''
      });

      this.btnCadastrar = false;
    });
  }
}


  // ---------------------------
  // Cancelar ações
  // ---------------------------
  cancelar(): void {
    console.log('Cancelando edição, resetando formulário...');
    this.categoria.reset();
    this.indiceCategoriaSelecionada = -1;
    this.btnCadastrar = true;
  }

  // ---------------------------
  // Alterar categoria
  // ---------------------------
alterar(): void {
  if (this.indiceCategoriaSelecionada >= 0) {
    const categoriaAtualizada: CategoriaModel = this.categoria.value as CategoriaModel;

    if (categoriaAtualizada.id != null) {
      this.service.atualiza(categoriaAtualizada).subscribe(res => {
        console.log('[COMPONENT] Categoria atualizada com sucesso:', res);
        this.vetor[this.indiceCategoriaSelecionada] = res; // substitui pelo retorno do backend
        this.cancelar();
      });
    }
  }
}

  // ---------------------------
  // Remover categoria
  // ---------------------------
remover(): void {
  if (this.indiceCategoriaSelecionada >= 0) {
    const id = this.vetor[this.indiceCategoriaSelecionada].id;

    if (id != null) {
      this.service.remove(id).subscribe(() => {
        console.log('[COMPONENT] Categoria removida com sucesso');
        this.vetor.splice(this.indiceCategoriaSelecionada, 1); // tira do vetor local também
        this.cancelar();
      });
    }
  }
}

}

import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { PostService } from '../../service/post-service';
import { PostModel } from '../../model/PostModel';

@Component({
  selector: 'app-post',
  templateUrl: './post.html',
  styleUrls: ['./post.css'],
  imports: [ReactiveFormsModule]
})
export class Post implements OnInit {

  btnCadastrar: boolean = true;

  // ---------------------------
  // Formulário reativo tipado
  // ---------------------------
  post = new FormGroup({
    id: new FormControl<number | null>(null),
    titulo: new FormControl<string | null>(null, [Validators.required, Validators.minLength(3)]),
    conteudo: new FormControl<string | null>(null, [Validators.required, Validators.minLength(3)]),
    imagem: new FormControl<string | null>(null),  // Base64
    video: new FormControl<string | null>(null),   // Base64
    status: new FormControl<string | null>('', [Validators.required]),
    dataCriacao: new FormControl<string | null>(null),    // ISO 8601 ou input type="date"
    dataAtualizacao: new FormControl<string | null>(null),// ISO 8601 ou input type="date"
    dataPublicacao: new FormControl<string | null>(null), // ISO 8601 ou input type="date"
    categoria: new FormControl<number | null>(null)       // Id da categoria selecionada
  });


  // Vetor para armazenar posts
  vetor: PostModel[] = [];

  // 🔹 Lista de status do post (para o select)
  statusPost = [
    { id: 'RASCUNHO', nome: 'Rascunho' },
    { id: 'PUBLICADO', nome: 'Publicado' },
    { id: 'EXCLUIDO', nome: 'Excluído' }
  ];

  categorias = [
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


  // Índice do post selecionado
  indicePostSelecionado: number = -1;

  constructor(private service: PostService) { }

  ngOnInit(): void {
    this.listar();
  }

  // ---------------------------
  // Listar posts
  // ---------------------------
  listar(): void {
    this.service.listarTodos().subscribe(posts => {
      console.log('Posts recebidos do backend:', posts);
      this.vetor = posts;
    });
  }

    // Lê o arquivo de imagem e converte para Base64
  onImagemChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.post.patchValue({ imagem: reader.result as string });
      };
      reader.readAsDataURL(file);
    }
  }

  // Lê o arquivo de vídeo e converte para Base64
  onVideoChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.post.patchValue({ video: reader.result as string });
      };
      reader.readAsDataURL(file);
    }
  }


  // ---------------------------
  // Cadastrar novo post
  // ---------------------------
  cadastrar(): void {
    const novoPost: PostModel = this.post.value as PostModel;

    delete novoPost.id;

    this.service.cadastrar(novoPost).subscribe(res => {
      this.vetor.push(res);
      this.post.reset();
    });
  }

  // ---------------------------
  // Selecionar post para edição
  // ---------------------------
  selecionar(indice: number): void {
    this.indicePostSelecionado = indice;
    const id = this.vetor[indice].id;

    if (id != null) {
      this.service.buscarPorId(id).subscribe(res => {
        this.post.setValue({
          id: res.id ?? null,
          titulo: res.titulo ?? null,
          conteudo: res.conteudo ?? null,
          imagem: res.imagem ?? null,
          video: res.video ?? null,
          status: res.status ?? '',
          dataCriacao: res.dataCriacao ?? null,
          dataAtualizacao: res.dataAtualizacao ?? null,
          dataPublicacao: res.dataPublicacao ?? null,
          categoria: res.categoria?.id ?? null
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
    this.post.reset();
    this.indicePostSelecionado = -1;
    this.btnCadastrar = true;
  }

  // ---------------------------
  // Alterar post
  // ---------------------------
  alterar(): void {
    if (this.indicePostSelecionado >= 0) {
      const postAtualizado: PostModel = this.post.value as PostModel;

      if (postAtualizado.id != null) {
        this.service.atualiza(postAtualizado).subscribe(res => {
          console.log('[COMPONENT] Post atualizado com sucesso:', res);
          this.vetor[this.indicePostSelecionado] = res;
          this.cancelar();
        });
      }
    }
  }

  // ---------------------------
  // Remover post
  // ---------------------------
  remover(): void {
    if (this.indicePostSelecionado >= 0) {
      const id = this.vetor[this.indicePostSelecionado].id;

      if (id != null) {
        this.service.remove(id).subscribe(() => {
          console.log('[COMPONENT] Post removido com sucesso');
          this.vetor.splice(this.indicePostSelecionado, 1);
          this.cancelar();
        });
      }
    }
  }
}

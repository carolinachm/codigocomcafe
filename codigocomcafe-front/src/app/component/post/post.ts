import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse, HttpEventType } from '@angular/common/http';

import { PostService } from '../../service/post-service';
import { UploadService } from '../../service/upload-service';
import { PostModel, Status } from '../../model/PostModel';
import { CategoriaModel } from '../../model/CategoriaModel';

@Component({
  selector: 'app-post',
  templateUrl: './post.html',
  styleUrls: ['./post.css'],
  imports: [ReactiveFormsModule, CommonModule],
  providers: [UploadService]
})
export class Post implements OnInit {

  btnCadastrar: boolean = true;
  indicePostSelecionado: number = -1;

  selectedFileImage: File | undefined;
  selectedFileVideo: File | undefined;
  uploadProgress: number = 0;

  post = new FormGroup({
    id: new FormControl<number | null>(null),
    titulo: new FormControl<string | null>(null, [Validators.required, Validators.minLength(3)]),
    autor: new FormControl<string | null>(null, [Validators.required, Validators.minLength(3)]),

    conteudo: new FormControl<string | null>(null, [Validators.required, Validators.minLength(3)]),
    status: new FormControl<Status | null>(null, [Validators.required]),
    categoria: new FormControl<number | null>(null),
    dataCriacao: new FormControl<Date | null>(null),
    dataAtualizacao: new FormControl<Date | null>(null),
    dataPublicacao: new FormControl<Date | null>(null),
    imagem: new FormControl<File | null>(null),   // controle de imagem
    video: new FormControl<File | null>(null)
  });



  vetor: PostModel[] = [];

  statusPost: { id: Status, nome: string }[] = [
    { id: 'RASCUNHO', nome: 'Rascunho' },
    { id: 'PUBLICADO', nome: 'Publicado' },
    { id: 'EXCLUIDO', nome: 'Excluído' }
  ];

  categorias: { id: number, nome: string }[] = [
    { id: 1, nome: 'Tutorial' },
    { id: 2, nome: 'Dicas' },
    { id: 3, nome: 'Blog' },
    { id: 4, nome: 'Notícias' },
    { id: 5, nome: 'Lançamentos' },
    { id: 6, nome: 'Reviews' },
    { id: 7, nome: 'Comparativos' },
    { id: 8, nome: 'Opinião' },
    { id: 9, nome: 'Carreira' },
    { id: 10, nome: 'Eventos' },
    { id: 11, nome: 'Segurança' },
    { id: 12, nome: 'Inteligência Artificial' },
    { id: 13, nome: 'Cloud Computing' },
    { id: 14, nome: 'Programação' },
    { id: 15, nome: 'Banco de Dados' },
    { id: 16, nome: 'Frontend' },
    { id: 17, nome: 'Backend' },
    { id: 18, nome: 'DevOps' },
    { id: 19, nome: 'Mobile' },
    { id: 20, nome: 'Gadgets' }
  ];

  constructor(private postService: PostService, private uploadService: UploadService) { }

  ngOnInit(): void {
    this.listar();
  }

  // ---------------------------
  // Listagem de posts
  // ---------------------------
  listar(): void {
    this.postService.listarTodos().subscribe({
      next: posts => {
        console.log('Posts recebidos:', posts);
        this.vetor = posts;
      },
      error: err => console.error('Erro ao listar posts:', err)
    });
  }


  // ---------------------------
  // Seleção de arquivos
  // ---------------------------
  onFileSelected(event: Event, tipo: 'imagem' | 'video'): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    if (tipo === 'imagem') this.selectedFileImage = file;
    else this.selectedFileVideo = file;
  }

  // ---------------------------
  // Upload de arquivo
  // ---------------------------
  upload(tipo: 'imagem' | 'video'): void {
    const file = tipo === 'imagem' ? this.selectedFileImage : this.selectedFileVideo;
    if (!file) return;

    this.uploadService.upload(file).subscribe({
      next: event => {
        if (event.type === HttpEventType.UploadProgress && event.total) {
          this.uploadProgress = Math.round((event.loaded / event.total) * 100);
        } else if (event.type === HttpEventType.Response) {
          // Atualiza o FormGroup com o arquivo enviado
          if (tipo === 'imagem') this.post.patchValue({ imagem: file });
          else this.post.patchValue({ video: file });

          console.log(`${tipo} enviada com sucesso!`);
          this.uploadProgress = 0;
        }
      },
      error: (err: HttpErrorResponse) => {
        console.error(`Erro ao enviar ${tipo}:`, err);
        this.uploadProgress = 0;
      }
    });
  }

  // ---------------------------
  // Cadastro de post
  // ---------------------------
  cadastrar(): void {
    if (this.post.invalid) return;

    const formValue = this.post.value;
    const formData = new FormData();

    formData.append('titulo', formValue.titulo ?? '');
    formData.append('conteudo', formValue.conteudo ?? '');
    formData.append('status', formValue.status ?? 'RASCUNHO');
    if (formValue.categoria) formData.append('categoriaId', formValue.categoria.toString());
    if (this.selectedFileImage) formData.append('imagem', this.selectedFileImage, this.selectedFileImage.name);
    if (this.selectedFileVideo) formData.append('video', this.selectedFileVideo, this.selectedFileVideo.name);

    this.postService.cadastrar(formData).subscribe({
      next: res => {
        this.vetor.push(res);
        this.post.reset();
        this.btnCadastrar = true;
        this.selectedFileImage = undefined;
        this.selectedFileVideo = undefined;
        console.log('Post cadastrado com sucesso:', res);
      },
      error: (err: HttpErrorResponse) => console.error('Erro ao cadastrar post:', err)
    });
  }

  // ---------------------------
  // Selecionar post para edição
  // ---------------------------
  selecionar(indice: number): void {
  this.indicePostSelecionado = indice;
  const postSelecionado = this.vetor[indice];
  if (!postSelecionado.id) return;

  this.postService.buscarPorId(postSelecionado.id).subscribe(res => {
    this.post.setValue({
      id: res.id ?? null,
      titulo: res.titulo ?? null,
      autor: res.autor ?? null,
      conteudo: res.conteudo ?? null,
      status: res.status ?? null,
      categoria: res.categoria?.id ?? null,
      imagem: null,               // controla upload separadamente
      video: null,                // controla upload separadamente
      dataCriacao: res.dataCriacao ? new Date(res.dataCriacao) : null,
      dataAtualizacao: res.dataAtualizacao ? new Date(res.dataAtualizacao) : null,
      dataPublicacao: res.dataPublicacao ? new Date(res.dataPublicacao) : null
    });
    this.btnCadastrar = false;

    // Resetar seleção de arquivos
    this.selectedFileImage = undefined;
    this.selectedFileVideo = undefined;
  });
}


  // ---------------------------
  // Cancelar edição
  // ---------------------------
  cancelar(): void {
    this.post.reset();
    this.indicePostSelecionado = -1;
    this.btnCadastrar = true;
    this.selectedFileImage = undefined;
    this.selectedFileVideo = undefined;
  }

  // ---------------------------
  // Alterar post existente
  // ---------------------------
  alterar(): void {
    if (this.indicePostSelecionado < 0) return;

    const postAtualizado = this.post.value;
    if (!postAtualizado.id) return;

    const formData = new FormData();
    formData.append('titulo', postAtualizado.titulo ?? '');
    formData.append('conteudo', postAtualizado.conteudo ?? '');
    formData.append('status', postAtualizado.status ?? 'RASCUNHO');
    if (postAtualizado.categoria) formData.append('categoriaId', postAtualizado.categoria.toString());
    if (this.selectedFileImage) formData.append('imagem', this.selectedFileImage, this.selectedFileImage.name);
    if (this.selectedFileVideo) formData.append('video', this.selectedFileVideo, this.selectedFileVideo.name);

    this.postService.cadastrar(formData).subscribe({
      next: res => {
        this.vetor[this.indicePostSelecionado] = res;
        this.cancelar();
        console.log('Post atualizado com sucesso:', res);
      },
      error: (err: HttpErrorResponse) => console.error('Erro ao atualizar post:', err)
    });
  }

  // ---------------------------
  // Remover post
  // ---------------------------
  remover(): void {
    if (this.indicePostSelecionado < 0) return;
    const postSelecionado = this.vetor[this.indicePostSelecionado];
    if (!postSelecionado.id) return;

    this.postService.remove(postSelecionado.id).subscribe(() => {
      this.vetor.splice(this.indicePostSelecionado, 1);
      this.cancelar();
      console.log('Post removido com sucesso');
    });
  }
}

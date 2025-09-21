import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { PostModel } from '../model/PostModel';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  // URL base da API para posts
  private apiUrl = 'http://localhost:8080/api/posts';

  constructor(private http: HttpClient) { }

  // ---------------------------
  // CREATE - cadastrar novo post
  // ---------------------------
  cadastrar(post: PostModel): Observable<PostModel> {
    console.log('[SERVICE] Enviando post para cadastro:', post);
    return this.http.post<PostModel>(this.apiUrl, post).pipe(
      tap(res => console.log('[SERVICE] Post cadastrado (resposta):', res))
    );
  }

  // ---------------------------
  // READ - listar todos os posts
  // ---------------------------
  listarTodos(): Observable<PostModel[]> {
    console.log('[SERVICE] Buscando todos os posts...');
    return this.http.get<PostModel[]>(this.apiUrl).pipe(
      tap(res => console.log('[SERVICE] Posts recebidos:', res))
    );
  }

  // ---------------------------
  // READ - buscar post por ID
  // ---------------------------
  buscarPorId(id: number): Observable<PostModel> {
    console.log(`[SERVICE] Buscando post com ID: ${id}`);
    return this.http.get<PostModel>(`${this.apiUrl}/${id}`).pipe(
      tap(res => console.log('[SERVICE] Post recebido:', res))
    );
  }

  // ---------------------------
  // UPDATE - atualizar post existente
  // ---------------------------
  atualiza(post: PostModel): Observable<PostModel> {
    console.log('[SERVICE] Enviando post para atualização:', post);
    return this.http.put<PostModel>(`${this.apiUrl}/${post.id}`, post).pipe(
      tap(res => console.log('[SERVICE] Post atualizado (resposta):', res))
    );
  }

  // ---------------------------
  // DELETE - remover post
  // ---------------------------
  remove(id: number): Observable<void> {
    console.log(`[SERVICE] Removendo post com ID: ${id}`);
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => console.log('[SERVICE] Post removido com sucesso!'))
    );
  }
}

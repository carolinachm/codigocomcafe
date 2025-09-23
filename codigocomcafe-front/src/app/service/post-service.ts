import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { PostModel } from '../model/PostModel';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private apiUrl = 'http://localhost:8080/api/posts';

  constructor(private http: HttpClient) { }

  // ---------------------------
  // CREATE - cadastrar novo post com arquivos
  // ---------------------------
  cadastrar(formData: FormData): Observable<PostModel> {
    console.log('[SERVICE] Enviando FormData para cadastro:', formData);
    return this.http.post<PostModel>(this.apiUrl, formData).pipe(
      tap(res => console.log('[SERVICE] Post cadastrado:', res))
    );
  }

  // ---------------------------
  // READ - listar todos os posts
  // ---------------------------
  listarTodos(): Observable<PostModel[]> {
    return this.http.get<PostModel[]>(this.apiUrl).pipe(
      tap(res => console.log('[SERVICE] Posts recebidos:', res))
    );
  }

  // ---------------------------
  // READ - buscar post por ID
  // ---------------------------
  buscarPorId(id: number): Observable<PostModel> {
    return this.http.get<PostModel>(`${this.apiUrl}/${id}`).pipe(
      tap(res => console.log('[SERVICE] Post recebido:', res))
    );
  }

  // ---------------------------
  // UPDATE - atualizar post com arquivos
  // ---------------------------
  atualizar(id: number, formData: FormData): Observable<PostModel> {
    console.log('[SERVICE] Enviando FormData para atualização:', formData);
    return this.http.put<PostModel>(`${this.apiUrl}/${id}`, formData).pipe(
      tap(res => console.log('[SERVICE] Post atualizado:', res))
    );
  }

  // ---------------------------
  // DELETE - remover post
  // ---------------------------
  remove(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => console.log('[SERVICE] Post removido com sucesso'))
    );
  }
}

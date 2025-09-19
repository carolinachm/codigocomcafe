import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { CategoriaModel } from '../model/CategoriaModel';

@Injectable({
  providedIn: 'root'
})
export class CategoriaService {

  // URL base da API
  private apiUrl = 'http://localhost:8080/api/categorias';

  constructor(private http: HttpClient) {}

  // ---------------------------
  // CREATE - cadastrar nova categoria
  // ---------------------------
  cadastrar(categoria: CategoriaModel): Observable<CategoriaModel> {
    console.log('[SERVICE] Enviando categoria para cadastro:', categoria);
    return this.http.post<CategoriaModel>(this.apiUrl, categoria).pipe(
      tap(res => console.log('[SERVICE] Categoria cadastrada (resposta):', res))
    );
  }

  // ---------------------------
  // READ - listar todas as categorias
  // ---------------------------
  listarTodos(): Observable<CategoriaModel[]> {
    console.log('[SERVICE] Buscando todas as categorias...');
    return this.http.get<CategoriaModel[]>(this.apiUrl).pipe(
      tap(res => console.log('[SERVICE] Categorias recebidas:', res))
    );
  }

  // ---------------------------
  // READ - buscar categoria por ID
  // ---------------------------
  buscarPorId(id: number): Observable<CategoriaModel> {
    console.log(`[SERVICE] Buscando categoria com ID: ${id}`);
    return this.http.get<CategoriaModel>(`${this.apiUrl}/${id}`).pipe(
      tap(res => console.log('[SERVICE] Categoria recebida:', res))
    );
  }

  // ---------------------------
  // UPDATE - atualizar categoria existente
  // ---------------------------
  atualiza(categoria: CategoriaModel): Observable<CategoriaModel> {
    console.log('[SERVICE] Enviando categoria para atualização:', categoria);
    return this.http.put<CategoriaModel>(`${this.apiUrl}/${categoria.id}`, categoria).pipe(
      tap(res => console.log('[SERVICE] Categoria atualizada (resposta):', res))
    );
  }

  // ---------------------------
  // DELETE - remover categoria
  // ---------------------------
  remove(id: number): Observable<void> {
    console.log(`[SERVICE] Removendo categoria com ID: ${id}`);
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => console.log('[SERVICE] Categoria removida com sucesso!'))
    );
  }
}

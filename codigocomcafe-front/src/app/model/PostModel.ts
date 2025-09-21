import { CategoriaModel } from "./CategoriaModel";

export class PostModel {
    id?: number;
    titulo?: string;
    conteudo?: string;
    imagem?: string; // Base64
    video?: string;  // Base64
    status?: 'RASCUNHO' | 'PUBLICADO' | 'EXCLUIDO';
    dataCriacao?: string;    // ISO 8601
    dataAtualizacao?: string; // ISO 8601
    dataPublicacao?: string;  // ISO 8601
    categoria?: CategoriaModel;

}
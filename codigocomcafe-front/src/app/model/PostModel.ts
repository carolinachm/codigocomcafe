import { CategoriaModel } from "./CategoriaModel";

// Enum para status
export type Status = 'RASCUNHO' | 'PUBLICADO' | 'EXCLUIDO';

export class PostModel {
    id?: number;
    titulo?: string;
    autor?: string;
    conteudo?: string;
    imagem?: File | undefined; // agora é File, compatível com upload
    video?: File | undefined;  // agora é File, compatível com upload
    status?: Status;
    dataCriacao?: Date;    // tipo Date em vez de string
    dataAtualizacao?: Date;
    dataPublicacao?: Date;
    categoria?: CategoriaModel;
}

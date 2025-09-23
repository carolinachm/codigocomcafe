import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class UploadService {
  private baseUrl = 'http://localhost:8080/api/post';

  constructor(private http: HttpClient) {}

  upload(file: File): Observable<HttpEvent<any>> {
    const formData = new FormData();
    formData.append('file', file, file.name);
    formData.append('status', 'customer');

    const req = new HttpRequest<any>('POST', this.baseUrl, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req); // <- tipo HttpEvent<any> já está explícito
  }
}

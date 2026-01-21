import { Injectable } from '@angular/core';
import { Receipt } from '../models/receipt-model';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class PageService {
  private readonly apiUrl = '/backend';

  constructor(private http: HttpClient) {}

  getReceipts(): Observable<Receipt[]> {
    return this.http.get<Receipt[]>(`${this.apiUrl}/getReceipts`);
  }

  getReceipt(hivasAzonosito: string): Observable<Receipt> {
    return this.http.get<Receipt>(`${this.apiUrl}/getReceipt/${hivasAzonosito}`);
  }

  createReceipt(data: any): Observable<Receipt> {
    return this.http.post<Receipt>(`${this.apiUrl}/createReceipt`, data);
  }
}

import { Injectable } from '@angular/core';
import { Receipt, ReceiptBasicInfo } from '../models/receipt-model';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class PageService {
  private readonly apiUrl = '/backend';

  constructor(private http: HttpClient) {}

  getReceipts(): Observable<ReceiptBasicInfo[]> {
    return this.http.get<ReceiptBasicInfo[]>(`${this.apiUrl}/getReceipts`);
  }

  getReceipt(callId: string): Observable<Receipt> {
    return this.http.get<Receipt>(`${this.apiUrl}/getReceipt/${callId}`);
  }

  createReceipt(data: any): Observable<Receipt> {
    return this.http.post<Receipt>(`${this.apiUrl}/createReceipt`, data);
  }
}

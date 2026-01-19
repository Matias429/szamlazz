import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import { Receipt } from '../../models/receipt-model';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { PageService } from '../page-service';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-details',
  standalone: true,
  imports: [CommonModule, RouterModule, MatCardModule, MatListModule, MatTableModule, MatIconModule, MatButtonModule, RouterModule],
  templateUrl: './details-page.html',
  styleUrl: './details-page.scss',
})
export class DetailsPage {
  receipt = signal<Receipt | null>(null);
  hivasAzonosito = '';
  detailColumns = ['megnevezes', 'mennyiseg', 'nettoEgysegar', 'afakulcs', 'netto', 'afa', 'brutto'];
  
  paymentColumns = ['fizetoeszkoz', 'osszeg'];

  constructor(
    private route: ActivatedRoute,
    private pageService: PageService
  ) {}

  ngOnInit(): void {
    this.hivasAzonosito = this.route.snapshot.paramMap.get('hivasAzonosito')!;
    
    this.pageService.getReceipt(this.hivasAzonosito).subscribe({
      next: (data) => this.receipt.set(data),
      error: (err) => console.error('Receipt not found:', err)
    });
  }
}

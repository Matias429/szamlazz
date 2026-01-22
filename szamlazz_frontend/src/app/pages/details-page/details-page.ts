import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { Receipt } from '../../models/receipt-model';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { PageService } from '../page-service';

import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-details',
  standalone: true,
  imports: [
    CommonModule, 
    RouterModule, 

    MatCardModule, 
    MatListModule, 
    MatTableModule, 
    MatIconModule, 
    MatButtonModule, 
    RouterModule
  ],
  templateUrl: './details-page.html',
  styleUrl: './details-page.scss',
})
export class DetailsPage {
  receipt = signal<Receipt | null>(null);
  callId = '';
  readonly detailColumns = ['name', 'amount', 'netUnitPrice', 'vatRate', 'net', 'vat', 'gross'];
  readonly paymentColumns = ['paymentMethod', 'amount'];

  constructor(
    private route: ActivatedRoute,
    private pageService: PageService
  ) {}

  ngOnInit(): void {
    this.callId = this.route.snapshot.paramMap.get('callId')!;
    
    this.pageService.getReceipt(this.callId).subscribe({
      next: (data) => this.receipt.set(data),
      error: (err) => console.error('Receipt not found:', err)
    });
  }
}

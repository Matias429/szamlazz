import { Component, OnInit, signal } from '@angular/core';
import { PageService } from '../page-service';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatSortModule } from '@angular/material/sort';
import { Receipt } from '../../models/receipt-model';
import { Router, RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-home-page',
  imports: [
    CommonModule, 
    RouterModule, 
    
    MatTableModule, 
    MatCardModule, 
    MatSortModule, 
    MatIconModule, 
    MatButtonModule
  ],
  standalone: true,
  templateUrl: './home-page.html',
  styleUrl: './home-page.scss',
})
export class HomePage implements OnInit {
  receipts = signal<Receipt[]>([]);
  displayedColumns: string[] = ['receiptNumber', 'receiptDate', 'netAmount', 'grossAmount'];

  constructor(private pageService: PageService, private router: Router) {}

  ngOnInit(): void {
    this.pageService.getReceipts().subscribe({
      next: (data) => {
        this.receipts.set(data);
      },
      error: () => {
        console.error('Failed to fetch receipts');
      }
    });
  }

  goToDetails(callId: string) {
    this.router.navigate(['/details', callId]);
  }
}

import { Component, inject } from '@angular/core';
import { FormBuilder, FormArray, Validators, ReactiveFormsModule, FormGroup} from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatListModule } from '@angular/material/list';
import { CommonModule } from '@angular/common';
import { PageService } from '../page-service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

@Component({
  selector: 'app-create-page',
  standalone: true,
  imports: [
    CommonModule, RouterModule, ReactiveFormsModule,
    MatCardModule, MatButtonModule, MatIconModule,
    MatInputModule, MatSelectModule, MatCheckboxModule,
    MatFormFieldModule, MatListModule, MatAutocompleteModule, MatInputModule
  ],
  templateUrl: './create-page.html',
  styleUrl: './create-page.scss'
})
export class CreatePage {
  constructor(private router: Router, private pageService: PageService) {}

  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);

  form = this.fb.group({
    pdfDownload: [false],
    prefix: ['', Validators.required],
    paymentMethod: ['Készpénz', Validators.required],
    currency: ['HUF', Validators.required],
    note: [''],
    itemList: this.fb.array([], Validators.required),
    paymentList: this.fb.array([])
  });

  paymentMethodOptions = [
    'Átutalás', 'Készpénz', 'Bankkártya', 'Csekk', 'Utánvét', 
    'Ajándékutalvány', 'Barion', 'Barter', 'Csoportos beszedés', 
    'OTP Simple', 'Kompenzáció', 'Kupon', 'PayPal', 'PayU', 
    'SZÉP kártya', 'Utalvány'
  ];
  currencyOptions = ['HUF', 'EUR', 'USD', 'NOK', 'GBP', 'CHF', 'JPY', 'CNY', 'CZK', 'PLN', 'AUD', 'CAD'];
  vatRateOptions = [
    '0', '5', '10', '27', 
    'AAM', 'TAM', 'EU', 'EUK', 
    'MAA', 'F.AFA', 'K.AFA', 'ÁKK', 
    'HO', 'EUE', 'EUFADE', 'EUFAD37', 
    'ATK', 'NAM', 'EAM', 'KBAUK', 'KBAET'
  ];

  get itemList() { return this.form.get('itemList') as FormArray; }
  get paymentList() { return this.form.get('paymentList') as FormArray; }



addItem() {
    const group = this.fb.group({
      name: [, Validators.required],
      amount: [, [Validators.required, Validators.min(0.01)]],
      unitOfMeasure: [, Validators.required],
      netUnitPrice: [, [Validators.required, Validators.min(0)]],
      vatRate: [this.vatRateOptions[3], Validators.required],
      net: [0, {value: 0, disabled: true}],
      vat: [0, {value: 0, disabled: true}],  
      gross: [0, {value: 0, disabled: true}]
    });

  const inputControls = ['amount', 'netUnitPrice', 'vatRate'];
    inputControls.forEach(controlName => {
      group.get(controlName)?.valueChanges.subscribe(() => {
        this.calculateItemTotals(group);
      });
    });

    this.itemList.push(group);
    this.calculateItemTotals(group);
  }

  addPayment() {
    this.paymentList.push(this.fb.group({
      meansOfPayment: ['', Validators.required],
      amount: [, [Validators.required, Validators.min(0)]]
    }));
  }

  removeItem(index: number) {
    this.itemList.removeAt(index);
  }

  removePayment(index: number) {
    this.paymentList.removeAt(index);
  }

  calculateItemTotals(group: FormGroup) {
    const amount = group.get('amount')?.value || 0;
    const netUnitPrice = group.get('netUnitPrice')?.value || 0;
    const vatRateRaw = group.get('vatRate')?.value || '0';
    
    const net = Number((amount * netUnitPrice).toFixed(2));
    
    let vatRate = 0;
    const numericVat = parseFloat(vatRateRaw);
    if (!isNaN(numericVat)) {
      vatRate = numericVat / 100;
    }
    
    const vat = Number((net * vatRate).toFixed(2));
    const gross = Number((net + vat).toFixed(2));
    
    group.patchValue({
      net,
      vat, 
      gross
    });
  }

  get totalGross(): number {
  return this.itemList.controls
    .filter(control => control.get('gross')?.value)
    .reduce((sum, control) => sum + (control.get('gross')?.value || 0), 0);
  }

  get hasItems(): boolean {
    return this.itemList.length > 0;
  }

  get totalPayments(): number {
    return this.paymentList.controls
      .reduce((sum, control) => sum + (control.get('amount')?.value || 0), 0);
  }

  get hasPayments(): boolean {
    return this.paymentList.length > 0;
  }

  get remainingAmount(): number {
    return this.hasItems ? this.totalGross - this.totalPayments : 0;
  }

  get formInvalidDueToPayments(): boolean {
    return this.hasPayments && Math.abs(this.remainingAmount) > 0;
  }

async submit() {
    if (this.form.valid) {
      try {
        const formValue = this.form.value;

        this.itemList.controls.forEach(group => {
          this.calculateItemTotals(group as FormGroup);
        });
        
        const requestData = {
          pdfDownload: formValue.pdfDownload || false,
          prefix: formValue.prefix,
          paymentMethod: formValue.paymentMethod,
          currency: formValue.currency,
          note: formValue.note || null,
          itemList: formValue.itemList || [],
          paymentList: formValue.paymentList?.length ? formValue.paymentList : null
        };


        const newReceipt = await this.pageService.createReceipt(requestData).toPromise();
        
        if (newReceipt?.details?.receiptPdf) {
          await this.downloadBase64Pdf(newReceipt.details.receiptPdf, newReceipt.details.callId);
        }

        this.router.navigate(['/details', newReceipt?.details.callId]);
      } catch (error) {

        let errorMessage = 'Ismeretlen hiba történt';

        if (error instanceof HttpErrorResponse) {
          errorMessage = error.error || error.message || errorMessage;
        } else if (error instanceof Error) {
          errorMessage = error.message;
        }

        this.snackBar.open(
        errorMessage + '\nKérlek ellenőrizd az adatokat és próbáld újra!',
        'OK', 
        {
          duration: 8000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['error-snackbar', 'center-snackbar']
        }
      );
      }
    }
  }

  downloadBase64Pdf(base64String: string, callId: string) {
  return new Promise<void>((resolve) => {
    try {
      const base64Data = base64String.replace(/^data:application\/pdf;base64,/, '');
      const byteCharacters = atob(base64Data);
      const byteNumbers = new Array(byteCharacters.length);
      for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
      }
      const byteArray = new Uint8Array(byteNumbers);
      const blob = new Blob([byteArray], { type: 'application/pdf' });

      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `nyugta_${callId}.pdf`;
      document.body.appendChild(link);
      link.click();

      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
      
      resolve();
    } catch (error) {
      console.error('PDF download failed:', error);
      resolve();
    }
   });
  }
}

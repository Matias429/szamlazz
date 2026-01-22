import { Component, inject } from '@angular/core';
import { FormArray,FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

import { MatSnackBar } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatSelectModule } from '@angular/material/select';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

import { firstValueFrom } from 'rxjs';
import { PageService } from '../page-service';

@Component({
  selector: 'app-create-page',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,

    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatListModule,
    MatAutocompleteModule
  ],
  templateUrl: './create-page.html',
  styleUrl: './create-page.scss'
})
export class CreatePage {

  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);

  constructor(
    private readonly router: Router,
    private readonly pageService: PageService,
  ) {}

  readonly paymentMethodOptions = [
    'Átutalás', 'Készpénz', 'Bankkártya', 'Csekk', 'Utánvét',
    'Ajándékutalvány', 'Barion', 'Barter', 'Csoportos beszedés',
    'OTP Simple', 'Kompenzáció', 'Kupon', 'PayPal', 'PayU',
    'SZÉP kártya', 'Utalvány'
  ];

  readonly currencyOptions = [
    'HUF', 'EUR', 'USD', 'NOK', 'GBP', 'CHF', 'JPY',
    'CNY', 'CZK', 'PLN', 'AUD', 'CAD'
  ];

  readonly vatRateOptions = [
    '0', '5', '10', '27',
    'AAM', 'TAM', 'EU', 'EUK',
    'MAA', 'F.AFA', 'K.AFA', 'ÁKK',
    'HO', 'EUE', 'EUFADE', 'EUFAD37',
    'ATK', 'NAM', 'EAM', 'KBAUK', 'KBAET'
  ];

  readonly form = this.fb.group({
    pdfDownload: [false],
    prefix: ['', Validators.required],
    paymentMethod: ['Készpénz', Validators.required],
    currency: ['HUF', Validators.required],
    note: [''],
    itemList: this.fb.array<FormGroup>([], Validators.required),
    paymentList: this.fb.array<FormGroup>([])
  });

  /* ==============================
   * Getters
   * ============================== */

  get itemList(): FormArray {
    return this.form.get('itemList') as FormArray;
  }

  get paymentList(): FormArray {
    return this.form.get('paymentList') as FormArray;
  }

  get hasItems(): boolean {
    return this.itemList.length > 0;
  }

  get hasPayments(): boolean {
    return this.paymentList.length > 0;
  }

  get totalGross(): number {
    return this.itemList.controls.reduce(
      (sum, group) => sum + (group.get('gross')?.value ?? 0),
      0
    );
  }

  get totalPayments(): number {
    return this.paymentList.controls.reduce(
      (sum, group) => sum + (group.get('amount')?.value ?? 0),
      0
    );
  }

  get remainingAmount(): number {
    return this.hasItems ? this.totalGross - this.totalPayments : 0;
  }

  get formInvalidDueToPayments(): boolean {
    return this.hasPayments && Math.abs(this.remainingAmount) > 0;
  }

  /* ==============================
   * Item handling
   * ============================== */

  addItem(): void {
    const group = this.createItemGroup();

    ['amount', 'netUnitPrice', 'vatRate'].forEach(control =>
      group.get(control)?.valueChanges.subscribe(() =>
        this.calculateItemTotals(group)
      )
    );

    this.itemList.push(group);
    this.calculateItemTotals(group);
  }

  removeItem(index: number): void {
    this.itemList.removeAt(index);
  }

  private createItemGroup(): FormGroup {
    return this.fb.group({
      name: ['', Validators.required],
      amount: [, [Validators.required, Validators.min(1)]],
      unitOfMeasure: ['', Validators.required],
      netUnitPrice: [, [Validators.required, Validators.min(0)]],
      vatRate: [this.vatRateOptions[3], Validators.required],
      net: [{ value: 0, disabled: true }],
      vat: [{ value: 0, disabled: true }],
      gross: [{ value: 0, disabled: true }]
    });
  }

  private calculateItemTotals(group: FormGroup): void {
    const amount = group.get('amount')?.value ?? 0;
    const netUnitPrice = group.get('netUnitPrice')?.value ?? 0;
    const vatRateRaw = group.get('vatRate')?.value ?? '0';

    const net = +(amount * netUnitPrice).toFixed(2);
    const vatRate = this.parseVatRate(vatRateRaw);
    const vat = +(net * vatRate).toFixed(2);
    const gross = +(net + vat).toFixed(2);

    group.patchValue({ net, vat, gross });
  }

  private parseVatRate(value: string): number {
    const numeric = parseFloat(value);
    return isNaN(numeric) ? 0 : numeric / 100;
  }

  /* ==============================
   * Payment handling
   * ============================== */

  addPayment(): void {
    this.paymentList.push(this.createPaymentGroup());
  }

  removePayment(index: number): void {
    this.paymentList.removeAt(index);
  }

  private createPaymentGroup(): FormGroup {
    return this.fb.group({
      meansOfPayment: ['', Validators.required],
      amount: [, [Validators.required, Validators.min(1)]]
    });
  }

  /* ==============================
   * Submit
   * ============================== */

  async submit(): Promise<void> {
    if (this.form.invalid) return;

    try {
      this.itemList.controls.forEach(group =>
        this.calculateItemTotals(group as FormGroup)
      );

      const {
        pdfDownload,
        prefix,
        paymentMethod,
        currency,
        note,
        itemList,
        paymentList
      } = this.form.getRawValue();

      const requestData = {
        pdfDownload: !!pdfDownload,
        prefix,
        paymentMethod,
        currency,
        note: note || null,
        itemList: itemList ?? [],
        paymentList: paymentList?.length ? paymentList : null
      };

      const receipt = await firstValueFrom(
        this.pageService.createReceipt(requestData)
      );

      if (receipt?.details?.receiptPdf) {
        await this.downloadBase64Pdf(
          receipt.details.receiptPdf,
          receipt.details.callId
        );
      }

      this.router.navigate(['/details', receipt?.details.callId]);
    } catch (error) {
      this.handleError(error);
    }
  }

  /* ==============================
   * Utils
   * ============================== */

  private handleError(error: unknown): void {
    let message = 'Ismeretlen hiba történt';

    if (error instanceof HttpErrorResponse) {
      message = error.error || error.message;
    } else if (error instanceof Error) {
      message = error.message;
    }

    this.snackBar.open(
      `${message}\nKérlek ellenőrizd az adatokat és próbáld újra!`,
      'OK',
      {
        duration: 8000,
        horizontalPosition: 'center',
        verticalPosition: 'top',
        panelClass: ['error-snackbar', 'center-snackbar']
      }
    );
  }

  private downloadBase64Pdf(base64: string, callId: string): Promise<void> {
    return new Promise(resolve => {
      try {
        const cleanBase64 = base64.replace(
          /^data:application\/pdf;base64,/,
          ''
        );
        const bytes = Uint8Array.from(atob(cleanBase64), c =>
          c.charCodeAt(0)
        );
        const blob = new Blob([bytes], { type: 'application/pdf' });

        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `nyugta_${callId}.pdf`;

        document.body.appendChild(link);
        link.click();

        document.body.removeChild(link);
        URL.revokeObjectURL(url);
      } catch (e) {
        console.error('PDF download failed', e);
      } finally {
        resolve();
      }
    });
  }
}

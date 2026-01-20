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
    pdfLetoltes: [false],
    elotag: ['', Validators.required],
    fizmod: ['Készpénz', Validators.required],
    penznem: ['HUF', Validators.required],
    megjegyzes: [''],
    tetelek: this.fb.array([], Validators.required),
    kifizetesek: this.fb.array([])
  });

  fizmodOptions = [
    'Átutalás', 'Készpénz', 'Bankkártya', 'Csekk', 'Utánvét', 
    'Ajándékutalvány', 'Barion', 'Barter', 'Csoportos beszedés', 
    'OTP Simple', 'Kompenzáció', 'Kupon', 'PayPal', 'PayU', 
    'SZÉP kártya', 'Utalvány'
  ];
  penznemOptions = ['HUF', 'EUR', 'USD', 'NOK', 'GBP', 'CHF', 'JPY', 'CNY', 'CZK', 'PLN', 'AUD', 'CAD'];
  mennyisegEgysegek = ['db', 'kg', 'óra', 'perc'];
  afakulcsOptions = [
    '0', '5', '10', '27', 
    'AAM', 'TAM', 'EU', 'EUK', 
    'MAA', 'F.AFA', 'K.AFA', 'ÁKK', 
    'HO', 'EUE', 'EUFADE', 'EUFAD37', 
    'ATK', 'NAM', 'EAM', 'KBAUK', 'KBAET'
  ];

  get tetelekArray() { return this.form.get('tetelek') as FormArray; }
  get kifizetesekArray() { return this.form.get('kifizetesek') as FormArray; }



  addTetel() {
    const group = this.fb.group({
      megnevezes: ['', Validators.required],
      mennyiseg: [1, [Validators.required, Validators.min(0.01)]],
      mennyisegiEgyseg: ['db', Validators.required],
      nettoEgysegar: [0, [Validators.required, Validators.min(0)]],
      afakulcs: [this.afakulcsOptions[3], Validators.required],
      netto: [0, {value: 0, disabled: true}],
      afa: [0, {value: 0, disabled: true}],  
      brutto: [0, {value: 0, disabled: true}]
    });

    const inputControls = ['mennyiseg', 'nettoEgysegar', 'afakulcs'];
    inputControls.forEach(controlName => {
      group.get(controlName)?.valueChanges.subscribe(() => {
        this.calculateTetelTotals(group);
      });
    });

    this.tetelekArray.push(group);
    this.calculateTetelTotals(group);
  }

  addKifizetes() {
    this.kifizetesekArray.push(this.fb.group({
      fizetoeszkoz: ['', Validators.required],
      osszeg: [0, [Validators.required, Validators.min(0)]]
    }));
  }

  removeTetel(index: number) {
    this.tetelekArray.removeAt(index);
  }

  removeKifizetes(index: number) {
    this.kifizetesekArray.removeAt(index);
  }

  calculateTetelTotals(group: FormGroup) {
    const mennyiseg = group.get('mennyiseg')?.value || 0;
    const nettoEgysegar = group.get('nettoEgysegar')?.value || 0;
    const afakulcsRaw = group.get('afakulcs')?.value || '0';
    
    const netto = Number((mennyiseg * nettoEgysegar).toFixed(2));
    
    let afaRate = 0;
    const numericVat = parseFloat(afakulcsRaw);
    if (!isNaN(numericVat)) {
      afaRate = numericVat / 100;
    }
    
    const afa = Number((netto * afaRate).toFixed(2));
    const brutto = Number((netto + afa).toFixed(2));
    
    group.patchValue({
      netto,
      afa, 
      brutto
    });
  }


async submit() {
    if (this.form.valid) {
      try {
        const formValue = this.form.value;

        this.tetelekArray.controls.forEach(group => {
          this.calculateTetelTotals(group as FormGroup);
        });
        
        const requestData = {
          pdfLetoltes: formValue.pdfLetoltes || false,
          elotag: formValue.elotag,
          fizmod: formValue.fizmod,
          penznem: formValue.penznem,
          megjegyzes: formValue.megjegyzes || null,
          tetelek: formValue.tetelek || [],
          kifizetesek: formValue.kifizetesek?.length ? formValue.kifizetesek : null
        };


        const newReceipt = await this.pageService.createReceipt(requestData).toPromise();
        
        if (newReceipt?.alap?.nyugtaPdf) {
          await this.downloadBase64Pdf(newReceipt.alap.nyugtaPdf, newReceipt.alap.hivasAzonosito);
        }

        this.router.navigate(['/details', newReceipt?.alap.hivasAzonosito]);
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

  downloadBase64Pdf(base64String: string, hivasAzonosito: string) {
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
      link.download = `nyugta_${hivasAzonosito}.pdf`;
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

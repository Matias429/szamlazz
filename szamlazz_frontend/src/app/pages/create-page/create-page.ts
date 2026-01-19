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

@Component({
  selector: 'app-create-page',
  standalone: true,
  imports: [
    CommonModule, RouterModule, ReactiveFormsModule,
    MatCardModule, MatButtonModule, MatIconModule,
    MatInputModule, MatSelectModule, MatCheckboxModule,
    MatFormFieldModule, MatListModule
  ],
  templateUrl: './create-page.html',
  styleUrl: './create-page.scss'
})
export class CreatePage {
  constructor(private router: Router, private pageService: PageService) {}

  private fb = inject(FormBuilder);

  form = this.fb.group({
    pdfLetoltes: [false],
    elotag: ['MBLZS', Validators.required],
    fizmod: ['készpénz', Validators.required],
    penznem: ['HUF', Validators.required],
    megjegyzes: [''],
    tetelek: this.fb.array([], Validators.required),
    kifizetesek: this.fb.array([])
  });

  fizmodOptions = [
    'átutalás', 'készpénz', 'bankkártya', 'csekk', 'utánvét', 
    'ajándékutalvány', 'barion', 'barter', 'csoportos beszedés', 
    'OTP Simple', 'kompenzáció', 'kupon', 'PayPal', 'PayU', 
    'SZÉP kártya', 'utalvány'
  ];
  penznemOptions = ['HUF', 'EUR', 'USD'];
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
        
        // Transform form data to match backend DTO structure
        const requestData = {
          pdfLetoltes: formValue.pdfLetoltes || false,
          elotag: formValue.elotag,
          fizmod: formValue.fizmod,
          penznem: formValue.penznem,
          megjegyzes: formValue.megjegyzes || null,
          tetelek: formValue.tetelek || [],
          kifizetesek: formValue.kifizetesek?.length ? formValue.kifizetesek : null
        };

        console.log('Sending to backend:', requestData);

        const newReceipt = await this.pageService.createReceipt(requestData).toPromise();
        console.log('Receipt created:', newReceipt);
        
        // Navigate to new receipt details or home
        this.router.navigate(['/details', newReceipt?.alap.hivasAzonosito]);
      } catch (error) {
        console.error('Error creating receipt:', error);
        // Show error toast/message here
      }
    }
  }
}

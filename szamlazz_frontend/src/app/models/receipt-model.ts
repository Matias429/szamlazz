export interface TotalOssz {
  netto: number;
  afa: number;
  brutto: number;
}

export interface Osszegek {
  totalossz: TotalOssz;
}

export interface ReceiptItem {
  megnevezes: string;
  mennyiseg: number;
  mennyisegiEgyseg: string;
  nettoEgysegar: number;
  afakulcs: string;
  netto: number;
  afa: number;
  brutto: number;
}

export interface Tetelek {
  items: ReceiptItem[];
}

export interface PaymentItem {
  fizetoeszkoz: string;
  osszeg: number;
}

export interface Kifizetesek {
  items: PaymentItem[];
}

export interface Alap {
  fizmod: string;
  hivasAzonosito: string;
  id: number;
  kelt: string;
  nyugtaszam: string;
  penznem: string;
  stornozott: boolean;
  tipus: string;
  nyugtaPdf?: string;
}

export interface Receipt {
  alap: Alap;
  tetelek: Tetelek;
  kifizetesek?: Kifizetesek;
  osszegek: Osszegek;
}

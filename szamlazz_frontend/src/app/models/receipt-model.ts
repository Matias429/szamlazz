export interface TotalAmounts {
  net: number;
  vat: number;
  gross: number;
}

export interface AmountList {
  totalAmounts: TotalAmounts;
}

export interface ReceiptItem {
  name: string;
  amount: number;
  unitOfMeasure: string;
  netUnitPrice: number;
  vatRate: string;
  net: number;
  vat: number;
  gross: number;
}

export interface ItemList {
  itemList: ReceiptItem[];
}

export interface PaymentItem {
  paymentMethod: string;
  amount: number;
}

export interface PaymentList {
  itemList: PaymentItem[];
}

export interface Details {
  paymentMethod: string;
  callId: string;
  id: number;
  receiptDate: string;
  receiptNumber: string;
  currency: string;
  isCancelled: boolean;
  receiptType: string;
  receiptPdf?: string;
}

export interface Receipt {
  details: Details;
  itemList: ItemList;
  paymentList?: PaymentList;
  amountList: AmountList;
}

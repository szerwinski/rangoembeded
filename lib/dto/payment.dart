enum PaymentType {
  credito,
  debito,
  voucher
}

class Payment {
  String id;
  PaymentType type; 
  String amount;

  Payment({required this.id, required this.type, required this.amount});

}

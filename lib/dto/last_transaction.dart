class LastTransactions {
  String idTransaction = "";
  TransactionStatus status;

  LastTransactions() : status = TransactionStatus.NOT_FOUND;
}

enum TransactionStatus {
  APPROVED,
  DENIED,
  NO_ANSWER,
  ERROR,
  NOT_FOUND,
}

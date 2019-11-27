import { TransactionDetailedDto } from './transaction-detailed.dto';
export interface TransactionDto {

  id?: number;

  customerInfoId: number;

  transactionDetaileds: TransactionDetailedDto[];

}

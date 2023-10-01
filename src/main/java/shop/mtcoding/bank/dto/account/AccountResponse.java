package shop.mtcoding.bank.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.transaction.Transaction;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.util.CustomDateUtil;

import java.util.List;

public class AccountResponse {

    @Schema(title = "계좌 등록 응답")
    @Getter
    @Setter
    public static class AccountSaveResponse {
        @Schema(title = "id", description = "id")
        private Long id;
        @Schema(title = "계좌 번호", description = "계좌 번호")
        private Long number;
        @Schema(title = "잔액", description = "잔액")
        private Long balance;

        public AccountSaveResponse(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }

    @Schema(title = "계좌 목록 조회 응답")
    @Getter
    @Setter
    public static class AccountListResponse {
        @Schema(title = "풀네임", description = "풀네임")
        private String fullname;
        @Schema(title = "계좌 목록", description = "계좌 목록")
        private List<AccountDto> accounts;

        public AccountListResponse(User user, List<Account> accounts) {
            this.fullname = user.getFullname();
            this.accounts = accounts.stream().map(AccountDto::new).toList();
        }

        @Schema(title = "계좌 상세")
        @Getter
        @Setter
        public class AccountDto {
            @Schema(title = "id", description = "id")
            private Long id;
            @Schema(title = "계좌 번호", description = "계좌 번호")
            private Long number;
            @Schema(title = "잔액", description = "잔액")
            private Long balance;

            public AccountDto(Account account) {
                this.id = account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }
    }

    @Getter
    @Setter
    public static class AccountDepositResponse {
        private Long id;
        private Long number;
        private TransactionDto transaction;

        public AccountDepositResponse(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.transaction = new TransactionDto(transaction);
        }

        @Getter
        @Setter
        public class TransactionDto {
            private Long id;
            private String gubun;
            private String sender;
            private String receiver;
            private Long amount;
            @JsonIgnore
            private Long depositAccountBalance; // 클라이언트에게 전달X -> 서비스단에서 테스트 용도로 사용
            private String tel;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.depositAccountBalance = transaction.getDepositAccountBalance();
                this.tel = transaction.getTel();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    @Schema(title = "계좌 출금 응답")
    @Getter
    @Setter
    public static class AccountWithdrawResponse {
        @Schema(title = "id", description = "id")
        private Long id;
        @Schema(title = "계좌 번호", description = "계좌 번호")
        private Long number;
        @Schema(title = "잔액", description = "잔액")
        private Long balance;
        @Schema(title = "거래 내역", description = "거래 내역")
        private TransactionDto transaction;

        public AccountWithdrawResponse(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new TransactionDto(transaction);
        }

        @Schema(title = "거래 내역", description = "거래 내역")
        @Getter
        @Setter
        public class TransactionDto {
            @Schema(title = "id", description = "id")
            private Long id;
            @Schema(title = "구분", description = "구분", example = "DEPOSIT|WITHDRAW")
            private String gubun;
            @Schema(title = "송금자", description = "송금자")
            private String sender;
            @Schema(title = "수금자", description = "수금자")
            private String receiver;
            @Schema(title = "금액", description = "금액")
            private Long amount;
            @Schema(title = "거래일시", description = "거래일시", example = "yyyy-MM-dd HH:mm:ss")
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    @Setter
    @Getter
    public static class AccountTransferResponse {
        private Long id; // 계좌 ID
        private Long number; // 계좌번호
        private Long balance; // 출금 계좌 잔액
        private TransactionDto transaction;

        public AccountTransferResponse(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new TransactionDto(transaction);
        }

        @Setter
        @Getter
        public class TransactionDto {
            private Long id;
            private String gubun;
            private String sender;
            private String reciver;
            private Long amount;
            @JsonIgnore
            private Long depositAccountBalance;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.reciver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.depositAccountBalance = transaction.getDepositAccountBalance();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    @Getter
    @Setter
    public static class AccountDetailResponse {
        private Long id; // 계좌 ID
        private Long number; // 계좌번호
        private Long balance; // 그 계좌의 최종 잔액
        private List<TransactionDto> transactions;

        public AccountDetailResponse(Account account, List<Transaction> transactions) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transactions = transactions.stream()
                    .map((transaction) -> new TransactionDto(transaction, account.getNumber()))
                    .toList();
        }

        @Getter
        @Setter
        public class TransactionDto {

            private Long id;
            private String gubun;
            private Long amount;

            private String sender;
            private String receiver;

            private String tel;
            private String createdAt;
            private Long balance;

            public TransactionDto(Transaction transaction, Long accountNumber) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.amount = transaction.getAmount();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
                this.tel = transaction.getTel() == null ? "없음" : transaction.getTel();

                if (transaction.getDepositAccount() == null) {
                    this.balance = transaction.getWithdrawAccountBalance();
                } else if (transaction.getWithdrawAccount() == null) {
                    this.balance = transaction.getDepositAccountBalance();
                } else {
                    if (accountNumber.longValue() == transaction.getDepositAccount().getNumber().longValue()) {
                        this.balance = transaction.getDepositAccountBalance();
                    } else {
                        this.balance = transaction.getWithdrawAccountBalance();
                    }
                }

            }
        }
    }

}

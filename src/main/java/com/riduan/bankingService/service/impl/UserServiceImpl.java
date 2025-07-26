package com.riduan.bankingService.service.impl;

import com.riduan.bankingService.dto.*;
import com.riduan.bankingService.entity.Transaction;
import com.riduan.bankingService.entity.User;
import com.riduan.bankingService.repository.UserRepository;
import com.riduan.bankingService.utils.AccountUtlis;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TransactionService transactionService;




    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /*
        * Creating an account - Saving a new user into to db
        * check if user already has an account
        */
        if(userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtlis.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtlis.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();

        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtlis.generateAccountNumber())
                .email(userRequest.getEmail())
                .accountBalance(BigDecimal.ZERO)
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();
        User savedUser = userRepository.save(newUser);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipients(savedUser.getEmail())
                .subject("ACCOUNT CREATION.")
                .messageBody("Congratulation! Your account is successfully created.\n" +
                        "Your account number: " + savedUser.getAccountNumber())
                .build();

        emailService.sendEmailDetails(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtlis.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtlis.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName()+" "+
                        savedUser.getLastName()+" "+
                                savedUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        // Check if the provided accountNumber exists or not.
        boolean isAccountNumberExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountNumberExists){
            return BankResponse.builder()
                    .responseMessage(AccountUtlis.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .responseCode(AccountUtlis.ACCOUNT_NOT_EXISTS_CODE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtlis.FOUND_ACCOUNT_SUCCESSFUL_CODE)
                .responseMessage(AccountUtlis.FOUND_ACCOUNT_SUCCESSFUL_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                        .accountNumber(foundUser.getAccountNumber())
                        .accountBalance(foundUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountNumberExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountNumberExists){
            return AccountUtlis.ACCOUNT_NOT_EXISTS_CODE+"\n"+AccountUtlis.ACCOUNT_NOT_EXISTS_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());

        return foundUser.getFirstName()+" "+foundUser.getLastName()+" "+foundUser.getOtherName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        boolean isAccountNumberExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountNumberExists){
            return BankResponse.builder()
                    .responseMessage(AccountUtlis.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .responseCode(AccountUtlis.ACCOUNT_NOT_EXISTS_CODE)
                    .accountInfo(null)
                    .build();
        }

        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance()
                .add(request.getAmount()));

        EmailDetails creditAlertDetails = EmailDetails.builder()
                .subject("Credit Alert")
                .messageBody("You have been credited money "+request.getAmount()+" taka"+
                        " to account number: "+request.getAccountNumber())
                .recipients(userToCredit.getEmail())
                .build();
        emailService.sendEmailDetails(creditAlertDetails);

        userRepository.save(userToCredit);

        // Sava transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .amount(request.getAmount())
                .status("SUCCESS")
                .transactionType("Credit")
                .build();

        transactionService.saveTransaction(transactionDto);


        return BankResponse.builder()
                .responseMessage(AccountUtlis.CREDITED_ACCOUNT_SUCCESSFUL_MESSAGE)
                .responseCode(AccountUtlis.CREDITED_ACCOUNT_SUCCESSFUL_CODE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(request.getAccountNumber())
                        .accountName(userToCredit.getFirstName()+" "+userToCredit.getLastName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        boolean isAccountNumberExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountNumberExists){
            return BankResponse.builder()
                    .responseMessage(AccountUtlis.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .responseCode(AccountUtlis.ACCOUNT_NOT_EXISTS_CODE)
                    .accountInfo(null)
                    .build();
        }
        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        BigInteger availableBalance =userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();
        if ( availableBalance.intValue() < debitAmount.intValue()){
            return BankResponse.builder()
                    .responseCode(AccountUtlis.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtlis.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        userToDebit.setAccountBalance(userToDebit.getAccountBalance()
                .subtract(request.getAmount()));

        userRepository.save(userToDebit);

        EmailDetails debitAlertDetails = EmailDetails.builder()
                .subject("Debit Alert")
                .messageBody("You have been withdraw money "+request.getAmount()+" taka"+
                        " from account number: "+request.getAccountNumber())
                .recipients(userToDebit.getEmail())
                .build();
        emailService.sendEmailDetails(debitAlertDetails);

        // Sava transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToDebit.getAccountNumber())
                .amount(request.getAmount())
                .status("SUCCESS")
                .transactionType("Debit")
                .build();

        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseMessage(AccountUtlis.ACCOUNT_DEBITED_MESSAGE)
                .responseCode(AccountUtlis.ACCOUNT_DEBITED_SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(request.getAccountNumber())
                        .accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName())
                        .accountBalance(userToDebit.getAccountBalance())
                        .build())
                .build();

    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        boolean isSourceAccountExists = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
        boolean isDestinationAccountExists = userRepository.existsByAccountNumber((request.getDestinationAccountNumber()));
        if(!isDestinationAccountExists || !isSourceAccountExists){
            return BankResponse.builder()
                    .responseMessage(AccountUtlis.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .responseCode(AccountUtlis.ACCOUNT_NOT_EXISTS_CODE)
                    .accountInfo(null)
                    .build();
        }
        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        BigInteger availableBalance = sourceAccountUser.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();

        if ( availableBalance.intValue() < debitAmount.intValue()){
            return BankResponse.builder()
                    .responseCode(AccountUtlis.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtlis.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance()
                .subtract(request.getAmount()));

        userRepository.save(sourceAccountUser);
        EmailDetails debitAlertDetails = EmailDetails.builder()
                .subject("Money Transfer Alert")
                .messageBody("You have been transferred money "+request.getAmount()+" taka"+
                        " to account number: "+request.getDestinationAccountNumber())
                .recipients(sourceAccountUser.getEmail())
                .build();
        emailService.sendEmailDetails(debitAlertDetails);


        User userToCredit = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance()
                .add(request.getAmount()));

        userRepository.save(userToCredit);
        EmailDetails creditAlertDetails = EmailDetails.builder()
                .subject("Money Transfer Alert")
                .messageBody("You have been get money "+request.getAmount()+" taka"+
                        " from account number: "+request.getSourceAccountNumber())
                .recipients(userToCredit.getEmail())
                .build();
        emailService.sendEmailDetails(creditAlertDetails);


        // Sava transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .amount(request.getAmount())
                .status("SUCCESS")
                .transactionType("Credit Transfer")
                .build();

        transactionService.saveTransaction(transactionDto);

        TransactionDto transactionDto1 = TransactionDto.builder()
                .accountNumber(sourceAccountUser.getAccountNumber())
                .amount(request.getAmount())
                .status("SUCCESS")
                .transactionType("Debit Transfer")
                .build();

        transactionService.saveTransaction(transactionDto1);
        return BankResponse.builder()
                .responseCode(AccountUtlis.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtlis.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountName(userToCredit.getFirstName()+" "+userToCredit.getLastName())
                        .accountNumber("from "+sourceAccountUser.getAccountNumber()+" to "+ userToCredit.getAccountNumber())
                        .build())
                .build();
    }
}

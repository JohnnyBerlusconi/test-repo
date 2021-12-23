package com.rigter.miceli.modifier;

import java.util.List;

public class CsvRecord {

    //headers
    private String Transfer_Code;
    private String Container_From;
    private String  Account_From;
    private String  Currency;
    private String  Amount;
    private String  Containter_To;
    private String  Account_To;
    private String  Beneficiary_IBAN_Account;
    private String  Beneficiary_Account;
    private String  Beneficiary;
    private String  Destination_Bank;
    private String  Value_Date;
    private String  Avis_Text;
    private String  Month;
    
    public CsvRecord (String[] fields) {
        this.Transfer_Code = fields[0];
        this.Container_From = fields[1];
        this.Account_From = fields[2];
        this.Currency = fields[3];
        this.Amount = fields[4];
        this.Containter_To = fields[5];
        this.Account_To = fields[6];
        this.Beneficiary_IBAN_Account = fields[7];
        this.Beneficiary_Account = fields[8];
        this.Beneficiary = fields[9];
        this.Destination_Bank = fields[10];
        this.Value_Date = fields[11];
        this.Avis_Text = fields[12];
        this.Month = fields[13];
    }
    
    public String toString() {
        String result = Transfer_Code;
        result += ";";
        result += Container_From;
        result += ";";
        result += Account_From;
        result += ";";
        result += Currency;
        result += ";";
        result += Amount;
        result += ";";
        result += Containter_To;
        result += ";";
        result += Account_To;
        result += ";";
        result += Beneficiary_IBAN_Account;
        result += ";";
        result += Beneficiary_Account;
        result += ";";
        result += Beneficiary;
        result += ";";
        result += Destination_Bank;
        result += ";";
        result += Value_Date;
        result += ";";
        result += Avis_Text;
        result += ";";
        result += Month;
        return result;
    }
    

    public String getTransfer_Code() {
        return Transfer_Code;
    }

    public void setTransfer_Code(String transfer_Code) {
        Transfer_Code = transfer_Code;
    }

    public String getContainer_From() {
        return Container_From;
    }

    public void setContainer_From(String container_From) {
        Container_From = container_From;
    }

    public String getAccount_From() {
        return Account_From;
    }

    public void setAccount_From(String account_From) {
        Account_From = account_From;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getContainter_To() {
        return Containter_To;
    }

    public void setContainter_To(String containter_To) {
        Containter_To = containter_To;
    }

    public String getAccount_To() {
        return Account_To;
    }

    public void setAccount_To(String account_To) {
        Account_To = account_To;
    }

    public String getBeneficiary_IBAN_Account() {
        return Beneficiary_IBAN_Account;
    }

    public void setBeneficiary_IBAN_Account(String beneficiary_IBAN_Account) {
        Beneficiary_IBAN_Account = beneficiary_IBAN_Account;
    }

    public String getBeneficiary_Account() {
        return Beneficiary_Account;
    }

    public void setBeneficiary_Account(String beneficiary_Account) {
        Beneficiary_Account = beneficiary_Account;
    }

    public String getBeneficiary() {
        return Beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        Beneficiary = beneficiary;
    }

    public String getDestination_Bank() {
        return Destination_Bank;
    }

    public void setDestination_Bank(String destination_Bank) {
        Destination_Bank = destination_Bank;
    }

    public String getValue_Date() {
        return Value_Date;
    }

    public void setValue_Date(String value_Date) {
        Value_Date = value_Date;
    }

    public String getAvis_Text() {
        return Avis_Text;
    }

    public void setAvis_Text(String avis_Text) {
        Avis_Text = avis_Text;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }
}

package com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb;

import java.util.Date;

/**
 * Created by ChadHolewinski on 12/6/2017.
 */

public class dbProfile
{
    public String TABLE_PROFILE = "MilkReceipt_Profile";
    public String PROFILE_COLUMN_PKPROFILEID = "pkProfileID";
    public String PROFILE_COLUMN_FKPLANTID = "fkPlantID";
    public String PROFILE_COLUMN_USERNAME = "Username";
    public String PROFILE_COLUMN_FULLNAME = "FullName";
    public String PROFILE_COLUMN_INITIALS = "Initials";
    public String PROFILE_COLUMN_PIN = "Pin";
    public String PROFILE_COLUMN_HAULERSIGNATURE = "HaulerSignature";
    public String PROFILE_COLUMN_HAULERLICENSENUMBER = "HaulerLicenseNumber";
    public String PROFILE_COLUMN_HAULEREXPIRATIONDATE = "HaulerExpirationDate";
    public String PROFILE_COLUMN_HAULERNUMBER = "HaulerNumber";
    public String PROFILE_COLUMN_SIGNATUREAGREEMENT = "SignatureAgreement";
    public String PROFILE_COLUMN_ACTIVE = "Active";
    public String PROFILE_COLUMN_ADMINSECURITY = "AdminSecurity";
    public String PROFILE_COLUMN_LASTSIGNINDATE = "LastSignInDate";
    public String PROFILE_COLUMN_INSERTDATE = "InsertDate";
    public String PROFILE_COLUMN_MODIFIEDDATE = "ModifiedDate";

    String pkProfileID;
    String fkPlantID;
    String Username;
    String FullName;
    String Initials;
    Integer Pin;
    String HaulerSignature;
    String HaulerLicenseNumber;
    String HaulerExpirationDate;
    String HaulerNumber;
    Integer SignatureAgreement;
    Integer Active;
    Integer AdminSecurity;
    Date LastSignInDate;
    Date InsertDate;
    Date ModifiedDate;

    public dbProfile()
    {

    }

    public String getPkProfileID() {
        return pkProfileID;
    }

    public String getFkPlantID() {
        return fkPlantID;
    }

    public String getUsername() {
        return Username;
    }

    public String getFullName() {
        return FullName;
    }

    public String getInitials() {
        return Initials;
    }

    public Integer getPin() {
        return Pin;
    }

    public String getHaulerSignature() {
        return HaulerSignature;
    }

    public String getHaulerLicenseNumber() {
        return HaulerLicenseNumber;
    }

    public String getHaulerExpirationDate() {
        return HaulerExpirationDate;
    }

    public String getHaulerNumber() {
        return HaulerNumber;
    }

    public Integer getSignatureAgreement() {
        return SignatureAgreement;
    }

    public Integer getActive() {
        return Active;
    }

    public Integer getAdminSecurity() {
        return AdminSecurity;
    }

    public Date getLastSignInDate() {
        return LastSignInDate;
    }

    public Date getInsertDate() {
        return InsertDate;
    }

    public Date getModifiedDate() {
        return ModifiedDate;
    }

    public void setPkProfileID(String pkProfileID) {
        this.pkProfileID = pkProfileID;
    }

    public void setFkPlantID(String fkPlantID) {
        this.fkPlantID = fkPlantID;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public void setInitials(String initials) {
        Initials = initials;
    }

    public void setPin(Integer pin) {
        Pin = pin;
    }

    public void setHaulerSignature(String haulerSignature) {
        HaulerSignature = haulerSignature;
    }

    public void setHaulerLicenseNumber(String haulerLicenseNumber) { HaulerLicenseNumber = haulerLicenseNumber; }

    public void setHaulerExpirationDate(String haulerExpirationDate) { HaulerExpirationDate = haulerExpirationDate; }

    public void setHaulerNumber(String haulerNumber) {
        HaulerNumber = haulerNumber;
    }

    public void setSignatureAgreement(Integer signatureAgreement) { SignatureAgreement = signatureAgreement; }

    public void setActive(Integer active) {
        Active = active;
    }

    public void setAdminSecurity(Integer adminSecurity) {
        AdminSecurity = adminSecurity;
    }

    public void setLastSignInDate(Date lastSignInDate) {
        LastSignInDate = lastSignInDate;
    }

    public void setInsertDate(Date insertDate) {
        InsertDate = insertDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        ModifiedDate = modifiedDate;
    }
}

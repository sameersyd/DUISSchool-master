package com.haze.sameer.duisschool;

public class FeesObject {

    String typeFees,groupFees,amountFees,dueDateFees,isactiveFees;

    public FeesObject(String typeFees, String groupFees, String amountFees, String dueDateFees, String isactiveFees) {
        this.typeFees = typeFees;
        this.groupFees = groupFees;
        this.amountFees = amountFees;
        this.dueDateFees = dueDateFees;
        this.isactiveFees = isactiveFees;
    }

    public String getTypeFees() {
        return typeFees;
    }
    public String getGroupFees() {
        return groupFees;
    }
    public String getAmountFees() {
        return amountFees;
    }
    public String getDueDateFees() {
        return dueDateFees;
    }
    public String getIsactiveFees() {
        return isactiveFees;
    }

}

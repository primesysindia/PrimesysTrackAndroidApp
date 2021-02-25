package com.primesys.VehicalTracking.Dto;

public class PaymentTypeDTO {
	String PaymentType,PaymentTypeId,PlanId;
	public String getPaymentType() {
		return PaymentType;
	}
	public void setPaymentType(String paymentType) {
		PaymentType = paymentType;
	}
	public int getId() {
		return Id;
	}

	public String getPaymentTypeId() {
		return PaymentTypeId;
	}

	public void setPaymentTypeId(String paymentTypeId) {
		PaymentTypeId = paymentTypeId;
	}

	public String getPlanId() {
		return PlanId;
	}

	public void setPlanId(String planId) {
		PlanId = planId;
	}

	public void setId(int id) {
		Id = id;

	}
	public int getAmountOfPlan() {
		return AmountOfPlan;
	}
	public void setAmountOfPlan(int amountOfPlan) {
		AmountOfPlan = amountOfPlan;
	}
	public int getDaysOfplan() {
		return DaysOfplan;
	}
	public void setDaysOfplan(int daysOfplan) {
		DaysOfplan = daysOfplan;
	}
	int Id,AmountOfPlan,DaysOfplan;
}

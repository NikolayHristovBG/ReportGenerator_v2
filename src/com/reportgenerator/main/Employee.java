package com.reportgenerator.main;

public class Employee {
	private String name;
	private long totalSales;
	private long salesPeriod;
	private double experienceMultiplier;
	private boolean useExprienceMultiplier;
	private double score;
	
	Employee(String name, long totalSales, long salesPeriod, double experienceMultiplier, boolean useExprienceMultiplier){
		this.name = name;
		this.totalSales = totalSales;
		this.salesPeriod = salesPeriod;
		this.experienceMultiplier = experienceMultiplier;
		this.useExprienceMultiplier = useExprienceMultiplier;
	}

	public String getName() {
		return name;
	}

	public long getTotalSales() {
		return totalSales;
	}

	public long getSalesPeriod() {
		return salesPeriod;
	}

	public double getExperienceMultiplier() {
		return experienceMultiplier;
	}
	
	public boolean getUseExprienceMultiplier() {
		return useExprienceMultiplier;
	}
	
	public double getScore() {
		return score;
	}
	
	public double calculateScore() {
		score = 0.0;

		if (useExprienceMultiplier) {
			score = totalSales/salesPeriod*experienceMultiplier;
		} else {
			score = totalSales/salesPeriod;
		}
		
		return score;
	}
	
}

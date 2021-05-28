package com.reportgenerator.main;

import java.util.*;
import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {
	public static void main(String[] args) throws Exception{
		
		// Path to data and definition files passed as arguments when the program is executed via command line
//		String dataFilePath = args[0];
//		String definitionFilePath = args[1];
		
		// Path to data and definition files passed as arguments when the program is executed via IDE
		String dataFilePath = "src\\com\\reportgenerator\\json\\data.json";
		String definitionFilePath = "src\\com\\reportgenerator\\json\\definition.json";	
								
		
		try {
			
			// Parse definition file
			JSONParser parser = new JSONParser();
			Object objDefinition = parser.parse(new BufferedReader( new FileReader(definitionFilePath)));
			JSONObject jsonObject = (JSONObject) objDefinition;
			
			long topPerformersThreshold = (long)jsonObject.get("topPerformersThreshold");
			Boolean useExprienceMultiplier = (Boolean)jsonObject.get("useExprienceMultiplier");
			long periodLimit = (long)jsonObject.get("periodLimit");
			
			// Parse data file
			Object objData = parser.parse(new BufferedReader( new FileReader(dataFilePath)));
			JSONArray jsonArray = (JSONArray) objData;
			
			@SuppressWarnings("unchecked")
			ListIterator<Object> iteratorTemp = jsonArray.listIterator();
			@SuppressWarnings("unchecked")
			ListIterator<Object> iterator = jsonArray.listIterator();
			
			// calculate employee number
			int employeeNumber = 0;
			while(iteratorTemp.hasNext()) {
				@SuppressWarnings("unused")
				JSONObject employeeTemp = (JSONObject) iteratorTemp.next();
				employeeNumber++;				
			}
			
			Employee[] employees = new Employee[employeeNumber];
			int i = 0;
			int employeeCount = employees.length;
			@SuppressWarnings("unused")
			double scoreSum = 0.0;
			
			// populate employees array with employee objects
			while(iterator.hasNext()) {
				JSONObject employee = (JSONObject) iterator.next();
								
				employees[i] = new Employee(
						(String)employee.get("name"),
						(long)employee.get("totalSales"), 
						(long)employee.get("salesPeriod"), 
						(double)employee.get("experienceMultiplier"), 
						(boolean)useExprienceMultiplier);
				i++;		
			}
			
			// calculate score
			for (Employee employee : employees) {
				if (employee.getSalesPeriod() <= periodLimit) {
					scoreSum += employee.calculateScore();	
				}	
			}
				
					
			//sort employees by score
			boolean isNotSorted = true;
			while (isNotSorted) {
				isNotSorted = false;
				for(int j=0; j<employees.length-1; j++) {
					if (employees[j].getScore() < employees[j+1].getScore()) {
						Employee tempEmployee = employees[j];
						employees[j] = employees[j+1];
						employees[j+1] = tempEmployee;
						isNotSorted = true;
					}
				}
			}
			
			double employeePercentile = 0.0;
			int employeePosition = 1;
			StringBuilder sb = new StringBuilder();
			sb.append("Name,");
		    sb.append("Score");
		    sb.append('\n');
		    
			for (Employee employee : employees) {
				employeePercentile = (1-((double)employeePosition/employeeCount)) * 100;
				
				if (employeePercentile < (100 - topPerformersThreshold)) {
					break;
				} else {
					sb.append("" + employee.getName() + ",");
				    sb.append(employee.getScore());
				    sb.append('\n');
				}
				employeePosition++;
			}
			
			try (PrintWriter writer = new PrintWriter(new File("Result.csv"))) {
			    writer.write(sb.toString());
			} catch (FileNotFoundException e) {
			      System.out.println(e.getMessage());
			}
		} catch (FileNotFoundException e) {
		      System.out.println(e.getMessage());	
		} catch (IOException e) {
			System.out.println("I/O Error: " + e);
		}
	}
}

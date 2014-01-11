package com.testgen.view;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sumeet Chhetri<br/>
 * The Form element class provides data for generating actual form fields
 */
public class ViewField {
	
	private String label;
	
	private String name;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	private String description;
	
	private String type;
	
	private String varType;
	
	private String claz;
	
	private List<String> values = new ArrayList<String>();
	
	private List<Validation> validations = new ArrayList<Validation>();

	public void setVarType(@SuppressWarnings("rawtypes") Class claz)
	{
		if(claz.equals(Integer.class) || claz.equals(Short.class) || claz.equals(Long.class) 
				|| claz.equals(int.class) || claz.equals(short.class) || claz.equals(long.class) 
				|| claz.equals(Number.class))
			setVarType("onblur=\"validate(this, 'number')\"");
		else if(claz.equals(Double.class) || claz.equals(Float.class)
				|| claz.equals(double.class) || claz.equals(float.class))
			setVarType("onblur=\"validate(this, 'float')\"");
		else if(claz.equals(Boolean.class) || claz.equals(boolean.class))
			setVarType("onblur=\"validate(this, 'boolean')\"");
	}
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
		this.name = this.label.replaceAll("'", "");
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return the varType
	 */
	public String getVarType() {
		return varType;
	}

	/**
	 * @param varType the varType to set
	 */
	public void setVarType(String varType) {
		this.varType = varType;
	}

	/**
	 * @return the values
	 */
	public List<String> getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(List<String> values) {
		this.values = values;
	}

	/**
	 * @return the validations
	 */
	public List<Validation> getValidations() {
		return validations;
	}

	/**
	 * @param validations the validations to set
	 */
	public void setValidations(List<Validation> validations) {
		this.validations = validations;
	}

	/**
	 * @return the claz
	 */
	public String getClaz() {
		return claz;
	}

	/**
	 * @param claz the claz to set
	 */
	public void setClaz(String claz) {
		this.claz = claz;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ViewField [label=" + label + ", description=" + description
				+ ", type=" + type + ", varType=" + varType + ", claz=" + claz
				+ ", values=" + values + ", validations=" + validations + "]";
	}
}

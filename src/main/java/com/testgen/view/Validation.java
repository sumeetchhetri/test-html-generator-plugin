package com.testgen.view;

/**
 * @author Sumeet Chhetri<br/>
 * The validation object providing form validation rules
 */
public class Validation {
	
	private String type;
	private String value;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValidateJSRule()
	{
		if(type!=null)
			return type+((value!=null)?"["+value+"]":"");
		return null;
	}
	@Override
	public String toString() {
		return "Validation [type=" + type + ", value=" + value + "]";
	}
	
	public Validation(){}
	
	public Validation(String type)
	{
		this.type = type;
	}
}

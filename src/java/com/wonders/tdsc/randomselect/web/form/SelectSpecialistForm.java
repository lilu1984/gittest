package com.wonders.tdsc.randomselect.web.form;

import org.apache.struts.action.ActionForm;

public class SelectSpecialistForm extends ActionForm{

	private String specialistIds[];
	private String specialistType[];

	public String[] getSpecialistType() {
		return specialistType;
	}

	public void setSpecialistType(String[] specialistType) {
		this.specialistType = specialistType;
	}

	public String[] getSpecialistIds() {
		return specialistIds;
	}

	public void setSpecialistIds(String[] specialistIds) {
		this.specialistIds = specialistIds;
	}
	
}

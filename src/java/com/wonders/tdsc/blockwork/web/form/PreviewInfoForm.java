package com.wonders.tdsc.blockwork.web.form;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import org.apache.struts.upload.FormFile;

import org.apache.struts.action.ActionForm;

public class PreviewInfoForm extends ActionForm{

    
    //����������explorId
    //��������
    private BigDecimal attendNum;
    
    //������Ա
    private String staff;
    
    //�ֳ����
    private String explorStat;
    
    
    //���õؿ����ִ�б�scheduleId 
    //�ֳ������ʱ��
    private Timestamp inspDate;
    
    //�ֳ������ص�
    private String inspLoc;
    
    
    private String recordId;
    
    //�ϴ��ļ�
    private FormFile file;

    public BigDecimal getAttendNum() {
        return attendNum;
    }

    public void setAttendNum(BigDecimal attendNum) {
        this.attendNum = attendNum;
    }

    public String getExplorStat() {
        return explorStat;
    }

    public void setExplorStat(String explorStat) {
        this.explorStat = explorStat;
    }

    public Timestamp getInspDate() {
        return inspDate;
    }

    public void setInspDate(Timestamp inspDate) {
        this.inspDate = inspDate;
    }

    public String getInspLoc() {
        return inspLoc;
    }

    public void setInspLoc(String inspLoc) {
        this.inspLoc = inspLoc;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}



}

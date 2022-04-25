package com.florianhansen.applicationmanager.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity(name = "application")
@Table(name = "application")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Application {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true)
	private Integer id;
	
	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "job_title")
	private String jobTitle;
	
	@Column(name = "work_type_id")
	private Integer workTypeId;
	
	@Column(name = "status_id")
	private Integer statusId;
	
	@Column(name = "company_name")
	private String companyName;
	
	@Column(name = "submission_date")
	private Date submissionDate;
	
	@Column(name = "wanted_salary")
	private Float wantedSalary;
	
	@Column(name = "accepted_salary")
	private Float acceptedSalary;
	
	@Column(name = "start_date")
	private Date startDate;
	
	@Column(name = "commentary")
	private String commentary;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Integer getWorkTypeId() {
		return workTypeId;
	}

	public void setWorkTypeId(Integer workTypeId) {
		this.workTypeId = workTypeId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public Float getWantedSalary() {
		return wantedSalary;
	}

	public void setWantedSalary(Float wantedSalary) {
		this.wantedSalary = wantedSalary;
	}

	public Float getAcceptedSalary() {
		return acceptedSalary;
	}

	public void setAcceptedSalary(Float acceptedSalary) {
		this.acceptedSalary = acceptedSalary;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
}

package com.xoolibeut.extract;

import java.util.Date;
import java.util.List;

public class XoolibeutFeuilleExcel {
	private List<XoolibeutLine> lines;
	private String name;
	private Date startDate;
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	private Date endDate;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<XoolibeutLine> getLines() {
		return lines;
	}

	public void setLines(List<XoolibeutLine> lines) {
		this.lines = lines;
	}
}

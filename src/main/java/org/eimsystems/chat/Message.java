package org.eimsystems.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
public class Message implements Serializable{
	private long id;
	private long idr;
	private Date date;
	//todo change to byte[], cause of End to End encryption
	private String text;
	public Message(@JsonProperty("id") long id, @JsonProperty("idr")long idr, @JsonProperty("date") Date date, @JsonProperty("text") String text){
		this.id = id;
		this.idr = idr;
		this.date = date;
		this.text = text;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getIdReceive() {
		return idr;
	}
	public void setIdReceive(long idr) {
		this.idr = idr;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}

package com.example.handwriting.bean;

import java.util.List;

public class Result {
	private long log_id;
	private int words_result_num;
	private List<Prword> words_result;
	public long getLog_id() {
		return log_id;
	}
	public void setLog_id(long log_id) {
		this.log_id = log_id;
	}
	public int getWords_result_num() {
		return words_result_num;
	}
	public void setWords_result_num(int words_result_num) {
		this.words_result_num = words_result_num;
	}
	public List<Prword> getWords_result() {
		return words_result;
	}
	public void setWords_result(List<Prword> words_result) {
		this.words_result = words_result;
	}
	@Override
	public String toString() {
		return "Result [log_id=" + log_id + ", words_result_num=" + words_result_num + ", words_result=" + words_result
				+ "]";
	}
	
	
}

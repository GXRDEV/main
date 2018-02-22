package com.tspeiz.modules.test;

import java.util.HashSet;
import java.util.Set;

public class Article {
	 private String id;
	  private String title;
	  private String content;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public static void main(String[] args) {
		 Set<String> set=new HashSet<String>();
		  set.add("id");
		  System.out.println(set.contains("id"));
	}
}

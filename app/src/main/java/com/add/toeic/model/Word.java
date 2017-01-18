package com.add.toeic.model;

public class Word {

	private int id;
	private String name;
	private String name_key;
	private String sound;
	private String example;
	private String example_key;
	private String expand;
	private int kind;
	private int remember;

    // kind==1 ==> Nound
	// kind==2 ==> V
	// kind==3 ==> adj
	// kind==4 ==> adv
	// kind==0 ==> (n, adv)... lan lon

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public String getExample_key() {
		return example_key;
	}

	public void setExample_key(String example_key) {
		this.example_key = example_key;
	}

	public String getExpand() {
		return expand;
	}

	public void setExpand(String expand) {
		this.expand = expand;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName_key() {
		return name_key;
	}

	public void setName_key(String name_key) {
		this.name_key = name_key;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public int getRemember() {
		return remember;
	}

	public void setRemember(int remember) {
		this.remember = remember;
	}

}

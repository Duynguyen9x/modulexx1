package info.androidhive.navigationdrawer.model;

public class Word {

	public int id;
	public String name;
	public String sound;
	public String name_key;
	public String examle;
	public String example_key;
	public String expand;
	public int kind_word;

	// kind_word==1 ==> Nound
	// kind_word==2 ==> V
	// kind_word==3 ==> adj
	// kind_word==4 ==> adv
	// kind_word==0 ==> (n, adv)... lan lon

	public String getExpand() {
		return expand;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public int getKind_word() {
		return kind_word;
	}

	public void setKind_word(int kind_word) {
		this.kind_word = kind_word;
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

	public String getExamle() {
		return examle;
	}

	public void setExamle(String examle) {
		this.examle = examle;
	}

	public String getExample_key() {
		return example_key;
	}

	public void setExample_key(String example_key) {
		this.example_key = example_key;
	}

	@Override
	public String toString() {
		return "Word [id=" + id + ", name=" + name + ", sound=" + sound
				+ ", name_key=" + name_key + ", examle=" + examle
				+ ", example_key=" + example_key + ", expand=" + expand
				+ ", kind_word=" + kind_word + "]";
	}

}

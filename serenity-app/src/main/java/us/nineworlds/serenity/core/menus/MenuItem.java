package us.nineworlds.serenity.core.menus;

import java.io.Serializable;

public class MenuItem implements Serializable {
	
	private static final long serialVersionUID = -5394624840598604518L;
	private String type;
	private String title;
	private String section;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}
	
	@Override
	public String toString() {
		return getTitle();
	}
	
}

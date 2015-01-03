package nkolkoikrzyzyk.model;

public enum Mark {
	NOUGHT(-1, "O"),
	CROSS(1, "X"),
	EMPTY(0, ".");
	
	private final int value;
	private final String str;
	
	Mark(int value, String str)
	{
		this.value = value; 
		this.str = str;
	}

	/**
	 * @return the value
	 */
	public int value() {
		return value;
	}

	/**
	 * @return the str
	 */
	public String str() {
		return str;
	}
}

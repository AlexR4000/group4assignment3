package appDomain;

import java.io.Serializable;

public class Nodetam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String word;
	Nodetam left;
	Nodetam right;
	
	Nodetam(String word) {
		this.word = word;
	}
}

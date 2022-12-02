package WordleSolverBot;

class testInfo {
	private final String word;
	private final int attempts;

	public testInfo(String word, int attempts) {
	     this.word = word;
	     this.attempts = attempts;
	  }

	public String getWord() {
		return word;
	}

	public int getAttempts() {
		return attempts;
	}
}
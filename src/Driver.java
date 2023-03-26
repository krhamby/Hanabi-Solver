
public class Driver {
	public static void main(String[] args) {
		boolean test = false;
		// Hanabi game = new Hanabi(true);
		// game.play();
		
		// This is the code I will actually use to test your code's behavior.
		int total = 0;
		for (int i = 0; i < 1000; i++) {
			Hanabi next = new Hanabi(false);
			int score = next.play();
			System.out.println("Game " + i + " score: " + score);
			total += score;
			if (score > 0) {
				test = true;
			}
		}
		System.out.println("Final average: " + (total/1000.0));
		if (test) {
			System.out.println("You passed the test!");
		}
		else {
			System.out.println("You failed the test.");
		}
	}

}

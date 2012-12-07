import java.util.*;
import acm.program.ConsoleProgram;
import acm.util.RandomGenerator;

@SuppressWarnings("serial")
public class tester extends ConsoleProgram {
	public void run() {
		while(true) {
			//TODO: allow min, max, and length of sequence to be set by user
			int[] seq = getRandomSequence(1,12,20);
			printOutput(numConsecutive(seq));
			if (!runAgain()) break;
		}
		exit();
	}

	private int[] getRandomSequence(int min, int max, int length) {
		RandomGenerator rgen = new RandomGenerator();
		int[] seq = new int[length];
		print("sequence is: ");
		for (int i = 0; i < seq.length; i++) {
			seq[i] = rgen.nextInt(min, max);
			print(seq[i]+((i == length)? "" : ", "));
		}
		println("");
		return seq;
	}
	
	private boolean runAgain() {
		return readBoolean("Again? (press Enter for \"yes\", anything else for \"no\"): ", "", "n");
	}

	private void printOutput(ArrayList<int[]> intervals) {
		println("The intervals from this sequence are:");
		for (int[] i: intervals) {
			int length = i[1] - i[0] + 1;
			println(brack(i) + " of length " + length);
		}
	}
	
	private String brack(int[] interval) {
		return "{" + interval[0] + ", " + interval[1] + "}";
	}
	
	private ArrayList<int[]> numConsecutive(int[] dice) {
		ArrayList<int[]> intervals = new ArrayList<int[]>();
		for(int d: dice){
			intervals.add(new int[]{d,d});
			joinOverlappingIntervals(intervals);
		}
		return intervals;
	}
	
	private void joinOverlappingIntervals(ArrayList<int[]> intervals) {
		//iterate over pairs of intervals, joining pairs if they overlap or touch
		for (int i = 0; i < intervals.size() - 1; i++) {
			for (int j = i+1; j < intervals.size(); j++) {
				int[] interval1 = intervals.get(i);
				int[] interval2 = intervals.get(j);
				if (perhapsJoinIntervals(interval1, interval2, intervals) ||
						perhapsJoinIntervals(interval2, interval1, intervals)) {
					joinOverlappingIntervals(intervals);
					break;
				}
			}
		}
	}
	
	private boolean perhapsJoinIntervals(
			int[] interval1, int[] interval2, ArrayList<int[]> intervals) {
		boolean canjoin = false;
		if ( interval2[0] - 1 <= interval1[1] && interval1[1] <= interval2[1] ) {
			canjoin = true;
			//right endpoint of first interval is in second interval
			int newLeft = ((interval2[0] < interval1[0])? interval2[0]: interval1[0]);
			//println's for troubleshooting
			//println("found overlap: ");
			//println("will remove"+brack(interval1)+"and"+brack(interval2));
			//println("will insert"+brack(new int[] {newLeft, interval2[1]}));
			intervals.add(new int[] {newLeft, interval2[1]});
			intervals.remove(interval1);
			intervals.remove(interval2);
		}
		return canjoin;
	}

	private void getAListOfUserEnteredNumbers() {
		//TODO: allow user-entered sequences
		String diceRoll = readLine("Enter a dice roll (e.g. 3,5,2,3,6): ");
		diceRoll = diceRoll.replaceAll("[^0-9]{1,}+", "-");
		println(diceRoll);
		StringTokenizer st = new StringTokenizer(diceRoll, "-");
		dice = new int[st.countTokens()];
		int i = 0;
		while(st.hasMoreTokens()) {
			dice[i++] = Integer.parseInt(st.nextToken());
		}
	}

	private int[] dice;
}

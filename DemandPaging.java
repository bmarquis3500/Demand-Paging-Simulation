import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class DemandPaging {
	// public variables
	public static Scanner scanner = new Scanner(System.in);
	public static int[] nums;
	public static int[] frames;
	public static int[] victims, faults;
	public static int[][] frameData;
	public static HashMap<Integer, Integer> refFreqDict;
	public static HashMap<Integer, Integer> refRecDictArr[], refProxDictArr[];

	// main
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String choiceString;
		int choice;
		System.out.println("Greetings, welcome to the File directory processing program.");
		// while loop runs infinitely to continue getting input from user
		while (true) {
			while (true) {
				System.out.println();
				System.out.println("0 – Exit\r\n" + "1 – Read reference string\r\n"
						+ "2 – Generate reference string \r\n" + "3 – Display current reference string \r\n"
						+ "4 – Simulate FIFO\r\n" + "5 – Simulate OPT \r\n" + "6 – Simulate LRU \r\n"
						+ "7 – Simulate LFU \r\n" + "Select option: ");
				choiceString = scanner.next();
				// try catch for inappropriate input such as a letter
				try {
					choice = Integer.parseInt(choiceString);
					if (choice > 8 || choice < 0) {
						throw new Exception();
					}
				} catch (Exception e) {
					System.out.println("Sorry, incorrect input try again.");
					continue;
				}
				break;
			}
			// exit
			if (choice == 0) {
				System.out.println("Thank you, goodbye!");
				System.exit(0);
			}
			// read reference string
			if (choice == 1) {
				readReferenceString();
			}
			// generate reference string
			if (choice == 2) {
				generateReferenceString();
			}
			// try catch for reading generated or read-in reference string
			if (choice == 3) {
				try {
					System.out.println("Reference string is: ");
					displayReferenceString();
				} catch (NullPointerException e) {
					System.out.println("Please generate or read in reference string first.");
				}
			}
			// fifo
			if (choice == 4) {
				simulateFIFO();
			}
			// opt
			if (choice == 5) {
				simulateOPT();
			}
			// lru
			if (choice == 6) {
				simulateLRU();
			}
			// lfu
			if (choice == 7) {
				simulateLFU();
			}
		}
	}

	// simulates least frequently used
	private static void simulateLFU() {
		// TODO Auto-generated method stub
		// frame input number
		clearFaultsVictims();
		System.out.println("Enter the number of physical frames: ");
		int framesNum = scanner.nextInt();
		// generate frame data and create frames array
		generateFrameData(nums.length, framesNum);
		createFrames(framesNum);
		System.out.print("Reference String ");
		// display reference string
		displayReferenceString();
		System.out.println();
		printFrameData(0);
		makeReferenceFrequencyDict();
		// for loop runs length of reference string
		for (int i = 0; i < nums.length; i++) {
			// format output
			System.out.println("Enter any input to continue");
			String throwaway = scanner.next();
			System.out.print("Reference String ");
			displayReferenceString();
			System.out.println();
			// make lfu reference on this reference string number
			makeLFUReference(i);
			// print frame data at this iteration
			printFrameData(i);

		}
	}

	private static void makeLFUReference(int it) {
		// variables
		int reference = nums[it];
		boolean present = false;
		boolean space = false;
		int openSpace = -1;
		// if reference in temp array do nothing
		for (int i = 0; i < frames.length; i++) {
			if (frames[i] == reference) {
				present = true;
				break;
			}
		}
		// if reference not in temp array
		if (!present) {
			// add fault to fault array
			faults[it] = 1;
			// loop through frames and find open space if one exists
			for (int i = 0; i < frames.length; i++) {
				if (frames[i] == -1) {
					space = true;
					openSpace = i;
					break;
				}
			}
			// if there is a space in frames
			if (space) {
				// set open space equal to reference int in frames and temp
				frames[openSpace] = nums[it];
				// tempArray[openSpace] = nums[it];
			} else {
				// if there is no space in frames
				int lowestIndex = 0;
				// loop through frames and consult reference frequency dictionary to find least
				// used frame
				for (int i = 0; i < frames.length; i++) {
					if (refFreqDict.get(frames[i]) < refFreqDict.get(frames[lowestIndex])) {
						lowestIndex = i;
					}
				}
				// add contents of least used frame to victims
				victims[it] = frames[lowestIndex];
				// set frame to contain reference
				frames[lowestIndex] = nums[it];
			}
		}
		// add current frames to frame data
		for (int i = 0; i < frames.length; i++) {
			frameData[i][it] = frames[i];
		}
	}

	// make reference frequency dict
	private static void makeReferenceFrequencyDict() {
		// this method populates the reference frequency dictionary
		// this creates a key value pair for each number in the reference string
		// and its frequency
		// this uses a common algorithm to get the frequency of appearances
		// for a value in an array of values
		refFreqDict = new HashMap<Integer, Integer>();
		boolean visited[] = new boolean[nums.length];
		Arrays.fill(visited, false);
		// for loop runs through length of references
		for (int i = 0; i < nums.length; i++) {
			if (visited[i] == true) {
				continue;
			}
			// set counter to 1
			int count = 1;
			// for loop runs through references internally
			for (int j = i + 1; j < nums.length; j++) {
				// if reference appears again
				if (nums[i] == nums[j]) {
					visited[j] = true;
					// count is incremented
					count++;
				}
			}
			// puts reference and its frequency into the dictionary
			refFreqDict.put(nums[i], count);
		}
	}

	// simulate least recently used
	private static void simulateLRU() {
		// TODO Auto-generated method stub
		clearFaultsVictims();
		// get number of frames
		System.out.println("Enter the number of physical frames: ");
		int framesNum = scanner.nextInt();
		// generate frame data method call
		generateFrameData(nums.length, framesNum);
		// creates frames
		createFrames(framesNum);
		// displays reference string
		System.out.print("Reference String ");
		displayReferenceString();
		System.out.println();
		// print frame data
		printFrameData(0);
		generateReferenceRecencyDictArr();
		for (int i = 0; i < nums.length; i++) {
			System.out.println("Enter any input to continue");
			String throwaway = scanner.next();
			System.out.print("Reference String ");
			// print reference string
			displayReferenceString();
			System.out.println();
			// make least recently used reference
			makeLRUReference(i);
			printFrameData(i);

		}
	}

	// make least recently used reference
	private static void makeLRUReference(int it) {
		// variables
		boolean present = false;
		boolean space = false;
		// for loop loops through frames
		for (int i = 0; i < frames.length; i++) {
			// if reference is in frame mark present
			if (frames[i] == nums[it]) {
				present = true;
				break;
			}
		}
		// if not present
		if (!present) {
			// add fault to fault array
			faults[it] = 1;
			// for loop through frames
			for (int i = 0; i < frames.length; i++) {
				// if empty space add reference to empty space
				if (frames[i] == -1) {
					space = true;
					frames[i] = nums[it];
					break;
				}
			}
			// if no empty space
			if (!space) {
				// holder for highest index
				int highestIndex = 0;
				// loop through frames
				for (int i = 0; i < frames.length; i++) {
					// if reference recency value is higher than highest set new highest
					if (refRecDictArr[it].get(frames[i]) > refRecDictArr[it].get(frames[highestIndex])) {
						highestIndex = i;
					}
				}
				// add frame to victim
				victims[it] = frames[highestIndex];
				// add reference to the frame that held the highest valued frequency
				frames[highestIndex] = nums[it];
			}
		}
		// add frames to frame data
		for (int i = 0; i < frames.length; i++) {
			frameData[i][it] = frames[i];
		}
	}

	// generate reference recency dictionary array
	public static void generateReferenceRecencyDictArr() {
		// array of dictionaries
		refRecDictArr = new HashMap[nums.length];
		// loop through length of array and intialize each dictionary
		for (int i = 0; i < refRecDictArr.length; i++) {
			refRecDictArr[i] = new HashMap<Integer, Integer>();
		}
		// put the first reference with a value of zero in the first dict
		refRecDictArr[0].put(nums[0], 0);
		// loop through references
		for (int i = 1; i < nums.length; i++) {
			// loop through keys in previous dictionary and add to current dictionary
			for (Integer key : refRecDictArr[i - 1].keySet()) {
				refRecDictArr[i].put(key, refRecDictArr[i - 1].get(key));
			}
			// add current reference to dictionary with value of -1 (to be incremented to 0
			// immediately)
			refRecDictArr[i].put(nums[i], -1);
			// increment value of each entry in dictionary
			for (Integer key : refRecDictArr[i].keySet()) {
				refRecDictArr[i].put(key, refRecDictArr[i].get(key) + 1);
			}
		}
	}

	// simulate optimal replacement algorithm
	private static void simulateOPT() {
		// TODO Auto-generated method stub
		clearFaultsVictims();
		// get frames num
		System.out.println("Enter the number of physical frames: ");
		int framesNum = scanner.nextInt();
		// generate frame data table and create frames
		generateFrameData(nums.length, framesNum);
		createFrames(framesNum);
		// display reference string
		System.out.print("Reference String ");
		displayReferenceString();
		System.out.println();
		// output first frame data
		printFrameData(0);
		// generate reference proximity dictionary array
		generateReferenceProximityDictArr();
		System.out.println();
		for (int i = 0; i < nums.length; i++) {
			System.out.println("Enter any input to continue");
			String throwaway = scanner.next();
			// output
			System.out.print("Reference String ");
			displayReferenceString();
			System.out.println();
			// make opt reference
			makeOPTReference(i);
			printFrameData(i);
		}
	}

	// make optimal replacement algorithm reference
	private static void makeOPTReference(int it) {
		// variables
		boolean present = false;
		boolean space = false;
		// for loop through frames
		for (int i = 0; i < frames.length; i++) {
			// if reference is present in frames mark present and break
			if (frames[i] == nums[it]) {
				present = true;
				break;
			}
		}
		// if not present
		if (!present) {
			// add fault to faul array
			faults[it] = 1;
			// for loop through frames
			for (int i = 0; i < frames.length; i++) {
				// if frame is empty fill with reference
				if (frames[i] == -1) {
					space = true;
					frames[i] = nums[it];
					break;
				}
			}
			// if there are no empty frames
			if (!space) {
				// holder for highest index
				int highestIndex = 0;
				// for loop through length of frames
				for (int i = 0; i < frames.length; i++) {
					// if the proximity is higher at this frame than current highest set highest to
					// this frame
					if (refProxDictArr[it].get(frames[i]) > refProxDictArr[it].get(frames[highestIndex])) {
						highestIndex = i;
					}
				}
				// add frame to victims
				victims[it] = frames[highestIndex];
				// set highest index frame to the new reference
				frames[highestIndex] = nums[it];
			}
		}
		// add current frames to frame data
		for (int i = 0; i < frames.length; i++) {
			frameData[i][it] = frames[i];
		}
	}

	// generate reference proximity dictionary array
	private static void generateReferenceProximityDictArr() {
		// this is an array of dictionaries
		refProxDictArr = new HashMap[nums.length];
		// loop through array initializing each dictionary
		for (int i = 0; i < refProxDictArr.length; i++) {
			refProxDictArr[i] = new HashMap<Integer, Integer>();
		}
		// loop through nums
		for (int i = 0; i < nums.length; i++) {
			// internal second loop through nums
			for (int j = 0; j <= i; j++) {
				// put each reference in dictionary with the distance to its next occurrence at
				// that time
				refProxDictArr[i].put(nums[j], generateDistanceToNextOccurrence(i, nums[j]));
			}
		}
	}

	// generate distance to next occurrence method
	private static int generateDistanceToNextOccurrence(int index, int target) {
		// distance holder
		int distance = 0;
		// for loop through references from index
		for (int i = index + 1; i < nums.length; i++) {
			// increment distance
			distance++;
			// if at target break
			if (target == nums[i]) {
				break;
			}
			if (i == nums.length - 1) {
				distance++;
			}
		}
		return distance;
	}

	// create frames method
	private static void createFrames(int x) {
		// integer array will hold frames
		frames = new int[x];
		// loop through frames and fill with -1
		for (int i = 0; i < x; i++) {
			frames[i] = -1;
		}
	}

	// generate frame data method
	private static void generateFrameData(int referenceStringLength, int numOfPFrames) {
		// array of arrays, number of frames x length of reference
		frameData = new int[numOfPFrames][referenceStringLength];
		for (int i = 0; i < numOfPFrames; i++) {
			for (int j = 0; j < referenceStringLength; j++) {
				// fill framedata with -1
				frameData[i][j] = -1;
			}
		}
	}

	// print frame data formatted correctly
	private static void printFrameData(int iterations) {
		// for loop through frames
		for (int i = 0; i < frames.length; i++) {
			// print frame and number
			System.out.print("Physical frame " + i);
			for (int j = 0; j < nums.length; j++) {
				System.out.print("    ");
				// print data
				if (frameData[i][j] == -1) {
					System.out.print("x");
				} else {
					System.out.print(frameData[i][j]);
				}
			}
			System.out.println();
		}
		// print page faults
		System.out.print("Page faults     ");
		for (int i = 0; i < faults.length; i++) {
			if (faults[i] != 0) {
				System.out.print("    " + "F");
			} else {
				System.out.print("     ");
			}
		}
		System.out.println();
		// print victims
		System.out.print("Victim frames   ");
		for (int i = 0; i < victims.length; i++) {
			if (victims[i] != -1) {
				System.out.print("    " + victims[i]);
			} else {
				System.out.print("    x");
			}
		}
		System.out.println();
		System.out.println("--------------------------------------------------------------------");
	}

	// simulate first in first out
	private static void simulateFIFO() {
		// TODO Auto-generated method stub
		clearFaultsVictims();
		System.out.println("Enter the number of physical frames: ");
		int framesNum = scanner.nextInt();
		// generate frame data and create frames
		generateFrameData(nums.length, framesNum);
		createFrames(framesNum);
		// for loop through references
		for (int i = 0; i < nums.length; i++) {
			System.out.print("Reference String ");
			displayReferenceString();
			System.out.println();
			// make fifo reference for iteration
			makeFIFOReference(i);
			printFrameData(i);
			System.out.println("Enter any input to continue");
			String throwaway = scanner.next();
		}
	}

	// make fifo references
	private static void makeFIFOReference(int it) {
		// reference is the iteration index of the references
		int reference = nums[it];
		boolean present = false;
		// create temp array one size larger than frames
		int[] tempArray = new int[frames.length + 1];
		// fill temp array with data from frames
		for (int i = 0; i < tempArray.length - 1; i++) {
			tempArray[i] = frames[i];
		}
		// set last number in temp array to -1
		tempArray[tempArray.length - 1] = -1;
		// if reference in temp array do nothing
		for (int i = 0; i < tempArray.length - 1; i++) {
			if (tempArray[i] == reference) {
				present = true;
				break;
			}
		}
		// if reference not in temp array
		if (!present) {
			faults[it] = 1;
			// shift each temp array item down 1
			for (int i = tempArray.length - 1; i > 0; i--) {
				tempArray[i] = tempArray[i - 1];
			}
			// insert reference at position 0
			tempArray[0] = reference;
			if (it != 0) {
				victims[it] = tempArray[tempArray.length - 1];
			}
		}
		// set frame data to data in temp array now
		for (int i = 0; i < frames.length; i++) {
			frames[i] = tempArray[i];
			// add frames to frame data
			frameData[i][it] = frames[i];
		}
	}

	public static void readReferenceString() {
		// take input from user
		System.out.print("Enter the length of the string: ");
		int len = scanner.nextInt();
		System.out.println();
		// variables
		nums = new int[len];
		victims = new int[len];
		faults = new int[len];
		// for loop through length
		for (int i = 0; i < len; i++) {
			// infinite loop to verify input
			while (true) {
				try {
					Scanner scanner = new Scanner(System.in);
					System.out.print("Enter the value: ");
					nums[i] = scanner.nextInt();
					break;
				} catch (InputMismatchException e) {
					System.out.println("Incorrect input, please use an integer value");

				}
			}
			System.out.println();
			// fill victims with -1 and faults with binary no
			victims[i] = -1;
			faults[i] = 0;
		}
	}

	// generate reference string method
	public static void generateReferenceString() {
		// get user input for length
		System.out.println("Enter the length of the string: ");
		int len = scanner.nextInt();
		Random random = new Random();
		// variables
		nums = new int[len];
		victims = new int[nums.length];
		faults = new int[nums.length];
		// for loop through length
		for (int i = 0; i < nums.length; i++) {
			// fill references with random digits
			nums[i] = random.nextInt(10);
			// fill victims and faults
			victims[i] = -1;
			faults[i] = 0;
		}
	}

	// display reference string method
	public static void displayReferenceString() {
		System.out.print("   ");
		// for loop through references
		for (int i = 0; i < nums.length; i++) {
			System.out.print(nums[i] + "    ");
		}
	}

	private static void clearFaultsVictims() {
		for (int i = 0; i < faults.length; i++) {
			faults[i] = 0;
			victims[i] = -1;
		}
	}

}
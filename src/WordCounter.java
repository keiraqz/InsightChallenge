import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Keira Qi Zhou
 * @data May 11, 2015
 */

public class WordCounter {

	public static class Results {
		static HashMap<String, Integer> wordCountMap = new HashMap<String, Integer>();
	}

	public static void main(String[] args) {
		final ArrayList<String> allFiles = getFiles("./wc_input", ".txt");
		final int filesSize = allFiles.size();
		final int numberOfProcessors = filesSize / 4 + 1;
		// Used for output purposes
		final HashMap<Integer, String> processingStatus = new HashMap<Integer, String>();
		for (int i = 1; i <= 10; i++)
			processingStatus.put((int) (filesSize * (i / 10d)), i + "0% ("
					+ (int) (filesSize * (i / 10d)) + " out of " + filesSize
					+ ").");

		// Threading code
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < numberOfProcessors; ++i) {
			threads.add((new Thread() {
				int core;

				public void run() {
					try {
						for (int j = 0; j + core < filesSize; j += numberOfProcessors) {
							if (processingStatus.containsKey(j + core)) {
								System.out.println(processingStatus.get(j
										+ core));
							}
							countWordFunction(allFiles.get(j + core));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				private Thread initialize(int core) {
					this.core = core;
					return this;
				}
			}).initialize(i));
			threads.get(i).start();
		}
		for (int i = 0; i < numberOfProcessors; ++i) {
			try {
				threads.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			BufferedWriter writeOutput = new BufferedWriter(new FileWriter(
					new File("./wc_output/wc_result.txt")));
			// Sort words in alphabetical order
			SortedSet<String> sortedTokens = new TreeSet<String>(
					Results.wordCountMap.keySet());
			for (String key : sortedTokens) {
				// Write the output
				writeOutput.append(key).append("\t")
						.append(String.valueOf(Results.wordCountMap.get(key)))
						.append("\n");
			}

			writeOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static synchronized void countWordFunction(String f)
			throws IOException {
		Scanner readFile;
		readFile = new Scanner(new File(f));
		while (readFile.hasNextLine()) {
			String currentLine = readFile.nextLine();
			currentLine = currentLine.toLowerCase();
			// Remove punctuation
			currentLine = currentLine.replaceAll(
					"[\\[\\]\\{\\}\\/,_\"-.!?:;)(’<>]", "");
			String[] tokens = currentLine.trim().split(" ");
			// Count words
			for (int i = 0; i < tokens.length; i++) {
				if (Results.wordCountMap.containsKey(tokens[i])) {
					Results.wordCountMap.put(tokens[i],
							Results.wordCountMap.get(tokens[i]) + 1);
				} else {
					Results.wordCountMap.put(tokens[i], 1);
				}
			}
		}
		readFile.close();

	}

	public static ArrayList<String> getFiles(String folder, String suffix) {
		File dir = new File(folder);
		ArrayList<String> Files = new ArrayList<String>();
		for (File f : dir.listFiles()) {
			if (f.isFile() && f.getName().endsWith(suffix)) {
				Files.add(f.getAbsolutePath());
			} else if (f.isDirectory())
				Files.addAll(getFiles(f.getAbsolutePath(), suffix));
		}
		return Files;
	}
}

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;


public class RunMedian {

	public static void main(String[] args) {
		runningMedianFunction("./wc_input", ".txt");
	}
	
	private static void runningMedianFunction(String folder, String suffix) {
		try {
			BufferedWriter writeOutput = new BufferedWriter(new FileWriter(
					new File("./wc_output/med_result.txt")));
			ArrayList<Double> runningMedian = new ArrayList<Double>();
			ArrayList<Double> lineLengthRecord = new ArrayList<Double>();
			Scanner readFile;

			// Sort files by name
			File folderFile = new File(folder);
			File[] fileList = folderFile.listFiles();
			Arrays.sort(fileList);

			// Lists only files since we have applied file filter
			for (File f : fileList) {
//				System.out.println(f.getName());
				if (f.isFile() && f.getName().endsWith(suffix)) {
					readFile = new Scanner(f);
					while (readFile.hasNextLine()) {
						String currentLine = readFile.nextLine();
						currentLine = currentLine.toLowerCase();
						// Remove punctuation
						currentLine = currentLine.replaceAll(
								"[\\[\\]\\{\\}\\/,_\"-.!?:;)(’<>]", "");
						String[] tokens = currentLine.trim().split(" ");
						lineLengthRecord.add((double) tokens.length);
						// Get the median
						Collections.sort(lineLengthRecord);
						if (lineLengthRecord.size() % 2 == 0) {
							// Even number of elements
							runningMedian
									.add((lineLengthRecord.get(lineLengthRecord
											.size() / 2) + lineLengthRecord
											.get(lineLengthRecord.size() / 2 - 1)) / 2);
						} else {
							// Odd number of elements
							runningMedian.add(lineLengthRecord
									.get((lineLengthRecord.size() - 1) / 2));
						}
					}
				}
			}
			for (int i = 0; i < runningMedian.size(); i++)
				writeOutput.append(String.valueOf(runningMedian.get(i)))
						.append("\n");
			writeOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

package plagdetect;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class PlagiarismDetector implements IPlagiarismDetector {
	int number;
	Map<String, HashSet<String>> filestoNgrams= new HashMap<String, HashSet<String>>();
	Map<String, HashSet<ArrayList<String>>> compare= new HashMap<String, HashSet<ArrayList<String>>>(); // string is filename
	public PlagiarismDetector(int n) {
		number = n;
	}
	
	@Override
	public int getN() {
		
		return number;
	}

	@Override
	public Collection<String> getFilenames() {
		return filestoNgrams.keySet();
	}

	@Override
	public Collection<String> getNgramsInFile(String filename) {
		return filestoNgrams.get(filename);
	}

	@Override
	public int getNumNgramsInFile(String filename) {
		return filestoNgrams.get(filename).size();
	}

	@Override
	public Map<String, HashSet<ArrayList<String>>> getResults() {
		
		return this.compare;
	}
	
	public int compareNgramsInCommon(String f1, String f2) {
		if(!this.compare.containsKey(f1)||!this.compare.containsKey(f2)){// Find if f1 and f2 exist in compare map. If not then return 0.
					return 0;												// declare a counter. for each item in set 1 and in set 2 if Strings are common then update and return counter.
		} else {
			int counter =0;
			HashSet<ArrayList<String>> s1 = this.compare.get(f1);
			HashSet<ArrayList<String>> s2 = this.compare.get(f2);
			for(ArrayList<String> a: s1){
				if(s2.contains(a)) {
					counter++;
				}
			}
			return counter;
		}
	}
	
	@Override
	public void readFile(File file) throws IOException {
		Scanner scan = new Scanner(file);
		HashSet<ArrayList<String>> set = new HashSet<>();
		while(scan.hasNextLine()) {
			String[] store=scan.nextLine().split(" ");
			for(int i=0;i<store.length-number;i++) {
				String ngrams = store[i]+store[i+1]+store[i+2];
				set.add(ngrams);
			}
		}
		scan.close();
		filestoNgrams.put(file.getName()s, set);
		
		for(Map.Entry<String, HashSet<String>> d1: filestoNgrams.entrySet()) {
			for(Map.Entry<String, HashSet<String>> d2: filestoNgrams.entrySet()) {
				for(String grams1: d1.getValue()) {
					String d1Name = d1.getKey();
					String d2Name = d2.getKey();
					if(d2.getValue().contains(grams1)) {
						System.out.println(this.getResults());
						int n1 = this.getResults().get(d1Name).get(d2Name);
						this.getResults().get(d1Name).put(d2Name, n1+1);
						this.getResults().get(d2Name).put(d1Name, n1+1);

					}
				}
			}
		}
	}

	@Override
	public int getNumNGramsInCommon(String file1, String file2) {
		return compare.get(file1).get(file2);
	}

	@Override
	public Collection<String> getSuspiciousPairs(int minNgrams) {
		LinkedList<String> Sus = new LinkedList<>();
		if()
		return Sus;
	}

	@Override
	public void readFilesInDirectory(File dir) throws IOException {
		// delegation!
		// just go through each file in the directory, and delegate
		// to the method for reading a file
		for (File f : dir.listFiles()) {
			readFile(f);
		}
	}
}

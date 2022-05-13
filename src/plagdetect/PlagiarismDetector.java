package plagdetect;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class PlagiarismDetector implements IPlagiarismDetector {
	
	public Map<String, Set<String>> map = new HashMap<>();
	public Map<String, Map<String, Integer>> full = new HashMap<>();
	public int n = 3;
	private int ngrams;
	public PlagiarismDetector(int n) {
		// TODO implement this method
		ngrams = n;
	}
	
	@Override
	public int getN() {
		// TODO Auto-generated method stub
		
		return ngrams;
	}

	@Override
	public Collection<String> getFilenames() {
		// TODO Auto-generated method stub
		Collection<String> col = new HashSet<>();
		for (Map.Entry<String,Set<String>> entry : map.entrySet()) {
			String str = entry.getKey();
			col.add(str);
		}
		
		return col;
	}

	@Override
	public Collection<String> getNgramsInFile(String filename) {
		// TODO Auto-generated method stub
		return map.get(filename);
	}

	@Override
	public int getNumNgramsInFile(String filename) {
		// TODO Auto-generated method stub
		return map.get(filename).size();
	}

	@Override
	public Map<String, Map<String, Integer>> getResults() {
		// TODO Auto-generated method stub
		return full;
	}

	@Override
	public void readFile(File file) throws IOException {
		// TODO Auto-generated method stub
		// most of your work can happen in this method
		Scanner scan = new Scanner(file);
		String[] read;
		Set<String> set = new HashSet<>();
		while (scan.hasNextLine()) {
			String temp;
			temp = scan.nextLine();
			read = temp.split(" ");
			if (read.length >= ngrams) {
				for (int i = 0; i < read.length-ngrams+1; i++) {
					String sb = "";
					for (int j = i; j < i + ngrams; j++) {
						sb += read[j] + " ";
					}
					sb = sb.trim();
					set.add(sb);
				}
			}
		}
		map.put(file.getName(), set);
		
	}

	@Override
	public int getNumNGramsInCommon(String file1, String file2) {
		// TODO Auto-generated method stub
		Set<String> one = map.get(file1);
		Set<String> two = map.get(file2);
		int count = 0;
		for (String str : one) {
			if (file1.equals(file2)) {
				continue;
			}
			if (two.contains(str)) {
				count++;
			}
		}
		//System.out.println(count);
		return count;
	}

	@Override
	public Collection<String> getSuspiciousPairs(int minNgrams) {
		// TODO Auto-generated method stub
		Set<String> suspairs = new HashSet<>();
		for (String str: full.keySet()) {
			for (String st1: full.keySet()) {
				if (full.get(str).get(st1) >= minNgrams) {
					String temp = "";
					if (str.compareTo(st1) > -1) {
						temp += str + " " + st1 + " " + full.get(str).get(st1);
						suspairs.add(temp);
					} else { temp += st1 + " " + str + " " + full.get(str).get(st1);
					} suspairs.add(temp);
					
					
				}
			}
			
		}
		
		return suspairs;
	}

	@Override
	public void readFilesInDirectory(File dir) throws IOException {
		// delegation!
		// just go through each file in the directory, and delegate
		// to the method for reading a file
		
		for (File f : dir.listFiles()) {
			readFile(f);
		}
		for (Map.Entry<String,Set<String>> f1 : map.entrySet()) {
			Map<String, Integer> temp1 = new HashMap<>();
			String file1 = f1.getKey();
			for (Map.Entry<String,Set<String>> f2 : map.entrySet()) {
				String file2 = f2.getKey();
				int temp = getNumNGramsInCommon(file1, file2);
				temp1.put(file2, temp);
			}
			full.put(file1, temp1);
		}
	}
}



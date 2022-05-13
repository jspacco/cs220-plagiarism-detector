package plagdetect;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class PlagiarismDetector implements IPlagiarismDetector {
	
	public Map<String, Set<String>> map  = new HashMap<>();
	public int n;
	public Map<String, Map<String, Integer>> map2 = new HashMap<>();
	public int minGrams;
	
	public PlagiarismDetector(int nn) {
		// TODO implement this method
		n = nn;
	}
	
	@Override
	public int getN() {
		// TODO Auto-generated method stub
		return n;
	}

	@Override
	public Collection<String> getFilenames() {
		// TODO Auto-generated method stub
		return map.keySet();
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
		return map2;
	}

	@Override
	public void readFile(File file) throws IOException {
		// TODO Auto-generated method stub
		// most of your work can happen in this method
		Set<String> set = new HashSet<>();
		Map<String, Integer> map3 = new HashMap<>();
		Scanner sc = new Scanner(file);
		String[] read;
		while(sc.hasNextLine()) {
			String temp;
			temp = sc.nextLine();
			read = temp.split(" ");
			if(read.length >= n) {
				for(int i = 0; i<read.length-n+1; i++) {
					String string = "";
					for(int j = i; j<i + n; j++) {
					string += read[j] + " ";
					}
					string = string.trim();
					set.add(string);
				}
			}
		}
		sc.close();
		map.put(file.getName(), set);
		
		if(map2.isEmpty()) {
			map2.put(file.getName(), new HashMap<>());
		}else {
			int num = 0;
			for(String string: map2.keySet()) {
				for(String s: map.keySet()) {
					for(String str: map.get(file.getName())) {
						if(map.get(s).equals(str)) {
							num++;
						}
					}
				}
				map3.put(string, num);
				map2.get(string).put(file.getName(), num);
			}
			map2.put(file.getName(), map3);
		}
	}


	@Override
	public int getNumNGramsInCommon(String file1, String file2) {
		// TODO Auto-generated method stub
		return map2.get(file1).get(file2);
	}

	@Override
	public Collection<String> getSuspiciousPairs(int minNgrams) {
		// TODO Auto-generated method stub
		minGrams = minNgrams-1;
		Set<String> spairs = new HashSet<>();
		for(String s : map2.keySet()) {
			for(String ss : map2.get(s).keySet()) {
				if(map2.get(s).get(ss) > minGrams) {
					String file1;
					String file2;
					if(s.compareTo(ss) < 0) {
						file1 = s;
						file2 = ss;
					}else {
						file1 = ss;
						file2 = s;
					}
					spairs.add(file1 + " " + file2 + " " + map2.get(s).get(ss));
				}
			}
		}
		return spairs;
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

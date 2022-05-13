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
	
	public int n;
	public Map<String, Set<String>> allFiles = new HashMap<>();
	public Map<String, Map<String, Integer>> results = new HashMap<>();
	public int mingrams;
	
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
		return allFiles.keySet();
	}

	@Override
	public Collection<String> getNgramsInFile(String filename) {
		// TODO Auto-generated method stub
		/*
		 * for(String s : allFiles.keySet()) {
			for(String t : allFiles.get(s)) {
				System.out.println(t);
			}
		}
		 */
		return allFiles.get(filename);
	}

	@Override
	public int getNumNgramsInFile(String filename) {
		// TODO Auto-generated method stub
		return allFiles.get(filename).size();
	}

	@Override
	public Map<String, Map<String, Integer>> getResults() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void readFile(File file) throws IOException {
		// TODO Auto-generated method stub
		// most of your work can happen in this method
		Scanner scan = new Scanner(file);
		Set<String> nGrams = new HashSet<String>();
		while(scan.hasNextLine()){
			String cur = scan.nextLine();
			String[] curArr = cur.split(" ");
			if(!(curArr.length < n)) {
			for(int i = 0; i < curArr.length-n+1; i++) {
			 String str = "";
			 for(int j = i; j < i + n; j++) {
				 str += curArr[j] + " ";
			 }
			 str = str.trim();
			 nGrams.add(str);
			}
			}
			
		}
		scan.close();
		allFiles.put(file.getName(), nGrams);
		
		Map<String, Integer> nums = new HashMap<>();
		if(results.isEmpty()) {
			results.put(file.getName(), new HashMap<>());
		} else {
			for(String s: results.keySet()) {
				int c = 0;
				for(String ng : allFiles.get(s)) {
					for(String gn : allFiles.get(file.getName())) {
						if(ng.equals(gn)) {
							c++;
						}
					}
				}
				nums.put(s, c);
				results.get(s).put(file.getName(),c);
				
			}
			results.put(file.getName(), nums);
		}
		
		
		
	}

	@Override
	public int getNumNGramsInCommon(String file1, String file2) {
		// TODO Auto-generated method stub
  	    return results.get(file1).get(file2);
	}

	@Override
	public Collection<String> getSuspiciousPairs(int minNgrams) {
		// TODO Auto-generated method stub
		mingrams = minNgrams - 1;
		Set<String> sus = new HashSet<>();
		for(String s : results.keySet()) {
			for(String t : results.get(s).keySet()) {
				if(results.get(s).get(t) > mingrams) {
					String f1 = "";
					String f2 = "";
					if(s.compareTo(t) < 0) {
						f1 = s;
						f2 = t;
					} else {
						f1 = t;
						f2 = s;
					}
					sus.add(f1 + " " + f2 + " " + results.get(s).get(t));
				}
			}
		}
		return sus;
	}

	@Override
	public void readFilesInDirectory(File dir) throws IOException {
		// delegation!
		// just go through each file in the directory, and delegate
		// to the method for reading a file
		int y = 0;
		for (File f : dir.listFiles()) {
			y++;
			readFile(f);
			System.out.println("I'm reading" + y);
		}
	}
}

package plagdetect;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class PlagiarismDetector implements IPlagiarismDetector {
	
	String copy = "";
	int n;
	Map<String, Set<String>> map = new HashMap<>();
	
	Map<String, Map<String, Integer>> results = new HashMap<>();
	
	public PlagiarismDetector(int n) {
		// TODO implement this method
		this.n = n;
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
		return results;
	}

	@Override
	public void readFile(File file) throws IOException {
		// TODO Auto-generated method stub
		// most of your work can happen in this method
		
		Set<String> ngramFile = new HashSet<>();
		
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()) {
			String[] arr = scanner.nextLine().split(" ");
			if(n <= arr.length) {
				for(int i=0; i<arr.length +1 - n; i++) {
					String filler = "";
					for(int j=i; j<i+n; j++) {
						filler = filler + arr[j] + " ";
					}
					filler = filler.trim();
					ngramFile.add(filler);
				}
			}
		}
		
		map.put(file.getName(), ngramFile);
		
		
	}

	@Override
	public int getNumNGramsInCommon(String file1, String file2) {
		// TODO Auto-generated method stub
		return results.get(file1).get(file2);
	}

	@Override
	public Collection<String> getSuspiciousPairs(int minNgrams) {
		// TODO Auto-generated method stub
		String x = "";
		Collection<String> collect = new HashSet<>();

		for(String str : results.keySet()) {
			for(String str2 : results.keySet()) {
				if(results.get(str).get(str2) >= minNgrams) {
					if(str.compareTo(str2) < 0) {
						x = str + " " + str2 + " " + getNumNGramsInCommon(str, str2) + " ";
						x = x.trim();
						collect.add(x);
					} else if(str.compareTo(str2) > 0) {
						x = str2 + " " + str + " " + getNumNGramsInCommon(str, str2) + " ";
						x = x.trim();
						collect.add(x);
						
					} else if(str.compareTo(str2)==0) {
						x = str + " " + str2 + " " + getNumNGramsInCommon(str, str2) + " ";
						x = x.trim();
						collect.add(x);
					}
					
					//System.out.println(getNumNGramsInCommon(str, str2));
					
				}
			}
		}
				
		return collect;
	}

	@Override
	public void readFilesInDirectory(File dir) throws IOException {
		// delegation!
		// just go through each file in the directory, and delegate
		// to the method for reading a file
		
		
		for (File f : dir.listFiles()) {
			readFile(f);
		}
		
		for(String str : map.keySet()) {
			Map<String, Integer> smolmap = new HashMap<>();
			
			for(String strr: map.keySet()) {
				int count = 0;
				
				for(String x: map.get(str)) {
					if(map.get(strr).contains(x)) {
						count++;
					}
					//System.out.println(map.get(str).size());
					//System.out.println(map.get(strr).size());
				}
				smolmap.put(strr, count);
				//System.out.println(count + " " + str + " " + strr);
			}
			results.put(str, smolmap);
		}
		
	}
}

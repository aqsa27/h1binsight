import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


public class H1FileReader {
	
	HashMap<String,Integer> occupation = new HashMap<>();
	HashMap<String,Integer> state = new HashMap<>();
	private static final char DEFAULT_SEPARATOR = ';';
    private static final char DEFAULT_QUOTE = '"';
    
    
    
    
    public void createOccupationOutput(String path)
    {
    	try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
    		String newLine = null;
    		float percent ;
    		List<Integer> L = new ArrayList<>(occupation.values());
    		int sum = L.stream().mapToInt(Integer::intValue).sum();
    	
			bw.write("TOP_OCCUPATIONS;NUMBER_CERTIFIED_APPLICATIONS;PERCENTAGE");
			bw.newLine();
			LinkedHashMap<String, Integer> top10Occupations = getTop10Occupations();
			DecimalFormat df = new DecimalFormat("#.##");
			for(String Key:top10Occupations.keySet())
			{
				
				percent = ((Float.parseFloat(top10Occupations.get(Key).toString()))/sum)*100;
				double roundPercent = roundPercent(Double.parseDouble(df.format(percent)),1);
				newLine = Key+";"+top10Occupations.get(Key)+";"+roundPercent+"%";
				bw.write(newLine);
				bw.newLine();
			}
			


		} catch (IOException e) {

			e.printStackTrace();

		}
    	
    }
    
    
    public void createStateOutput(String path)
    {
    	try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
    		String newLine = null;
    		float percent ;
    		List<Integer> L = new ArrayList<>(state.values());
    		int sum = L.stream().mapToInt(Integer::intValue).sum();
    
			bw.write("TOP_STATES;NUMBER_CERTIFIED_APPLICATIONS;PERCENTAGE");
			bw.newLine();
			LinkedHashMap<String, Integer> top10States = getTop10States();
			DecimalFormat df = new DecimalFormat("#.##");
			for(String Key:top10States.keySet())
			{
				
				percent = ((Float.parseFloat(top10States.get(Key).toString()))/sum)*100;	
				double roundPercent = roundPercent(Double.parseDouble(df.format(percent)),1);
				newLine = Key+";"+top10States.get(Key)+";"+roundPercent+"%";
				
				bw.write(newLine);
				bw.newLine();
			}
			


		} catch (IOException e) {

			e.printStackTrace();

		}
    	
    }
    
    private static double roundPercent (double value, int precision) 
    {
	    int scale = (int) Math.pow(10, precision);
	    return (double) Math.round(value * scale) / scale;
	}
    
    public LinkedHashMap<String, Integer> getTop10Occupations()
    {
    	
    	List<Integer> l = new ArrayList<Integer>(occupation.values());
    	Collections.sort(l);
    	l = l.subList(l.size()-10,l.size());
    	HashMap<String, Integer> Sortedoccupation = new HashMap<>();	
    	for(String key : occupation.keySet())
    	{ 
    	  		if(l.contains(occupation.get(key)))
    	  		{
    	  			Sortedoccupation.put(key, occupation.get(key));
    	  		
    	  		} 
    	  	
  		
    	}

       	LinkedHashMap<String,Integer> topOccupation = sortHashMapByValues(Sortedoccupation);
  
   
    	return topOccupation;
    
    }
    
    public LinkedHashMap<String, Integer> getTop10States()
    {
    	
    	List<Integer> l = new ArrayList<Integer>(state.values());
    	Collections.sort(l);
    	l = l.subList(l.size()-10,l.size());
    	HashMap<String, Integer> SortedStates = new HashMap<>();	
    	for(String key : state.keySet())
    	{ 
    	  		if(l.contains(state.get(key)))
    	  		{
    	  			SortedStates.put(key, state.get(key));
    	  		
    	  		} 
    	  	
  		
    	}

       	LinkedHashMap<String,Integer> topStates = sortHashMapByValues(SortedStates);

    	return topStates;
    
    }
    
    
    public LinkedHashMap<String,Integer> sortHashMapByValues(
            HashMap<String,Integer> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Integer> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues,Collections.reverseOrder());
        Collections.sort(mapKeys,Collections.reverseOrder());

        LinkedHashMap<String,Integer> sortedMap =
            new LinkedHashMap<>();

        Iterator<Integer> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            int val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                int comp1 = passedMap.get(key);
                int comp2 = val;

                if (comp1==comp2) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
 	
	public void readFile(String path)
	{
	 String FILENAME = path;
	 try (BufferedReader br = new BufferedReader(new java.io.FileReader(path))) {

			String sCurrentLine;
			String FirstLine = br.readLine();
			List<String> columns = parseLine(FirstLine);
			int Cstatus=2,occup=22,WState=38;
			for(String name : columns)
			{
				if(name.equals("WORKSITE_STATE"))
					WState = columns.indexOf(name);
				if(name.equalsIgnoreCase("SOC_NAME"))
					occup = columns.indexOf(name);
				if(name.equals("CASE_STATUS"))
					Cstatus = columns.indexOf(name); 
			}
			
			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				setOccupation(sCurrentLine,Cstatus,occup,WState);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	 		
	}
	
	
	private static double round (double value, int precision) {
	    int scale = (int) Math.pow(10, precision);
	    return (double) Math.round(value * scale) / scale;
	}
	
	public void setOccupation(String line, int Cstatus, int occp, int WState)
	{
		
		List<String> Line = parseLine(line);
		String status;
		String occup;
		String st;
				
	
		if(Line.size()>37)
		{

		status = Line.get(Cstatus);
		occup = Line.get(occp);
		st = Line.get(WState);
	
		
		if(status.equals("CERTIFIED"))
		{
			if(occupation.containsKey(occup))
				occupation.put(occup, occupation.get(occup)+1);
			else
				occupation.put(occup, 1);
			
			if(state.containsKey(st))
				state.put(st, state.get(st)+1);
			else
				state.put(st, 1);
		}
		}
		
		
	}
	
	
	 public static List<String> parseLine(String cvsLine) {
	        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
	    }

	    public static List<String> parseLine(String cvsLine, char separators) {
	        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
	    }
	
	public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }
}

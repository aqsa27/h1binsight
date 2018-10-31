
public class h1BStats {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String input = args[0];
		
		String occupationOutput = args[1];
		String stateOutput = args[2];
		
		
		H1FileReader h1 = new H1FileReader();
		h1.readFile(input);
		h1.getTop10Occupations();
		h1.getTop10States();
		h1.createOccupationOutput(occupationOutput);
		h1.createStateOutput(stateOutput);
	}

}

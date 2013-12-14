/**
 * The mst class is the class which handles the input output of the program.
 * Creating graphs and calling the fibonacci heap and simple scheme methods.
 */
package mst;



public class Mst {

	
	public static void main(String[] args) {
		
		char mode = 'x';
		/*
		 * Create undirected graph to store the graph
		 */
		Graph mst00 = new Graph(); 
		
		/*
		 * If the no of arguments are three then randomly generate the graph
		 */
		
		if(args.length==3){
			/*
			 * Set connected as false to indicate that the graph is not connected. 
			 * It will be modified by the dfs method
			 */
			Boolean connected = false;
			
			System.out.println("Generating Graph");
			
			while(!connected){
				
				mst00.resetGraph();		//reset the graph ie clear the adjecency lists
				mode = 'r';				//Set mode to r
				/*
				 * Generate the graph
				 */
				mst00.generateGraph(mode, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
				
				System.out.println("Checking Connectivity");
				
				connected=mst00.checkIfConnected();	//Check if the graph is connected
				
				//mst00.getGraph();					//Print the graph
			}
			
				System.out.println("Calling Simple Primms");
			
				long startTime =System.currentTimeMillis();			//Start Time
				/*
				 * Call the prims's algorithm using the simple scheme
				 */
				mst00.primms();
				
				
				long endtime = startTime-System.currentTimeMillis();	//End Time

				System.out.println("Calling Fibonacci Primms");
				
				startTime =System.currentTimeMillis();					//Start Time
				/*
				 * Call the primm's Algorithm using the fibonacci heap
				 */
				mst00.fprimms();
				long endtime2 = startTime-System.currentTimeMillis();

				/*
				 * Print out the times for both algorithms
				 */
			//	System.out.println("Simple Scheme Execution Time:"+endtime);
			//	System.out.println("Fibonacci Execution Time:"+endtime2);
				System.exit(0);
				
			
			
			
		
		}
		
		else if(args.length==2){
			/*
			 * Set connected as false until the graph is determined to connect
			 */
			Boolean connected = false;
			while(!connected){
				
				mst00.resetGraph();						//Reset the graph
				/*
				 * Set mode
				 */
				mode = args[0].toCharArray()[1];
				/*
				 * Generate Graph
				 */
				mst00.generateGraph(mode, args[1]);
				/*
				 * Check if Connected
				 */
				connected=mst00.checkIfConnected();
				
				if(!connected){
					System.out.println("The graph is not connected, exiting ...");
					System.exit(-1);
				}
			}
				
				//mst00.getGraph();						//Print Graph
				
				long startTime =System.currentTimeMillis();	//Start Time
				/*
				 * Call methods as determined by mode
				 */
				if(mode=='s'){
					mst00.primms();
					System.out.println("Simple Scheme Execution Time:"+(startTime-System.currentTimeMillis()));
				}
				else if(mode=='f'){
					mst00.fprimms();
					System.out.println("Fibonacci Execution Time:"+(startTime-System.currentTimeMillis()));
				}
				System.exit(0);
			
			

		}
		
		else{
			/*
			 * Handle deficient arguments
			 */
			System.out.print("Error: Not enough arguments\n" +
					"Usage: \tjava mst -r <no of vertices> <density> \n \t\tor\n" +
					"\tjava mst [-s][-f] <filename>");
			System.exit(1);
		}

	}

}

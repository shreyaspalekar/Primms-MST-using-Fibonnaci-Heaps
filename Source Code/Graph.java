package mst;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import mst.FibonacciHeap.Node;

public class Graph{
	/*
	 * The variable adjecencies store the graph in a adjecency list format implemented using HashMap
	 * The first key is the first vertex , the inner  key is the second vertex and it is associated 
	 * to the cost
	 */
	private HashMap<Integer,HashMap<Integer,Integer>> adjecencies = new HashMap<Integer,HashMap<Integer,Integer>>();
	/*
	 * vertices stores the visited nodes in dfs and primm's methods
	 */
	private HashMap<Integer,Boolean> vertices = new HashMap<Integer,Boolean>();
	/*
	 * The no of vertices in the graph
	 */
	private int noOfVertices = 0;
	
	/*
	 * The following method returns the unvisited neighbours of a vertex in the primm's algorithm
	 */
	private ArrayList<Integer> getUnvisitedNeighbours(int v){
		/*
		 * Create an arraylist to return the unvisited neighbours
		 */
		ArrayList<Integer> neighbours=new ArrayList<Integer>();
		/*
		 * Create an iterator to use the adjecency list
		 */
		Iterator<Integer> iter = this.adjecencies.keySet().iterator();
		
		int k =0;
		int j =0;
		/*
		 * The following  loop checks wether the outer keys have any inner keys that are
		 * equal to the vertex and if they are not visited then add to the neighbour list 
		 */
		
		/*while(iter.hasNext()){
			k=iter.next();
			if(adjecencies.get(k).containsKey(v)&&!vertices.containsKey(k))	//check if outer keys contain vertex as inner key
				neighbours.add(k);
		}*/
		while(iter.hasNext()){
			k=iter.next();
			if(!vertices.containsKey(k))
				if(adjecencies.get(k).containsKey(v))	//check if outer keys contain vertex as inner key
					neighbours.add(k);
		}
		/*
		 * The following block checks whether the vertex is an outer key and if so adds
		 * any unvisited inner keys to the neighbour list
		 */
		
		if(adjecencies.containsKey(v)){
			Iterator<Integer> iter2 = this.adjecencies.get(v).keySet().iterator(); //Get iterator
			while(iter2.hasNext()){
				j=iter2.next();
				if(!vertices.isEmpty()&&!vertices.containsKey(j))	//Check if inner keys are visited
					neighbours.add(j);
			}
		}

			return neighbours;
	}
	/*
	 * The following method is used by the DFS search to find out the unvisited vertices in the search
	 */
	private int getAdjUnvisitedVertex(int v)
    {
		/*
		 * Create an arraylist to return the unvisited neighbours
		 */
		ArrayList<Integer> neighbours=new ArrayList<Integer>();
		/*
		 * Create an iterator to use the adjecency list
		 */
		Iterator<Integer> iter = this.adjecencies.keySet().iterator();
		
		
		int k =0;
		/*
		 * The following  loop checks wether the outer keys have any inner keys that are
		 * equal to the vertex and if they are not visited then add to the neighbour list 
		 */
		while(iter.hasNext()){
			k=iter.next();
			if(adjecencies.get(k).containsKey(v)&&!vertices.containsKey(k))
				neighbours.add(k);
			
		}
		
		/*
		 * The following block checks whether the vertex is an outer key and if so adds
		 * any unvisited inner keys to the neighbour list
		 */
		
		if(adjecencies.containsKey(v)){
			Iterator<Integer> iter2 = this.adjecencies.get(v).keySet().iterator();
			while(iter2.hasNext()){
				k=iter2.next();
				if(!vertices.isEmpty()&&!vertices.containsKey(k))
					neighbours.add(k);
			}
		}
		
		/*
		 * If there are no unvisited nodes return -1
		 */
		if(neighbours.isEmpty())
			return -1;
		/*
		 * Else send the first unvisited
		 */
		else
			return neighbours.get(0);
	
    } 
	
	/*
	 * The DFS method is used to check whether the undirected graph is connected or not.
	 * It stores the visited nodes in the global vertices variable
	 */
	private void dfs() {
		/*
		 * Create a new stack
		 */
		StackX stack = new StackX();
		int i=0;
		/*
		 * Find a random key to start from
		 */
		while(!this.adjecencies.containsKey(i)){
			i++;
		}
		/*
		 * Put i in the visited array and push it in the stack
		 */
		vertices.put(i, true);
		stack.push(i);
		/*
		 * Perform the actual DFS
		 */
		while( !stack.isEmpty() )     
        {
        int v = getAdjUnvisitedVertex( stack.peek() );
        
        if(v == -1)                 								  
           stack.pop();				//Pop if there are no unvisisted vertices
        
        else                        								   
           {
        		vertices.put(v,true);  									
        		stack.push(v);                					
           }
        } 

	}
	
	/*
	 * The following method checks if a given edge exists in the graph
	 */
	private Boolean edgeExists(Integer v1,Integer v2){
		
		if(adjecencies.containsKey(v1)&&adjecencies.get(v1).containsKey(v2))
			return true;
		
		else if(adjecencies.containsKey(v2)&&adjecencies.get(v2).containsKey(v1))
			return true;
		
		return false;
		
	}
	
	/*
	 * The following method generates a random graph
	 * @param mode: The mode of generation
	 * @param noOfVertices: The no of vertices in the graph
	 * @param density: The density of the edges of the graph 
	 */
	public void generateGraph(char mode,int noOfVertices,int density){
		/*
		 * Initialize the noOfVertices , hypothetical maximum edges and  the actual no of edges
		 * in the graph. Also create a random variable that will generate random no useful for 
		 * generating the graph. 
		 */
		this.noOfVertices = noOfVertices;
		int maxEdges = (noOfVertices*(noOfVertices - 1 ))/2;
		int noOfEdges = (maxEdges*density)/100;
	//	int noOfEdges = (noOfVertices-1)*2;
		Random random = new Random();
		int k =0;

		/*
		 * Loop until the no of edges is reached
		 */
		while(k<noOfEdges){
			/*
			 * Create 2 vertices
			 */
			int i = random.nextInt(noOfVertices);
			int j = random.nextInt(noOfVertices);
			
			/*
			 * Check if they are non unique
			 */
			if(i==j)
				continue;
			/*
			 * Generate a random cost 
			 */
			int cost = random.nextInt(1000)+1;
			/*
			 * Create the inner hashmap which store the 2nd vertex and the cost associated from moving from 
			 * i to j
			 */
			HashMap<Integer,Integer> edgeNCost = new HashMap<Integer,Integer>(); 
			edgeNCost.put(j, cost);
			/*
			 * Check if the edge already exists
			 */
			if(edgeExists(i,j))
				continue;
			/*
			 * Check if i already exists and if so put it using i as the key
			 */
			if(adjecencies.containsKey(i)){
				if(!adjecencies.get(i).containsKey(j)){
					adjecencies.get(i).put(j, cost);
				}
				else{
					continue;
				}
			}
			/*
			 * Else are entry for i
			 */
			else{
				adjecencies.put(i,edgeNCost);
			}
			
			k++;

		
		}
	}
	/*
	 * This method generates a graph given a user input in a file
	 */
	public void generateGraph(char mode,String filename){
		
		Scanner reader = null;
		
		try{
			File file = new File(filename);			//Create a file object
			reader = new Scanner(file);				//Create a scanner to read from the file
			/*
			 * Read in the no of vertices and the no of edges.
			 */
			this.noOfVertices = reader.nextInt();
			int noOfEdges = reader.nextInt();
			/*
			 * Read in until the end of file is reached
			 */
			while(reader.hasNext()){
				/*
				 * Read in the vertices and the cost
				 */
				int i = reader.nextInt();
				int j = reader.nextInt();
				int cost = reader.nextInt();
				/*
				 * Create the inner hashmap which store the 2nd vertex and the cost associated from moving from 
				 * i to j
				 */
				HashMap<Integer,Integer> edgeNCost = new HashMap<Integer,Integer>(); 
				edgeNCost.put(j, cost);
				/*
				 * Check if i already exists and if so put it using i as the key
				 */
				if(adjecencies.containsKey(i)){
					if(!adjecencies.get(i).containsKey(j)){
						adjecencies.get(i).put(j, cost);
					}
					else{
						
					}
				}
				/*
				 * Else are entry for i
				 */
				else{
					adjecencies.put(i,edgeNCost);
				}
			}			
		}
		/*
		 * Handle file exception
		 */
		catch(FileNotFoundException x){
			System.err.format("FileNotFoundException: %s%n", x);
		}

	}
	/*
	 * The following method prints out the graph
	 */
	public void getGraph(){
		
		System.out.println("From\tTo\tCost");
		
		for(int i:adjecencies.keySet()){
			for(int j:adjecencies.get(i).keySet()){	
				System.out.println(i+"\t"+j+"\t"+adjecencies.get(i).get(j));
			}
			
		}
		
	}
	/*
	 * The following method checks if the graph is connected by performing DFS on it 
	 */
	
	public Boolean checkIfConnected(){
		
		this.dfs();
		
		int dfsVertices = vertices.keySet().size();
		
		vertices.clear();
		
		if(dfsVertices==this.noOfVertices){
			return true;
		}
		return false;
		
	}
	
	/*
	 * The following method resets the graph by clearing the adjecency lists
	 */
	public void resetGraph(){
		adjecencies.clear();
		}
	/*
	 * The following method is the Simple Scheme implementation of the primms' algorithm
	 */
	public void primms(){
		
		String Edges = "\n";							//String to store the edges of the mst
		int totalCost = 0;								//cost of the mst
		
		int INFINITE = 10000;							//set a macro INFINITE to more than 1000
		int[][] lastUpdate = new int[6000][2];			//a last update table to store with vertex did the last decrease key
		int counter = 0;								//a counter

		int v1 = this.adjecencies.keySet().iterator().next();	// get the first vertex from the adjecency list

		ArrayList<Integer> neighbours = new ArrayList<Integer>();	//a arraylist to store the neighbours of a vertex
		
		int extractedVertex = 0;						// the minimum vertex extracted from the array
		
		int[] pq = new int[noOfVertices];				//an array which stores the cost of the vertex at the vertex index
		/*
		 * Populate pq with vertices 
		 */
		
		for(int i= 0;i<noOfVertices;i++ ){
			if(i==v1){									//set the first vertex to min
				pq[i]= -1;
			}
			else{
				pq[i] = INFINITE;						//set all other vertex cost to infinite
				lastUpdate[i][0] = INFINITE;
				lastUpdate[i][1] = -1;
			}
		}
		/*
		 * Start to remove the min vertices until all have been removed
		 */
		long startTime =System.currentTimeMillis();
		
		while(counter<noOfVertices){
			
			int leastCost = INFINITE;							//reset leaastcost to infinite
			
			//find out the least cost and its vertex
			//this step is the complexity deciding factor
			//the above while loops throgh n vertices and the for loop below again loops through n vertices
			// so the complexity  is O(n^2)
			
			for(int i = 0;i<noOfVertices;i++){
				if(pq[i]<leastCost&&!vertices.containsKey(i)){
					extractedVertex =i;
					leastCost=pq[i];
				}
			}
			/*
			 * Mark the vertex as visited
			 */
			vertices.put(extractedVertex, true);
			/*
			 * add the edge to the MST and update the cost of the MST
			 */
			if(extractedVertex==v1){}
			else{
				Edges = Edges+extractedVertex+" "+lastUpdate[extractedVertex][1]+"\n";
				totalCost = totalCost + lastUpdate[extractedVertex][0];
			}
			/*
			 * The following block of code gets the extracted vertex's neighbours 
			 * and updates the cost
			 */
			neighbours = this.getUnvisitedNeighbours(extractedVertex);						//get all unvisited neighbours
			
			Iterator<Integer> neighboursItr = neighbours.iterator();

			while(neighboursItr.hasNext()){
				int nextVertexVal = (int) neighboursItr.next();								//get next neighbour
				int cost =0;
				
				if(this.adjecencies.containsKey(extractedVertex)){							//Get the cost of the edge
					if(this.adjecencies.get(extractedVertex).containsKey(nextVertexVal))	//from the extracted node 
						cost = this.adjecencies.get(extractedVertex).get(nextVertexVal);    //to this neighbour
					else
						cost = this.adjecencies.get(nextVertexVal).get(extractedVertex);
				}
				else
					cost = this.adjecencies.get(nextVertexVal).get(extractedVertex);
				
				if(lastUpdate[nextVertexVal][0]>cost){										//check if the cost is less than the previous cost
					lastUpdate[nextVertexVal][0] = cost;									//if yes than update that cost in pq and the last update table
					lastUpdate[nextVertexVal][1] = extractedVertex;
					pq[nextVertexVal] = cost;

				}
		
			}
			counter++;																		//increment counter


		}
		long endtime = startTime-System.currentTimeMillis();
		System.out.println("Simple Scheme Execution Time:"+endtime);
		System.out.println(totalCost+Edges);												//print out the MST
		vertices.clear();		
	}
	/*
	 * Fibonacci Heap Implementation of the primms algorithm
	 */
	public void fprimms(){
		String Edges = "\n";					//String to store the edges of the MST
		int totalCost = 0;						//Cost of the MST

		int INFINITE = 10000;					//A macro INFINITE set to a value greater then 1000
		int[][] lastUpdate = new int[6000][2];	//Last update table to store the vertex which update a given node
												//in the fibonacci heap last

		int v1 = 0; 							//The start vertex
		
		ArrayList<Integer> neighbours = new ArrayList<Integer>();						//A hashmap to store a vertex's neighbours
		HashMap<Integer,Node<Integer>> nodes = new HashMap<Integer,Node<Integer>>();	// a hashmap storing references to nodes in the fibonacci heap

		Node<Integer> extractedVertex = null;									//A node object storing a reference to the min extracted fibonacci node
		
		FibonacciHeap<Integer> pq = new FibonacciHeap<Integer>();				//The Fibonacci Heap
		
		pq.insert(v1, -1);														//Insert the first vertex and set its key so that it is the min
		
		/*
		 * Populate the heap with the other vertices and set the last update table
		 * This operation iterates over n vertices and each insert takes O(l)
		 * So it takes O(n) time
		 */
		
		for(int i = 1;i<noOfVertices;i++ ){ 			
				nodes.put(i,pq.insert(i, INFINITE));
				lastUpdate[i][0] = INFINITE;
				lastUpdate[i][1] = -1;
		}
		/*
		 * Start to remove the min vertex until the heap is empty
		 */

		long startTime =System.currentTimeMillis();

		while(!pq.isEmpty()){
			/*
			 * Remove the min
			 * This is called n times which is the no of vertices 
			 * A remove Min cost O(logn) so the complexity of this operation is O(nlogn)
			 */
			extractedVertex = pq.removeMin();
			/*
			 * Mark as visited
			 */

			vertices.put(extractedVertex.getValue(), true);
			/*
			 * Add edges to the MST and update the cost
			 */
			if(extractedVertex.getValue()==v1){}	// if the root do nothing
			else{
				Edges = Edges+extractedVertex.getValue()+" "+lastUpdate[extractedVertex.getValue()][1]+"\n";
				totalCost = totalCost + lastUpdate[extractedVertex.getValue()][0];
			}
			/*
			 * get the unvisited neighbours of the min
			 */

			neighbours = this.getUnvisitedNeighbours(extractedVertex.getValue());

			Iterator<Integer> neighboursItr = neighbours.iterator();
			/*
			 * The following block iterates over its neighbours and calls decrease key if required
			 * If we summarise over the whole operation the decrease key goes over all the edges in the graph
			 * Thus the cost of a decrease key is O(1) and if there a e edges the following block of code runs in
			 * O(e)
			 * Thus the overall complexity comes to O(n+nlogn+e) which is O(nlogn+e). For very dense graphs e ~ n^2
			 * So the worst case complexity of primms using fibonnaci heap is O(n^2)
			 */
			
			while(neighboursItr.hasNext()){
				int nextVertexVal = (int) neighboursItr.next();			//get next neighbour
				int cost =0;											//set cost of edge to zero
				
				
				if(this.adjecencies.containsKey(extractedVertex.getValue())){                           //Get the cost of the edge from
					if(this.adjecencies.get(extractedVertex.getValue()).containsKey(nextVertexVal))     //the current vertex to this
						cost = this.adjecencies.get(extractedVertex.getValue()).get(nextVertexVal);     //neighbour
					else
						cost = this.adjecencies.get(nextVertexVal).get(extractedVertex.getValue());
				}
				else
					cost = this.adjecencies.get(nextVertexVal).get(extractedVertex.getValue());
				
				if(lastUpdate[nextVertexVal][0]>cost){                                   //Check if the current cost is less than the key of the
					Node<Integer> nextVertex = nodes.get(nextVertexVal);                 //neighbour in the heap and if so do a decrease key and
					lastUpdate[nextVertexVal][0] = cost;                                 //set the min element as the element in the last update table
					lastUpdate[nextVertexVal][1] = extractedVertex.getValue();
					pq.decreaseKey(nextVertex, cost);
				}
		
			}
		}
		
		long endtime = startTime-System.currentTimeMillis();
		System.out.println("Fibonacci Execution Time:"+endtime);


		System.out.println(totalCost+Edges);                   //Print the MST

		vertices.clear();
	}

}

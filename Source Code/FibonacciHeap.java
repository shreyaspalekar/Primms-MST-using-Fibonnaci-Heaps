package mst;
import java.util.*; // For ArrayList

public final class FibonacciHeap<T> {
    /*
     * Node Structure of a Fibonacci Node
     */
    public static final class Node<T> {
        private int     degree = 0;       	// Number of children
        private boolean childCut = false; 	// Whether this node is marked

        private Node<T> next;   			// Next and previous elements in the list
        private Node<T> prev;

        private Node<T> parent; 			// Parent in the tree, if any.

        private Node<T> child;  			// Child node, if any.

        private T      element;     		// Element being stored here
        private double key; 				// Its priority
        
        /*
         * Getters and Setters for element
         */

        public T getValue() {
            return element;
        }

        public void setValue(T value) {
            element = value;
        }
        /*
         * Getter for priority
         */

        public double getKey() {
            return key;
        }
        /*
         * Constructor
         */

        public Node(T elem, double priority) {
            next = prev = this;
            element = elem;
            key = priority;
        }
    }
    
    /*
     * Min Node
     */

    private Node<T> min = null;
    
    /*
     * Size of the heap
     */

    private int size = 0;
    
    /*
     * Insert Operation
     */

    public Node<T> insert(T value, double key) {
        checkKeys(key);									//check for a valid key

        Node<T> result = new Node<T>(value, key);		//Create a new heap node

        min = mergeLists(min, result);					//meld it with the top list

        ++size;											//increment the size

        return result;									//return a reference to the newly created node
    }
    
    /*
     * min element lookup operation
     */

    public Node<T> min() {
        if (isEmpty())												//Check if the heap is empty
            throw new NoSuchElementException("Heap is empty.");
        return min;													//Else return min element reference
    }
    
    /*
     * Operation to check if the heap is empty
     */

    public boolean isEmpty() {
        return min == null;								//return true if min is null
    }
    
    /*
     * Opearation to get heap size
     */

    public int size() {
        return size;									//return heap size
    }
    
    /*
     * Meld operation on two fibonacci heaps
     */

    public static <T> FibonacciHeap<T> meld(FibonacciHeap<T> one, FibonacciHeap<T> two) {
       
    	FibonacciHeap<T> result = new FibonacciHeap<T>();			//create a new result node

        result.min = mergeLists(one.min, two.min);					//merge the two top lists and get the min element

        result.size = one.size + two.size;							//add up the sizes

        one.size = two.size = 0;									//
        one.min  = null;											//Delete the two orignal lists
        two.min  = null;											//

        return result;												//return a reference to the result node
    }
    /*
     * Remove min operation on the Fibonacci Heap
     */

    public Node<T> removeMin() {
        
    	if (isEmpty())													//Check if the heap is empty
            throw new NoSuchElementException("Heap is empty.");

        --size;															//decrease the size

        Node<T> minElem = min;											//store the min element

        if (min.next == min) { 											// Case one: There is one element in the heap
            min = null;													//set the min to null
        }
        else { 															// Case two
            min.prev.next = min.next;									// set the sibling pointers of the min elements
            min.next.prev = min.prev;									//neighbours
            min = min.next; 											// select an arbitrary element of the root list.
        }

        if (minElem.child != null) {									//if the min element has child trees
            Node<?> curr = minElem.child;
            do {
                curr.parent = null;										// set the parent pointers of all the children to null

                curr = curr.next;
            } while (curr != minElem.child);
        }

        min = mergeLists(min, minElem.child);							//meld the min elements chlidren and the top list 
        																// and get a new min

        if (min == null) return minElem;								//if the tree is empty stop
        /*
         * Start the procedure for pairwise merging
         */

        List<Node<T>> treeTable = new ArrayList<Node<T>>();

        List<Node<T>> toVisit = new ArrayList<Node<T>>();
        
        /*
         * add top trees to a list
         */
        
        for (Node<T> curr = min; toVisit.isEmpty() || toVisit.get(0) != curr; curr = curr.next)
            toVisit.add(curr);

        for (Node<T> curr: toVisit) {
            while (true) {
            	/*
            	 * set the tree table to null
            	 */
                while (curr.degree >= treeTable.size())
                    treeTable.add(null);
                /*
                 * set the degree of the tree in the tree table if it is null
                 */

                if (treeTable.get(curr.degree) == null) {
                    treeTable.set(curr.degree, curr);
                    break;
                }
                /*
                 * else start to pairwise merge
                 */

                Node<T> other = treeTable.get(curr.degree);
                treeTable.set(curr.degree, null);							 // Clear the slot
                /*
                 * Select the max and the min out of the two trees
                 */

                Node<T> min = (other.key < curr.key)? other : curr;
                Node<T> max = (other.key < curr.key)? curr  : other;
                /*
                 * merge the two trees
                 */
                
                max.next.prev = max.prev;
                max.prev.next = max.next;

                max.next = max.prev = max;
                min.child = mergeLists(min.child, max);
                
                max.parent = min;

                max.childCut = false;

                ++min.degree;

                curr = min;
            }

            if (curr.key <= min.key) min = curr;
        }
        return minElem;
    }
    /*
     * Decrease key operation
     */

    public void decreaseKey(Node<T> entry, double newPriority) {
        checkKeys(newPriority);
        if (newPriority > entry.key)
            throw new IllegalArgumentException("New priority exceeds old.");

        decreaseKeyUnchecked(entry, newPriority);					//Actual Decrease key
    }
    
    /*
     * Delete a node
     */
    public void delete(Node<T> entry) {
        decreaseKeyUnchecked(entry, Double.NEGATIVE_INFINITY); 		//Decrease the element key so that it becomes the min 

        removeMin();												//Remove min
    }
    /*
     * Operation to check for valid keys
     */
    private void checkKeys(double priority) {
        if (Double.isNaN(priority))
            throw new IllegalArgumentException(priority + " is invalid.");
    }
    /*
     * Merge two lists
     */

    private static <T> Node<T> mergeLists(Node<T> one, Node<T> two) {
        if (one == null && two == null) { 			// Both null, resulting list is null.
            return null;
        }
        else if (one != null && two == null) { 		// Two is null, result is one.
            return one;
        }
        else if (one == null && two != null) { 		// One is null, result is two.
            return two;
        }
        else {									 	// Both non-null; actually do the splice.
            Node<T> oneNext = one.next; 			// Cache this since we're about to overwrite it.
            one.next = two.next;
            one.next.prev = one;
            two.next = oneNext;
            two.next.prev = two;

            return one.key < two.key? one : two;
        }
    }
    /*
     * Actual decrease key after checking the key
     */

    private void decreaseKeyUnchecked(Node<T> entry, double priority) {
        entry.key = priority;												//Set the key to new key
        
        if (entry.parent != null && entry.key <= entry.parent.key)			//Check if the key of the node is less than its parent
            cutNode(entry);													//if yes cut the nodes

        if (entry.key <= min.key)											//Check if the new key is the min key
            min = entry;
    }
    /*
     * Operation to cut out a tree
     */

    private void cutNode(Node<T> entry) {
        entry.childCut = false;						//Set its childcut to false as it has just cut itself

        if (entry.parent == null) return;			//if it has no parent then stop

        if (entry.next != entry) { 					// Set the sibling node pointers
            entry.next.prev = entry.prev;
            entry.prev.next = entry.next;
        }

        if (entry.parent.child == entry) {			//if it is the child pointer of the parent
            if (entry.next != entry) {				
                entry.parent.child = entry.next;	//select the next sibling as the child pointer
            }
            else {									//if it has no sibling then set the child pointer to null
                entry.parent.child = null;
            }
        }

        --entry.parent.degree;						//decrease the degree of the parent

        entry.prev = entry.next = entry;			//Set the sibling pointers of the current node
        min = mergeLists(min, entry);				// merge the node with the top level list

        if (entry.parent.childCut)					//Check the child cut of the parent
            cutNode(entry.parent);					//if true then cut the parent
        else
            entry.parent.childCut = true;			//else set the child cut of the parent to true

        entry.parent = null;						//set the parent pointer of the current node to null
    }
}
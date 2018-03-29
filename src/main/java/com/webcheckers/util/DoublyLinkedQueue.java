package com.webcheckers.util;

import java.util.NoSuchElementException;

public class DoublyLinkedQueue<T> {


    private Node<T> front;
    private Node<T> rear;

    // declare variable for size
    private int size;

    //default constructor
    public DoublyLinkedQueue()
    {
        front = rear = null;
        size = 0;
    }


	/* will need min. of three methods
		size(), enqueue which takes a parameter, and dequeue

		remember that the dequeue should throw an Exception if nothing in list
		can use NoSuchElementException
	 */

    /**
     * size method--
     * This method returns the number of items in the list
     * @return - int value representing number of items in the list
     */
    public int size()
    {
        return size;
    }

    /**
     * enqueue method--
     * This method can add new items to the list
     * @param data - This store a reference to the data that needs to be stored
     */
    public void enqueue(T data)
    {
        if(size==0)
            front = rear = new Node<T>(data,null,null);
        else
        {
            rear.setNext(new Node<T>(data,null, rear));
            rear = rear.getNext();
        }
        size++;
    }

    /**
     * dequeue method--
     * This method can remove items from the front of the queue and throws
     * NoSuchElementException if the queue is empty
     * @return - a reference to the items that was removed
     */
    public T dequeue()
    {
        T temp;

        if(size == 0)
            throw new NoSuchElementException("Empty Collection!");
        else
        {
            temp = front.getData();
            front = front.getNext();

            if(front == null)
                rear = null;
        }
        size--;
        return temp;
    }

    /**
     * addToFront method
     * This method can add new items to the front of the queue
     * @param data - refers to the data that needs to stored in the queue
     */
    public void addToFront(T data)
    {
        if(size==0)
            this.enqueue(data);
        else
        {
            front = new Node<T>(data,front,null);
            front.getNext().setPrev(front);
            size++;
        }

    }

    /**
     * removeFromFront method
     * This removes items from the front of the queue and throws an exception
     * if the collection is empty
     */
    public T removeFromFront()
    {
        return dequeue();
    }

    /**
     * addToRear method
     * This method adds the new item at the rear of the queue
     * @param data - refers to the data to be stored
     */
    public void addToRear(T data)
    {
        this.enqueue(data);
    }

    /**
     * removeFromRear method
     * This  method removes items from the rear of the queue and throws an
     * exception if the the collection is empty
     * @return - a reference to the data removed
     */
    public T removeFromRear()
    {
        if(size == 0)
            throw new NoSuchElementException("Empty Collection!");

        T temp = rear.getData();

        if(size == 1)
            rear = front = null;
        else
        {
            rear = rear.getPrev();
            rear.setNext(null);
        }

        size--;

        return temp;

    }

    /**
     * insertAtPosition method--
     * This method can add new items from (0 to size) positions in the queue,
     * since 0 based position is used
     * @param data - reference to the data to be stored
     * @param pos - int value representing the 0 based position of where
     *  item needs to be added
     */
    public void insertAtPosition(T data,int pos)
    {
        if(pos > size || pos < 0)
            throw new NoSuchElementException("Inserting at position " + pos + " failed.");

        if (pos == 0)
            addToFront(data);
        else if(pos == size)
            addToRear(data);
        else
        {
            Node<T> target = front;

            for (int i = 1; i < pos; i++)
                target = target.getNext();

            Node<T> temp = new Node<T>(data, target.getNext(), target);
            temp.getPrev().setNext(temp);
            temp.getNext().setPrev(temp);
            size++;
        }

    }

    /**
     * getPos method--
     * This method returns the items at the 0 based entered position,
     * It  throws an exception if invalid position is entered
     * @param pos -- 0 based position
     * @return - the item
     */
    public T getPos(int pos)
    {
        if(pos >= size || pos < 0)
        {
            throw new NoSuchElementException("Invalid Position");
        }
        Node<T> target = front;

        for (int i = 0; i < pos; i++)
            target = target.getNext();

        return target.getData();
    }

    /**
     * reverseString method--
     * this method returns a reference to a string containing the state of
     *  the list in reverse order
     * @return - reference to a string containing the state of the object in the reverse order
     */
    public String reverseString()
    {
        String str ="";

        Node<T> target = rear;

        while(target != null)
        {
            str+= target.getData().toString() + "\n";

            //	if(target.getPrev() == null)
            //	System.out.println("some problemo");

            target = target.getPrev();
        }

        return str;
    }

    /**
     * toString method--
     * this method returns the state of the list.
     * @return a reference to a String containing the state of the object.
     */
    public String toString()
    {
        String str ="";

        Node<T> target = front;

        while(target != null)
        {
            str+= target.getData().toString() + "\n";
            target = target.getNext();
        }
        //System.out.println("In queue a toString");
        return str;
    }



}

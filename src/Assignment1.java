// Assignment1.java
// The goal of this was to implement strassen's algorithm.
// By: Austin Teshuba 251003742

import java.io.*;
import java.util.Scanner;

public class Assignment1{
  
  public int[][] denseMatrixMult(int[][] A, int[][] B, int size)
  {
	  // create a return matrix.
	  int[][] matrix = initMatrix(size);
	  
	  // base case
	  // Just multiply the two numbers in the matrix.
	  if (size==1) {
		  matrix[0][0] = A[0][0]*B[0][0];
		  return matrix;
	  }
	  
	  // If the base case is not satisfied, we must do strassens. 
	  // Get M0-M6
	  //a00: x1 = 0, y1 = 0
	  //a01: x1 = 0; y1 = size/2
	  //a10: x1 = size/2, y1 = 0
	  //a11: x1 = size/2,. y1 = size/2
	  
	  // (a00+a11)*(b00+b11)
	  int[][] m0 = denseMatrixMult(sum(A,A,0,0,size/2,size/2,size/2), sum(B,B, 0,0,size/2,size/2,size/2), size/2);
	  // (a10+a11)*(B00)
	  int[][] m1 = denseMatrixMult(sum(A,A,size/2,0,size/2,size/2,size/2), sum(B, initMatrix(size/2), 0,0,0,0,size/2), size/2);
	  //a00*(b01-b11)
	  int[][] m2 = denseMatrixMult(sum(A, initMatrix(size/2), 0,0,0,0,size/2), sub(B, B, 0, size/2, size/2, size/2, size/2), size/2);
	  //a11*(b10-b00)
	  int[][] m3 = denseMatrixMult(sum(A,initMatrix(size/2), size/2, size/2, 0,0,size/2), sub(B,B,size/2,0,0,0,size/2), size/2);
	  //(a00+a01)*b11
	  int[][] m4 = denseMatrixMult(sum(A,A,0,0,0,size/2,size/2), sum(B, initMatrix(size/2), size/2, size/2,0,0,size/2), size/2);
	  //(a10-a00)*(b00+b01)
	  int[][] m5 = denseMatrixMult(sub(A,A,size/2,0,0,0,size/2), sum(B,B,0,0,0,size/2,size/2), size/2);
	  //(a01-a11)*(b10-b11)
	  int[][] m6 = denseMatrixMult(sub(A,A,0,size/2,size/2,size/2,size/2), sum(B,B,size/2,0,size/2,size/2,size/2), size/2);
	  
	  // Now that we have these, we can get C00 to C11
	  // m0+m3 + (m6-m4)
	  int[][] c00 = sum(sum(m0,m3,0,0,0,0,size/2), sub(m6,m4,0,0,0,0,size/2), 0,0,0,0, size/2);
	  // m2 + m4
	  int[][] c01 = sum(m2,m4,0,0,0,0,size/2);
	  // m1 + m3
	  int[][] c10 = sum(m1,m3,0,0,0,0,size/2);
	  // m0-m1 + m2 + m5
	  int[][] c11 = sum(sub(m0,m1,0,0,0,0,size/2), sum(m2,m5,0,0,0,0,size/2), 0,0,0,0,size/2);
	  
	  // Load the results into the return array.
	  // We are "stitching" the four subarrays together. 
	  for (int i = 0; i< size; i++) {
		  for (int j = 0; j<size; j++) {
			  if (i<size/2) {
				  if (j<size/2) {
					  matrix[i][j] = c00[i][j];
				  } else {
					  matrix[i][j] = c01[i][j-size/2];
				  }
			  } else {
				  if (j<size/2) {
					  matrix[i][j] = c10[i-size/2][j];
				  } else {
					  matrix[i][j] = c11[i-size/2][j-size/2];
				  }
			  }
		  }
	  }
	  
	  // return the matrix we made.
	  return matrix;
  }
  
  // Helper function to get a sub matrix without adding anything
  // ex. getting a00 or b11 
  public int[][] subMatrix(int[][]A, int x1, int y1, int n) {
	  
	  // create a return matrix
	  int[][] matrix = initMatrix(n);
	  
	  // iterate through and populate the matrix.
	  for (int i = 0; i<n; i++) {
		  for (int j = 0; j<n; j++) {
			  matrix[i][j] = A[x1+i][y1+j];
		  }
	  }
	  
	  // return matrix
	  return matrix;
  }
  
  // Helper function to get the sum of two matricies
  public int[][] sum(int[][] A, int[][] B, int x1, int y1, int x2, int y2, int n)
  {
	  // create return matrix
	  int[][] matrix = initMatrix(n);
	  
	  // populate the return matrix by adding together the elements of the two given arrays.
	  for (int i = 0; i<n; i++) {
		  for (int j = 0; j<n; j++) {
			  matrix[i][j] = A[x1+i][y1+j] + B[x2+i][y2+j];
		  }
	  }
	  
	  // return the matrix
	  return matrix;
  }
  
  // This is a helper function to subtract two matricies
  public int[][] sub(int[][] A, int[][] B, int x1, int y1, int x2, int y2, int n)
  {
	  // create return matrix
	  int[][] matrix = initMatrix(n);
	  
	  // iterate through each element and make the appropriate subtraction. Populate the return matrix
	  for (int i = 0; i<n; i++) {
		  // This is each line
		  for (int j = 0; j<n; j++) {
			  matrix[i][j] = A[x1+i][y1+j] - B[x2+i][y2+j];
		  }
	  }
	  
	  // return the return matrix.
	  return matrix;
  }
  
  // Helper function to create an empty matrix of a given size.
  public int[][] initMatrix(int n)
  {
	// create an empty 2d array and return it! Simple stuff here.
	  int[][] retMatrix = new int[n][n];
	  return retMatrix;
  }
  
  // Helper function to display the matrix
  public void printMatrix(int n, int[][] A)
  {
	  // go through by line, then by element, and print the output.
	  for (int i = 0; i<n; i++) {
		  for (int j = 0; j<n; j++) {
			  System.out.print(A[i][j] + " ");
		  }
		  // end the line at each new space. 
		  System.out.println(" ");
	  }
  }
  
  // Helper function to read a matrix from a given file
  public int[][] readMatrix(String filename, int n) throws Exception
  {
	  // start with a null file and scanner
	  File file = null;
	  Scanner sc = null;
	  // try to get a file and scanner. If it fails, an exception is thrown
	  file = new File(filename);
	  sc = new Scanner(file);
	  
	  
	  // create the return matrix
	  int[][] matrix = initMatrix(n);
	  // go through the file line by line and populate the matrix
	  for (int i = 0; i<n; i++) {
		  String line = sc.nextLine();
		  String[] elements = line.split(" ");
		  for (int j = 0; j<n; j++) {
			  matrix[i][j] = Integer.parseInt(elements[j]);
		  }
	  }
	  // close scanner, return the matrix.
	  sc.close();
	  return matrix;
  }
  
}
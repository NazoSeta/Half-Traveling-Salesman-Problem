import java.awt.Desktop;
import java.io.*;
import java.util.*;

//First Programmer  ->  Name: Hasan  	     Surname: Özeren    No: 150121036
//Second Programmer ->  Name: Niyazi Ozan    Surname: Ateþ      No: 150121991
//Third Programmer  ->  Name: Ahmet Arda     Surname: Nalbant   No: 150121004
//Fourth Programmer ->  Name: Umut  	     Surname: Bayar     No: 150120043

/* The purpose of this project was to find as optimal as possible solution for the half travelling salesman problem.
 * This means that we go through some two-dimensional coordinates only once.
 * We do this until half of the coordinates has been visited and after that we return to the starting point.
 * We could have started from any point. We will explain the reason of choosing a particular point later. */

public class HTSP {

	// This is our main function.
	public static void main(String[] args) throws Exception{
		
		// Here we ask the user to enter a valid input file.
		Scanner tempScan = new Scanner(System.in);
		System.out.print("What is the input file labeled as (test-input-X.txt): ");
		String fileName = tempScan.nextLine();
		File inputFile = new File(fileName);
		
		// If the file exists then we keep on going.
		if (inputFile.exists()) {
			Scanner input = new Scanner(inputFile);
			
			// Here we create our output file.
			File outputFile = new File("solution.txt");
			PrintWriter solution = new PrintWriter(outputFile);
			
			// This is our ArrayList which holds the cities
			int n = 0;
			ArrayList<Nodes> list = new ArrayList<>();
			
			// Here we fill the ArrayList in. We do this with the Nodes class.
			while (input.hasNext()) {
				String temp = input.nextLine();
				String tempArr[] = temp.split(" ");
				if (tempArr.length != 3) {
					System.out.println("Input is not valid!");
					System.exit(0);
				}
				Nodes tempNode = new Nodes(Integer.parseInt(tempArr[0]), Integer.parseInt(tempArr[1]), Integer.parseInt(tempArr[2]));
				list.add(tempNode);
				n++;
			}
			
			// From this part on we try to find the median of the cities to predict a good starting point.
			int xSort[] = new int[n];
			int ySort[] = new int[n];
			for (int i = 0 ; i < n ; i++) {
				xSort[i] = list.get(i).getX();
				ySort[i] = list.get(i).getY();
			}
			
			// We sort the list with the x and y coordinates separated.
			Arrays.sort(xSort);
			Arrays.sort(ySort);
			
			/* The calculations will differ based on the input size.
			 * If the size in odd, we take the middle point as the coordinates for the median.
			 * Otherwise, it is even, and we will take the average of the two middle points to get the coordinates of both x and y. */
			double xMedium = 0;
			double yMedium = 0;
			double m = n;
			if (n % 2 == 1) {
				xMedium = xSort[(int)Math.ceil(m/2) - 1];
				yMedium = ySort[(int)Math.ceil(m/2) - 1];
			}
			else {
				xMedium = (xSort[(int)Math.ceil(m/2) - 1] + xSort[(int)Math.ceil(m/2)])/2.0;
				yMedium = (ySort[(int)Math.ceil(m/2) - 1] + ySort[(int)Math.ceil(m/2)])/2.0;
			}
			
			// This part is used to find the closest city based on the median of both x and y.
			int idMedium = 0;
			double minD = Math.pow(list.get(0).getX() - xMedium, 2) + Math.pow(list.get(0).getY() - yMedium, 2);
			for (int i = 1 ; i < n ; i++) {
				double currentMin = Math.pow(list.get(i).getX() - xMedium, 2) + Math.pow(list.get(i).getY() - yMedium, 2);
				if (currentMin < minD) {
					minD = currentMin;
					idMedium = i;
				}
			}
			
			// This ArrayList called connectList is the list that will be our solution for the HTSP.
			ArrayList<Nodes> connectList = new ArrayList<>();
			connectList.add(list.get(idMedium));
			list.remove(idMedium);
			
			/* We search for the closest point for the start and end of the list.
			 * We do this n/2 times by looking through a n sized list.
			 * If it is the case that we have found a closer point to the starting point than the ending point of the list, we will add it on the top of the path.
			 * So, the new starting point will be the last found point in this case.
			 * Otherwise, we have found a point that is close to the ending point than the starting point of the list.
			 * In this case we add the new point to the end of the path.
			 * While doing this we will remove the closest point from the list.
			 * The reason for this is so we don’t get duplicated points. */
			for (int i = 0 ; i < (int)Math.ceil(m/2) - 1; i++) {
				double minStart = Math.pow(list.get(0).getX() - connectList.get(0).getX(), 2) + Math.pow(list.get(0).getY() - connectList.get(0).getY(), 2);
				double minEnd = Math.pow(list.get(0).getX() - connectList.get(i).getX(), 2) + Math.pow(list.get(0).getY() - connectList.get(i).getY(), 2);
				int startId = 0;
				int endId = 0;
				for (int j = 1 ; j < list.size() ; j++) {
					double currentMinStart = Math.pow(list.get(j).getX() - connectList.get(0).getX(), 2) + Math.pow(list.get(j).getY() - connectList.get(0).getY(), 2);
					double currentMinEnd = Math.pow(list.get(j).getX() - connectList.get(i).getX(), 2) + Math.pow(list.get(j).getY() - connectList.get(i).getY(), 2);
					if (currentMinStart < minStart) {
						minStart = currentMinStart;
						startId = j;
					}
					if (currentMinEnd < minEnd) {
						minEnd = currentMinEnd;
						endId = j;
					}
				}
				if (minStart < minEnd) {
					connectList.add(0, list.get(startId));
					list.remove(startId);
				}
				else {
					connectList.add(list.get(endId));
					list.remove(endId);
				}
			}
			
			/* We find the total distance that the salesman has travelled.
			 * We did this by going through the whole path and at in the end we add the distance from the starting and ending point.
			 * This is because the salesman needs to end at its starting point. */
			long distance = 0;
			for (int i = 0 ; i < connectList.size() - 1 ; i++) {
				distance = distance + (int)Math.round(Math.sqrt(Math.pow(connectList.get(i).getX() - connectList.get(i+1).getX(), 2) + Math.pow(connectList.get(i).getY() - connectList.get(i+1).getY(), 2)));
			}
			
			distance = distance + (int)Math.round(Math.sqrt(Math.pow(connectList.get(0).getX() - connectList.get(connectList.size()-1).getX(), 2) + Math.pow(connectList.get(0).getY() - connectList.get(connectList.size()-1).getY(), 2)));
			
			// Here we print our distance.
			System.out.println(distance);
			solution.write("" + distance + "\n");
			
			// Here we print our cities in order.
			for (int i = 0 ; i < connectList.size() ; i++) {
				System.out.println(connectList.get(i).getId());
				solution.println("" + connectList.get(i).getId());
			}
			
			// here we close our input and output files.
			Desktop.getDesktop().open(outputFile);
			solution.close();
			tempScan.close();
			input.close();
		}
		else { // This is an error message in case a wrong input file is entered.
			System.out.println("File doesn't exist!");
		}
		
	}

}

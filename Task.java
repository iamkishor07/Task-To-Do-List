import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Collections;

public class Task{

	private static String usage = "Usage :-\n" + "$ ./task add 2 hello world    # Add a new item with priority 2 and text \"hello world\" to the list\n"
			+ "$ ./task ls                   # Show incomplete priority list items sorted by priority in ascending order\n"
			+ "$ ./task del INDEX            # Delete the incomplete item with the given index\n"
			+ "$ ./task done INDEX           # Mark the incomplete item with the given index as complete\n" 
			+ "$ ./task help                 # Show usage\n"
			+ "$ ./task report           	 # Statistics";

	private static Path path = Paths.get("task.txt");
	public static void main(String args[]) {

		if (args.length == 0) {
			System.out.println(usage);
			System.exit(0);

		} else if (args[0].equals("help")) {
			System.out.println(usage);

		} else if (args[0].equals("add")) {

			if (args.length < 2) {
				System.out.println("Error: Missing tasks string. Nothing added!.");
			} else {
				String sentence = args[2];
				int priority = Integer.parseInt(args[1]);
				additemtotaskList(priority,sentence);
			}

		} else if (args[0].equals("ls")) {
			showtaskList();

		} else if (args[0].equals("del")) {

			if (args.length < 2) {
				System.out.println("Error: Missing NUMBER for deleting tasks.");
			} else {
				int delNumber = Integer.parseInt(args[1]);
				deletetaskItem(delNumber - 1);
			}
		} else if (args[0].equals("done")) {
  
			if (args.length < 2) {
				System.out.println("Error: Missing NUMBER for marking tasks as done");
			} else {
				int donePosition = Integer.parseInt(args[1]);
				markAsCompleted(donePosition - 1);
			}
		} else if (args[0].equals("report")) {
			report();
		}
	}

	private static void deletetaskItem(int position) {

		try {
			List<String> lines = Files.readAllLines(path);

			if (position >= 0 && position < lines.size()) {

				lines.remove(position);

				System.out.println("Deleted task #" + (position + 1));

				FileWriter writer1;
				try {
					writer1 = new FileWriter("task.txt");
					for (String str : lines) {
						writer1.write(str + System.lineSeparator());
					}
					writer1.close();

				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				System.out.println("Error: task with index #" + (position + 1) + " does not exist. Nothing deleted.");
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private static void report() {

		try {

			Path donePath = Paths.get("done.txt");

			List<String> taskList = Files.readAllLines(path);
			List<String> doneList = Files.readAllLines(donePath);

			System.out.println( " " + "Pending : " + (taskList.size()));
			showtaskList();
			
			System.out.println( " " + "Completed : " + doneList.size());
			if (doneList.size() != 0) {
				for (int k = doneList.size() - 1; k>=0; k--) {
					System.out.println( doneList.get(k));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void markAsCompleted(int position) {

		try {

			List<String> lines = Files.readAllLines(path);
			for(int i=0;i<lines.size();i++){
				int index1=getIdexoFString(lines.get(i));
				int min=i;
				for(int j=i+1;j<lines.size();j++){
					int index2=getIdexoFString(lines.get(j));
					if(index1>=index2){
                        index1=index2;
						min=j;
					}
				}
				Collections.swap(lines, i, min);
			}

			if (position >= 0 && position < lines.size()) {

				String doneItem = lines.get(position);

				Date date = Calendar.getInstance().getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String todayDate = formatter.format(date);

				BufferedWriter writer = new BufferedWriter(new FileWriter("done.txt", true));
				writer.write("x " + todayDate + " " + doneItem);
				writer.newLine();
				writer.close();

				System.out.println("Marked item as done.");

				lines.remove(position);

				FileWriter writer1 = new FileWriter("task.txt");

				for (String str : lines) {
					writer1.write(str + System.lineSeparator());	
				}
				writer1.close();

			} else {
				System.out.println("Error: no incomplete item with index #" + (position + 1) + " exists.");
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("not found");
		}
	}

	private static void additemtotaskList(int priority,String addtask) {

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("task.txt", true));

			writer.write(addtask + "["+priority+"]");
			writer.newLine();
			writer.close();

			System.out.println("Added task: " + "\"" + addtask + "\""+ " "+ "With priority" +" "+priority);

		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	private static int  getIdexoFString(String s){
        int l=s.length()-2;
        String m="";
        while(s.charAt(l)!='['){
            m=s.charAt(l)+m;
            l--;
        }
        return Integer.parseInt(m);
    }
	private static void showtaskList() {

		try {
			File f = new File("task.txt");
			List<String> lines = Files.readAllLines(path);
			for(int i=0;i<lines.size();i++){
				int index1=getIdexoFString(lines.get(i));
				int min=i;
				for(int j=i+1;j<lines.size();j++){
					int index2=getIdexoFString(lines.get(j));
					if(index1>=index2){
                        index1=index2;
						min=j;
					}
				}
				Collections.swap(lines, i, min);
			}

			if (lines.size() != 0 && f.exists()) {
				for (int k = lines.size() - 1; k>=0; k--) {
					System.out.println("[" + String.valueOf(k + 1) + "] " + lines.get(k));
				}

			} else {
				System.out.println("There are no pending tasks!");
			}
	
		} catch (IOException e) {
			System.out.println("There are no pending tasks!");

			e.printStackTrace();
		}
	}

}
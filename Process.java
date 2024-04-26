//Ashe Peters Extra Cred Data Processing
import java.util.*;

interface InMemoryDB {

    int get(String key);

    void put(String key, int val);

    void begin_transaction();

    void commit();

    void rollback();
}

public class Process implements InMemoryDB {

    HashMap<String, Integer> publicKeys = new HashMap<String, Integer>();
    HashMap<String, Integer> privateKeys = new HashMap<String, Integer>();


    //Transaction flag, if 1, door is open, can close to start transaction
    int open = 1;

    public int get(String key) {

        if (open == 1) {
            if (publicKeys.get(key) != null) {
                return publicKeys.get(key);
            }
            else {
                System.out.print("Error - Key Not Found ");
                return -1;
            }
        }
        else {
            System.out.print("Error - Transaction In Progress ");
            return -1;
        }
    }

    public void put(String key, int val) {

        if (open == 0) {
            privateKeys.put("A", val);
        }
        else {
            System.out.print("Error - Transaction Not In Progress");
        }
    }

    public void begin_transaction() {
        //Close door, only one in at a time
        open = 0;

        return;
    }

    public void commit() { 

        if (open == 0) {
            for (String i : privateKeys.keySet()) {
                publicKeys.put(i, privateKeys.get(i));
            }

            privateKeys.clear();

            open = 1;
        }
        else {
            System.out.print("Error - Transaction Not In Progress");
        }

        return;
    }

    public void rollback() {

        if (open == 0) {
            privateKeys.clear();

            open = 1;
        }
        else {
            System.out.print("Error - Transaction Not In Progress");
        }

        return;
    }

    public static void main(String[] arg) {

        Process process = new Process();

        //Should return null, because A doesn’t exist in the DB yet
        //Return null when function has return type int???
        //this req is inting this function dawg /j 
        System.out.println("Testing: get(\"A\") when A does not exist");
        System.out.println("    Expected: Error - Key Not Found -1");
        System.out.print("      Actual: ");
        System.out.print(process.get("A"));

        System.out.println("\n\n");

        //Should throw an error because a transaction is not in progress
        System.out.println("Testing: put(\"A\", 5)");
        System.out.println("    Expected: Error - Transaction Not In Progress");
        System.out.print("      Actual: ");
        process.put("A", 5);

        System.out.println("\n\n");

        //Starts a new transaction
        process.begin_transaction();

        //Set’s value of A to 5, but its not committed yet
        process.put("A", 5);

        //Should return null, because updates to A are not committed yet
        System.out.println("Testing: get(\"A\") with A in Private Changes");
        System.out.println("    Expected: Error - Transaction In Progress -1");
        System.out.print("      Actual: ");
        System.out.print(process.get("A"));

        System.out.println("\n\n");

        //Update A’s value to 6 within the transaction
        process.put("A", 6);

        //Commits the open transaction
        process.commit();

        //Should return 6, that was the last value of A to be committed
        System.out.println("Testing: get(\"A\") with A in Public Changes");
        System.out.println("    Expected: 6");
        System.out.print("      Actual: ");
        System.out.print(process.get("A"));

        System.out.println("\n\n");

        //Throws an error, because there is no open transaction
        System.out.println("Testing: commit with closed transaction");
        System.out.println("    Expected: Error - Transaction Not In Progress");
        System.out.print("      Actual: ");
        process.commit();

        System.out.println("\n\n");

        //Throws an error because there is no ongoing transaction
        System.out.println("Testing: rollback with closed transaction");
        System.out.println("    Expected: Error - Transaction Not In Progress");
        System.out.print("      Actual: ");
        process.rollback();

        System.out.println("\n\n");

        //Should return null because B does not exist in the database
        System.out.println("Testing: get(\"B\") when B does not exist");
        System.out.println("    Expected: Error - Key Not Found -1");
        System.out.print("      Actual: ");
        System.out.print(process.get("B"));

        System.out.println("\n\n");

        //Starts a new transaction
        process.begin_transaction();

        //Set key B’s value to 10 within the transaction
        process.put("B", 10);

        //Rollback the transaction - rSevert any changes made to B
        process.rollback();

        //Should return null because changes to B were rolled back
        System.out.println("Testing: get(\"B\") when B was rolled back");
        System.out.println("    Expected: Error - Key Not Found -1");
        System.out.print("      Actual: ");
        System.out.print(process.get("B"));
    }
}
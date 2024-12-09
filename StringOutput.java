import java.util.Arrays;
import java.util.Scanner;

public class StringOutput {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
int userInput[] = {1,2,3,4,5};
arrayNodify(userInput);
int userInput2[] = arrayNodify(userInput);

for(int i=0;i<userInput2.length;i++){
    System.out.println(userInput2[i] + " ");
}
    }
    public static int[] arrayNodify(int arrRef[]){
        int[] arrRef2 = new int[arrRef.length];

        for(int i=0;i<arrRef.length;i++){
            arrRef2[i] = arrRef[i] * 2;
        }
        return arrRef2;
    }
}

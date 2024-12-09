import java.util.Random;
import java.util.Scanner;

public class Test1 {
    public static void main(String[] args) {
        int[] a1 = null;
        Scanner sc = new Scanner(System.in);
        String userInput = sc.nextLine();
        int spaceCount = 0;
        Scanner numbers = new Scanner(userInput);
        for(int i=0;i<userInput.length();i++){
            if(userInput.charAt(i) == ' '){
                spaceCount++;
            }
        }
        a1 = new int[spaceCount + 1];
        // System.out.println(spaceCount);
        for(int i=0;i<spaceCount + 1;i++){
            a1[i] += numbers.nextInt();
        }
        numbers.close();
        sc.close();
        for(int i=0;i<spaceCount + 1;i++){
            System.out.print(a1[i] + " ");
        }

}

static String arrayComma(int a1[]){
    String arrayComma = "";
    for(int i=0;i<a1.length;i++){
        if(a1[i] != a1[a1.length-1]){
            arrayComma += a1[i] + ",";
        }else{
            arrayComma += a1[i];
        }
    }
    return arrayComma;
}
}

  




            




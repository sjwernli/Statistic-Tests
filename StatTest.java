import java.util.*;
import java.io.*; // for File
public class StatTest{
   public static final double[] zvalues = {.00, .01, .02, .03, .04, .05, .06, .07, .08, .09}; 
   public static void main(String[] args)throws FileNotFoundException{
      Scanner console = new Scanner(System.in);
      System.out.println("Do you have:\n1. Categorical data\n2. Means\nor\n3. Proportions?");
      int data = console.nextInt();
      if(data == 1){
         categoricalTests();
      }
      else if(data==2){
         meanTests();
      }
      else{
         propTests();
      }
         
         
   }
   
   public static void categoricalTests()throws FileNotFoundException{
      CatData set1;
      Scanner console = new Scanner(System.in);
      System.out.println("How many rows?");
      int rows = console.nextInt();
      System.out.println("How many columns?");
      int cols = console.nextInt();
      System.out.println("Enter data (do not include totals):");
      double[][] catData = new double[rows][cols];
      for(int i = 0; i < rows; i++){
         for(int j = 0; j < cols; j++){
            catData[i][j]=console.nextDouble();
         }
      }
      System.out.println("Enter 1 to add your own expected values.");
      System.out.println("Enter 2 to have expected values calculated.");
      int choice = console.nextInt();
      if (choice == 1){
         double[][] expected = new double[rows][cols];
         for(int r = 0; r < rows; r++){
            for(int c=0; c<cols; c++){
               expected[r][c]=console.nextDouble();
            }
         }
         set1 = new CatData(catData, expected);
      }
      else{
         set1 = new CatData(catData);
      }
         
      System.out.println(set1.printData());
      System.out.println("Expected\n"+set1.printExpected());
      System.out.println("Chi squared: " + set1.chiSquared());
      System.out.println("degrees of freedom: " + set1.findDF());
      System.out.println("p-value: " + set1.PValue());
   }
   public static void propTests()throws FileNotFoundException{
      File negativeZ = new File("negativeZ.txt");
      File positiveZ = new File("positiveZ.txt");
      double[][] negZ = readTable(negativeZ);
      double[][] posZ = readTable(positiveZ);
      Scanner console = new Scanner(System.in);
      System.out.println("Enter 1 for a confidence interval.");
      System.out.println("Enter 2 for a z-test.");
      int test = console.nextInt();
      System.out.println("Enter 1 for 1 sample.");
      System.out.println("Enter 2 for 2 sample.");
      int sample = console.nextInt();
      double p = 0;
      double z = 0;
      if(test == 2 && sample == 1){
         System.out.println("Enter the population proportion.");
         double pprop = console.nextDouble();
         System.out.println("Enter the sample size (integer).");
         int size = console.nextInt();
         System.out.println("Enter the sample proportion.");
         double prop = console.nextDouble();
         z = Math.abs(prop-pprop);
         double x = pprop*(1-pprop);
         z = z/Math.sqrt(x/size);
         z = (int)(z*100)/100.0;
         System.out.println("Choose a hypothesis test:");
         System.out.println("1. p != " + pprop);
         System.out.println("2. p < " + pprop);
         System.out.println("3. p > " + pprop);
         int hypothesis = console.nextInt();
         if(hypothesis == 1 || hypothesis == 2){
            p = PvalueBelow(z, negZ);
            if (hypothesis == 1){
               p = p*2;
            }
         }
         else{
            p = PvalueAbove(z, posZ);
         }
         System.out.println(z);
         System.out.println(p);
      }
      else if (test == 2){
         System.out.println("Enter x1:");
         double x1 = console.nextDouble();
         System.out.println("Enter the first sample size:");
         double n1 = console.nextDouble();
         System.out.println("Enter x2");
         double x2 = console.nextDouble();
         System.out.println("Enter the second sample size:");
         double n2 = console.nextDouble();
         double p1 = x1/n1;
         double p2 = x2/n2;
         double SE = Math.sqrt((p1*(1-p1)/n1)+(p2*(1-p2)/n2));
         z = (p1-p2)/SE;
         System.out.println("Choose a hypothesis test:");
         System.out.println("1. p1-p2 != 0");
         System.out.println("2. p1-p2 < 0");
         System.out.println("3. p1-p2 > 0");
         int hypothesis = console.nextInt();
         if(hypothesis == 1 || hypothesis == 2){
            p = PvalueBelow(z, negZ);
            if (hypothesis == 1){
               p = p*2;
            }
         }
         else{
            p = PvalueAbove(z, posZ);
         }
         System.out.println(z);
         System.out.println(p);
      }
      else{
         double SE = 0;
         System.out.println("Enter a confidence interval (ex. 0.95):");
         double C = console.nextDouble();
         while(C>=1){
            C = C-1;
         }
         p = (int)(((1 - C)/2 + C) * 10000)/10000.0; 
         if (p>=0.5){
            z = findZ(p, posZ);
         }
         else{
            z = findZ(p, negZ);
         }
         if (sample == 1){
            System.out.println("Enter the population proportion.");
            double pprop = console.nextDouble();
            System.out.println("Enter the sample size (integer).");
            int size = console.nextInt();
            System.out.println("Enter the sample proportion.");
            double prop = console.nextDouble();
            SE = Math.sqrt(prop*(1-prop)/size);
            System.out.println(prop-z*SE + " to " + (prop + z*SE));
         }
         else{
            System.out.println("Enter x1:");
            double x1 = console.nextDouble();
            System.out.println("Enter the first sample size:");
            double n1 = console.nextDouble();
            System.out.println("Enter x2");
            double x2 = console.nextDouble();
            System.out.println("Enter the second sample size:");
            double n2 = console.nextDouble();
            double p1 = x1/n1;
            double p2 = x2/n2;
            SE = Math.sqrt((p1*(1-p1)/n1)+(p2*(1-p2)/n2));
         }
         
     } 
   }
   public static void meanTests(){
   }
   
   public static double[][] readTable(File ztable)throws FileNotFoundException{
      double values[][] = new double[35][zvalues.length];
      Scanner input = new Scanner(ztable);
      for(int i = 0; i < 35; i++){
         input.nextDouble();
         for(int k = 0; k < zvalues.length; k++){
            values[i][k] = input.nextDouble();
         }
      }
      return values;
      }
  
   
   public static double PvalueBelow(double zValue, double[][] table){
      if(zValue > 3.49){
         return 0.0;
      }
      return table[35-(int)(zValue*10)][(int)(zValue*100)%10];
   }
  
   public static double PvalueAbove(double zValue, double[][] table){
      if(zValue > 3.49){
         return 0.0;
      }
      return 1-table[(int)(zValue*10)][(int)(zValue*100)%10];
   }
   
   public static double findZ(double p, double[][] zTable){
      double upper = 0;
      double lower = 0;
      for (int i = 0; i < zTable.length; i++){
         for(int k = 0; k < zTable[i].length-1; k++){
            upper = zTable[i][k];
            if (p == lower){
               if(p < 0.5){
                  return (3.5-i * 0.1) + k*0.01;
               }
               return i * 0.1 + k*0.01;
            }
            else if (p > lower && p < upper){
               if(p<0){
                  return (((3.5-i*0.1)+k*0.01)+((3.5-(i-1)*0.1)+(k-1)*0.01))/2;
               }
               return ((i*0.1+k*0.01)+((i-1)*0.1+(k-1)*0.01))/2;
            }
            lower = upper;
         }
      }
      return 3.5;
   }

}
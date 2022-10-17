import java.io.*; // for File
import java.util.*; //scanner
public class CatData{
   private double[][] data;
   private double[][] expected;
   private int df;
   private double pvalues[] = {0.995, 0.975, 0.20, 0.10, 0.05, 0.025, 0.02, 0.01, 0.005, 0.002, 0.001};
   private double pTable[][];

   
   public CatData(double[][] data)throws FileNotFoundException{
      this.data = data;
      expected = calculateE();
      df = findDF();
      pTable = readTable();
   }
   public CatData(double[][] data, double[][] expected)throws FileNotFoundException{
      this.data = data;
      this.expected = expected;
      df = findDF();
      pTable = readTable();
   }
   
   public double[][] calculateE(){
      double[][] expected = new double[data.length][data[0].length];
      for(int i = 0; i < data.length; i++){
         for(int j = 0; j < data[i].length; j++){
            expected[i][j] = (int)((rowTotal(i)/totalCounts())*colTotal(j)*100)/100.00;
         }
      }
      return expected;     
   }
   
   public int findDF(){
      if (data[0].length == 1){
         return data.length - 1;
      }
      else{
         return (data.length - 1)*(data[0].length-1);
      }
   }
   
   public double rowTotal(int row){
      double total = 0;
      for(int i = 0; i < data[row].length; i++){
         total += data[row][i];
      }
      return total;
   }
   
   public double colTotal(int col){
      double total = 0;
      for(int i = 0; i < data.length; i++){
         total += data[i][col];
      }
      return total;
   }
   
   public double totalCounts(){
      double total = 0;
      for(int i = 0; i < data.length; i ++){
         for(int j = 0; j < data[i].length; j++){
            total += data[i][j];
         }
      }
      return total;
   }
   
   public String printData(){
      String result = "";
      for(int i = 0; i < data.length; i++){
         for(int j = 0; j <data[i].length; j++){
            result = result + data[i][j] + "\t";
         }
         result += "\n";
      }
      return result;
   }
   
   public String printExpected(){
      String result = "";
      for(int i = 0; i < expected.length; i++){
         for(int j = 0; j <expected[i].length; j++){
            result = result + expected[i][j] + "\t";
         }
         result += "\n";
      }
      return result;
   }
   
   public double chiSquared(){
      double result = 0;
      for(int r = 0; r<data.length; r++){
         for(int c=0; c<data[r].length; c++){
            result += Math.pow(data[r][c]-expected[r][c], 2)/expected[r][c];
         }
      }
      return result;
   }
   
   public double[][] readTable()throws FileNotFoundException{
      double values[][] = new double[250][pvalues.length];
      Scanner input = new Scanner(new File("chiSquaredValues.txt"));
      for(int i = 0; i < 250; i++){
         input.nextDouble();
         for(int k = 0; k < pvalues.length; k++){
            values[i][k] = input.nextDouble();
         }
      }
      return values;
      }
   public double PValue(){
      if(df > 250){
         return -1;
      }
      double lower = pTable[df-1][0];
      double upper = pTable[df-1][1];
      if (chiSquared()<lower){
         return 0.995;
      }
      for(int i = 0; i < pvalues.length-1; i++){
         lower = pTable[df-1][i];
         upper = pTable[df-1][i+1];
         if(chiSquared() > lower && chiSquared() < upper){
            double temp = (Math.pow(chiSquared(), 2)-Math.pow(lower,2))/(Math.pow(upper,2)-Math.pow(lower,2));
            return pvalues[i] - (Math.pow(pvalues[i],1)-Math.pow(pvalues[i+1],1))*temp;
         }
         else if (chiSquared() == lower){
            return pvalues[i];
         }
         else if(chiSquared() == upper){
            return pvalues[i+1];
         }
      }
      return 0.0001;
   }
}
public class DataSet{
   private double mean;
   private double sd;
   private int sampleSize;
   private double[] data;
   
   public DataSet(double mean, double sd, int sampleSize){
      this.mean = mean;
      this.sd = sd;
      this.sampleSize = sampleSize;
      data = new double[0];
   }
   public DataSet(double[] data){
      this.data = data;
      sampleSize = data.length;
      mean = this.getMean();
   }
   
   public double getMean(){
      if (data.length>0){
         double total = 0;
         for(int i =0; i < data.length; i++){
            total += data[i];
         }
         return total/data.length;
      }
      else{
         return mean;
      }
   }
   
   public void setSD(double sd){
      this.sd = sd;
   }
   
   public double getSD(){
      return sd;
   }
   
   public int getSampleSize(){
      return sampleSize;
   }
}
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

    public static void main(String[] args) {
        CountDownLatch countDownLatch=new CountDownLatch(2);
        ExecutorService executorService=Executors.newSingleThreadExecutor();
        DecimalFormat df = new DecimalFormat("0.000");
        String response1,response2;
        Scanner input=new Scanner(System.in);
        CountCharacters c=new CountCharacters();


        System.out.print("Input 1st word: ");
        response1=input.nextLine();
        System.out.print("Input 2nd word: ");
        response2=input.nextLine();
        System.out.println();
        double startTime= System.nanoTime();

        executorService.submit(new CountCharacters(response1, countDownLatch));
        executorService.submit(new CountCharacters(response2, countDownLatch));
        executorService.shutdown();

        try{
            countDownLatch.await();
            double endTime = System.nanoTime();
            double executionTime = (endTime-startTime)/1000000000;
            System.out.println();
            System.out.println("Total: " +c.getTotal()+"\n");
            System.out.println("Execution time: "+df.format(executionTime)+ " seconds"+"\n");


        } catch (InterruptedException e) {
            System.out.print("Error");
        }
    }

    static class CountCharacters implements Runnable {
        private String response;
        private int count;
        private CountDownLatch countDownLatch;
        private static int total=0;

        public CountCharacters(String response, CountDownLatch countDownLatch){
            this.response=response;
            this.countDownLatch=countDownLatch;
        }

        public CountCharacters() {

        }

        @Override
        public void run() {
            try {
                countingfunc();
            } catch (IOException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();

        }

        private int getTotal(){
            return total;
        }

        private void countingfunc() throws IOException {
            File file=new File("src/RossBeresford.txt");
            String [] words;

            FileReader fileReader=new FileReader(file);
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            String content;
            while((content=bufferedReader.readLine())!=null){
                words=content.split(" ");
                for(String word : words){
                    word=word.replace(",","");
                    word=word.replace(".","");
                    if(word.equals(response)){
                        count++;
                    }
                }
            }
            fileReader.close();
            System.out.println(response +" - "+count);
            total+=count;
        }
    }

}

public class ConcurrencyArray {
    final int size = 10_000_000;
    final int h = size / 2;
    float[] arr = new float[size];

    public static void main(String[] args) {
        new ConcurrencyArray().calculate();
    }
    public void calculate(){

        //1 method
        for(int i = 0; i < size; i++){
            arr[i] = 1;
        }
        long a = System.currentTimeMillis();
        for(int i = 0; i < size; i++){
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }

        System.out.print(System.currentTimeMillis() - a);
        System.out.println("  - Время расчета 1 метода.");


        //2 method
        float[] a1 = new float[h];
        float[] a2 = new float[h];
        a = System.currentTimeMillis();
        System.arraycopy(arr, 0, a1, 0, h);
        System.arraycopy(arr, h, a2, 0, h);

        Thread th1 = new Thread(() -> {
            for (int i = 0; i < a1.length; i++){
                a1[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread th2 = new Thread(() -> {
            for (int i = 0; i < a2.length; i++){
                a2[i] = (float)(arr[i] * Math.sin(0.2f + (i + h) / 5) * Math.cos(0.2f + (i + h) / 5) * Math.cos(0.4f + (i + h) / 2));
            }
        });

        th1.start();
        th2.start();

        try{
            th1.join();
            th2.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        System.arraycopy(a1, 0, arr, 0, h);
        System.arraycopy(a2, 0, arr, h, h);

        System.out.print(System.currentTimeMillis() - a);
        System.out.println("  - Время расчета 2 метода.");
    }
}

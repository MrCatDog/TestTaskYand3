import java.nio.file.Path;
import java.nio.file.Paths;

public class NeighborParity {
    public static void main(String[] args) {
        Path in = Paths.get("input.txt");
        Path out = Paths.get("output.txt");
        NeighborParityMaker.makeParity(in,out);
    }
}


//далее херня с попыткой работы через RandomAccessFile.
//Проблемы с кодировкой. пишет-то пишет, а что и в чём не ясно. Просмотр через UTF-16 или близкородственные кодировки результата не дал.
        /*
        Random rand = new Random();
        try (RandomAccessFile file = new RandomAccessFile("input.txt","rw")) {
            file.writeInt(250);
            file.writeChars("\n");
            for(int i=0;i<250;i++) {
                file.writeInt(rand.nextInt());
                file.writeChars(" ");
            }

        } catch (IOException ex) {
            System.out.println(ex.toString());
            System.out.println(ex.getMessage());
            System.exit(-1);
        }


        int count=0;
        int mass=0;

        try (RandomAccessFile file = new RandomAccessFile("input.txt","rw")) {
            count = file.read();
            mass = file.read();
        } catch (IOException ex) {
            System.out.println(ex.toString());
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
        System.out.println(count);
        System.out.println(mass);
        */
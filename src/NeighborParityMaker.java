import java.io.*;
import java.nio.file.Path;
import java.util.*;

/**
 * Work in progress.
 */
public class NeighborParityMaker {

    public static void makeParity(Path inputFile, Path outputFile) {
        ArrayList<Integer> numbers = new ArrayList<>();
        HashMap<Integer, Integer> evenElem = new HashMap<>();
        HashMap<Integer,Integer> oddElem = new HashMap<>();
        HashSet<Integer> used = new HashSet<>();

        boolean startSpace=false;
        boolean endSpace=false;

        try (Scanner in = new Scanner(inputFile)) {
            int size = in.nextInt();
            int number = in.nextInt();

            boolean parity = number % 2 == 0;

            startSpace=!parity;

            numbers.add(number);

            for (int i = 1; i < size; i++) {
                number = in.nextInt();

                if ((number % 2 == 0) == parity) {
                    if (parity)
                        evenElem.put(i,number);
                    else
                        oddElem.put(i,number);
                } else
                    parity=!parity;

                numbers.add(number);
            }

            endSpace=!parity;
        } catch (IOException ex) {
            System.out.println(ex.toString());
            System.exit(-1);
        }

        try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile.toFile()), java.nio.charset.StandardCharsets.UTF_8))) {

            int sub=evenElem.size() - oddElem.size();
            Integer num;
            switch (sub) {
                case 0:
                    //write
                    break;

                case 1:
                    if(startSpace) {
                        num=evenElem.remove(0);
                        out.write(num);
                        used.add(num);
                        write(out,numbers,oddElem,evenElem,used);
                        break;
                    }
                    if(endSpace) {
                        num=evenElem.remove(0);
                        used.add(num);
                        write(out,numbers,oddElem,evenElem,used);
                        out.write(num);
                        break;
                    }
                    out.write(-1);//no space for one even element
                    break;

                case 2:
                    if(startSpace && endSpace) {
                        num=evenElem.remove(0);
                        out.write(numbers.get(num));
                        used.add(num);
                        num=evenElem.remove(0);
                        used.add(num);
                        write(out,numbers,oddElem,evenElem,used);
                        out.write(numbers.get(num));
                        break;
                    }
                    else
                        out.write(-1);//No space for two even elements

                case -1:
                    if(!startSpace) {
                        num=oddElem.remove(0);
                        out.write(numbers.get(num)+" ");
                        used.add(num);
                        write(out,numbers,evenElem,oddElem,used);
                        break;
                    }
                    if(!endSpace) {
                        num=oddElem.remove(0);
                        used.add(num);
                        write(out,numbers,evenElem,oddElem,used);
                        out.write(numbers.get(num));
                        break;
                    }
                    out.write(-1);//no space for one odd element
                    break;

                case -2:
                    if(!startSpace && !endSpace) {
                        num=oddElem.remove(0);
                        out.write(numbers.get(num));
                        used.add(num);
                        num=oddElem.remove(0);
                        used.add(num);
                        write(out,numbers,evenElem,oddElem,used);
                        out.write(numbers.get(num));
                        break;
                    }
                    else
                        out.write(-1);//No space for two odd elements

                default:
                    out.write(-1);//Too many elements with same parity.
                    break;
                }
        } catch (IOException ex) {
            System.out.println(ex.toString());
            System.exit(-1);
        }

    }

    private static void write(Writer out, ArrayList<Integer> numbers, HashMap<Integer, Integer> smallest, HashMap<Integer, Integer> biggest, HashSet<Integer> used) throws IOException
    {
        ListIterator<Integer> iterator = numbers.listIterator();
        Integer numberBuff;
        while (iterator.hasNext()) {
            int nextIndex = iterator.nextIndex();
            if (smallest.containsKey(nextIndex)) {
                numberBuff=iteratorBig.next();
                iteratorBig.remove();
                out.write(numbers.get(numberBuff)+" ");//WARNING if you delete space you will use write(int), and that will write symbols, NaN. Try append.
                used.add(numberBuff);
                out.write(iterator.next()+" ");
            } else {
                if(biggest.containsKey(nextIndex) || used.contains(nextIndex))
                    iterator.next();
                else
                    out.write(iterator.next()+" ");
            }
        }
    }
}

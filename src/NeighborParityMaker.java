import java.io.*;
import java.nio.file.Path;
import java.util.*;

/**
 * Work in progress.
 */
class NeighborParityMaker {

    static void makeParity(Path inputFile, Path outputFile) {
        Integer[] numbers;
        HashSet<Integer> evenElem = new HashSet<>();
        HashSet<Integer> oddElem = new HashSet<>();

        boolean startSpace;
        boolean endSpace;

        try (Scanner in = new Scanner(inputFile);
             Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile.toFile()), java.nio.charset.StandardCharsets.UTF_8)))
        {
            numbers = new Integer[in.nextInt()];
            int number = in.nextInt();
            boolean parity = number % 2 == 0;
            startSpace=!parity;
            numbers[0]=number;

            for (int i = 1; i < numbers.length; i++) {
                number = in.nextInt();

                if ((number % 2 == 0) == parity) {
                    if (parity)
                        evenElem.add(i);
                    else
                        oddElem.add(i);
                } else
                    parity=!parity;

                numbers[i] = number;
            }
            endSpace=!parity;

            Iterator<Integer> iterator;
            int sub=evenElem.size() - oddElem.size();
            switch (sub) {
                case 0:
                    iterator=evenElem.iterator();
                    write(out,numbers,evenElem,oddElem,iterator);
                    break;

                case 1:
                    if(evenElem.size()>oddElem.size()) {
                        iterator = evenElem.iterator();
                    } else {
                        iterator = oddElem.iterator();
                        startSpace=!startSpace;
                        endSpace=!endSpace;
                    }
                    if(startSpace || endSpace) {
                        boolean insertFlag=false;
                        if(startSpace) {
                            out.write(numbers[iterator.next()] + " ");
                            insertFlag=true;
                        }
                        write(out,numbers,evenElem,oddElem,iterator);
                        if(!insertFlag)
                            out.write(numbers[iterator.next()] + " ");
                        break;
                    }
                    else {
                        out.write(-1);//no space for one odd element
                        break;
                    }

                case 2:
                    if(startSpace && endSpace) {
                        iterator=evenElem.iterator();
                        out.write(numbers[iterator.next()]);
                        write(out,numbers,oddElem,evenElem,iterator);
                        out.write(numbers[iterator.next()]);
                        break;
                    }
                    else
                        out.write(-1);//No space for two even elements

                case -1:
                    if(!startSpace || !endSpace) {
                        iterator=oddElem.iterator();
                        boolean insertFlag=false;
                        if(!startSpace) {
                            out.write(numbers[iterator.next()] + " ");
                            insertFlag=true;
                        }
                        write(out,numbers,evenElem,oddElem,iterator);
                        if(!insertFlag)
                            out.write(numbers[iterator.next()] + " ");
                        break;
                    }
                    else {
                        out.write(-1);//no space for one odd element
                        break;
                    }

                case -2:
                    if(!startSpace && !endSpace) {
                        iterator=oddElem.iterator();
                        out.write(numbers[iterator.next()]);
                        write(out,numbers,evenElem,oddElem,iterator);
                        out.write(numbers[iterator.next()]);
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

    private static void write(Writer out, Integer[] numbers, HashSet<Integer> smallest, HashSet<Integer> biggest, Iterator<Integer> iterator) throws IOException
    {
        out.write(numbers[0]+" ");
        int nextIndex;
        for (int i=0;i<numbers.length-1;i++) {
            nextIndex=i+1;
            if (smallest.contains(nextIndex)) {
                out.write(numbers[iterator.next()]+" ");//WARNING if you delete space you will use write(int), and that will write symbols, NaN. Try append.
                out.write(numbers[nextIndex]+" ");
            } else {
                if(!biggest.contains(nextIndex))
                    out.write(numbers[i+1]+" ");
            }
        }
    }
}

import java.io.*;
import java.nio.file.Path;
import java.util.*;

/**
 * Read integer sequence from file and, if it possible, write a number of transpositions and a new sequence into new file,
 * where all neighbors for all numbers have opposite parity.
 * Algorithm made to make minimum transpositions in input sequence.
 * If changes are possible, writes a count of transpositions and, in a next string, new built sequence.
 * If changes are not possible, writes "-1".
 * @author Konstantin
 * @version 1.0
 */
class NeighborParityMaker {
    /**
     * Main method. Call it for make your sequence "parity-changed".
     * @param inputFile input file path. First line should be a number of integers in sequence. Second should be an integer sequence.
     * @param outputFile output file path. It will be created if not exists.
     */
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

            //this block define biggest Set and change settings for it
            Iterator<Integer> iterator;
            HashSet<Integer> big;
            HashSet<Integer> little;
            if(oddElem.size()>evenElem.size()) {
                startSpace=!startSpace;
                endSpace=!endSpace;
                iterator=oddElem.iterator();
                big=oddElem;
                little=evenElem;
            } else {
                iterator=evenElem.iterator();
                big=evenElem;
                little=oddElem;
            }

            //this block define difference between sets, check free space for unpaired elements and starts a write operation
            switch (big.size() - little.size()) {
                case 0:
                    out.write(big.size()+"\n");//write count of transpositions. Only elements from bigger Set will change their places, so size is a count.
                    write(out,numbers,little,big,iterator);
                    break;

                case 1:
                    if(startSpace || endSpace) {
                        out.write(big.size()+"\n");//write count of transpositions. Only elements from bigger Set will change their places, so size is a count.
                        if(startSpace) {//if start pos assume required parity
                            out.write(numbers[iterator.next()] + " ");//write it on start pos
                            write(out,numbers,little,big,iterator);//write new sequence.
                        }
                        else {
                            write(out,numbers,evenElem,oddElem,iterator);//write new sequence.
                            out.write(numbers[iterator.next()] + " ");//write extra elem to end pos
                        }
                    }
                    else
                        out.write("-1");//no space for one element
                    break;

                case 2:
                    if(startSpace && endSpace) {//if we have enough places for two elements
                        out.write(big.size()+"\n");//write count of transpositions. Only elements from bigger Set will change their places, so size is a count.
                        out.write(numbers[iterator.next()]+" ");//write first elem on start pos.
                        write(out,numbers,little,big,iterator);//write new sequence.
                        out.write(numbers[iterator.next()]+"");//write last elem on end pos.
                    }
                    else
                        out.write("-1");//No space for two elements
                    break;

                default:
                    out.write("-1");//Too many elements with same parity.
                    break;
                }
        } catch (IOException ex) {
            System.out.println(ex.toString());
            System.exit(-1);
        }
    }

    /**
     * Write input sequence into out file in right parity order.
     * @param out output file.
     * @param input input sequence.
     * @param wrongIndexes set of wrong indexes, which need to be replaced.
     * @param iteratedWrongIndexes set of wrong indexes, but bigger or equal wrongIndexes and iterated by iterator.
     * @param iterator iterator of iteratedWrongIndexes which show the next element to insert into sequence.
     * @throws IOException if an output error occurs while file writing.
     */
    private static void write(Writer out, Integer[] input, HashSet<Integer> wrongIndexes, HashSet<Integer> iteratedWrongIndexes, Iterator<Integer> iterator) throws IOException
    {
        out.write(input[0]+" ");//Writes bearing element, which cannot be in sets
        int nextIndex;
        for (int i = 0; i< input.length-1; i++) {
            nextIndex=i+1;
            if (wrongIndexes.contains(nextIndex)) {//if wrongIndexes contains this element,
                out.append(input[iterator.next()].toString()).append(' ');//write into a new sequence an element from iteratedWrongIndexes
                out.append(input[nextIndex].toString()).append(' ');//and this elem after what.
            } else {
                if(!iteratedWrongIndexes.contains(nextIndex))//all elements from iteratedWrongIndexes must change their position, so we skip them in input sequence.
                    out.append(input[i+1].toString()).append(' ');//all this elements had a rightful position at start. Just rewrite them into a new sequence.
            }
        }
    }
}

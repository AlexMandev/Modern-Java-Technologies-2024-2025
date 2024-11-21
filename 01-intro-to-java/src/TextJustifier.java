public class TextJustifier {

    public static int calculateRows(String[] words, int maxWidth) {
        if(words == null || words.length == 0)
            return 0;

        int currentWhiteSpace = 0;
        int currWordCount = 0;
        int rowCount = 1;
        int currSum = 0;

        for(String word : words) {
            currSum += word.length();
            currWordCount++;
            if(currWordCount > 1)
                currentWhiteSpace++;


            if(currSum + currentWhiteSpace > maxWidth) {
                rowCount++;
                currSum = word.length();
                currentWhiteSpace = 0;
                currWordCount = 1;
            }
        }

        return rowCount;
    }

    public static void appendLastLine(String[] words, int maxWidth, int rowBegin, StringBuilder line, String[] resultArray) {

        // supposing StringBuilder line is empty here
        for (int i = rowBegin; i < words.length; i++) {
            line.append(words[i]);

            if (i < words.length - 1) {
                line.append(' ');
            }
        }

        while (line.length() < maxWidth) {
            line.append(' ');
        }

        resultArray[resultArray.length - 1] = line.toString();

    }

    public static String[] justifyText(String[] words, int maxWidth) {
        if(words == null || words.length == 0)
            return new String[0];

        int lines = calculateRows(words, maxWidth);
        String[] result = new String[lines];

        StringBuilder line = new StringBuilder();

        int rowBegin = 0;
        int lineLength = 0, minSpacesCount = 0, wordCount = 0, lineIndex = 0;

        for(int i = 0; i < words.length; i++) {
            lineLength += words[i].length();
            wordCount++;

            if(wordCount > 1) {
                minSpacesCount++;
            }

            if(lineLength + minSpacesCount > maxWidth) { // creating a line, if it overflows the max width
                lineLength -= words[i].length();
                minSpacesCount--;
                wordCount--;

                int spaces = maxWidth - lineLength;

                int interval = 0;

                // calculating min spaces count between words

                if(wordCount != 1)
                    interval = spaces/(wordCount - 1);
                else
                    interval = maxWidth - lineLength;

                for(int j = rowBegin; j < i; j++) { // appending the current word
                    line.append(words[j]);

                    if(j != i - 1 || wordCount == 1) { // appending min amount of spaces
                        for(int k = 0; k < interval; k++)
                            line.append(' ');
                    }

                    // checking to append more spaces if they can't be evenly put between all words
                    if(wordCount != 1 && spaces > (wordCount - 1) * interval)
                        line.append(' ');

                    spaces--; // decrementing spaces, assuming we put extra spaces
                }

                // deleting the line and resetting indices and flags
                result[lineIndex++] = line.toString();
                line.delete(0, line.length());
                rowBegin = i;
                wordCount = 1;
                minSpacesCount = 0;
                lineLength = words[i].length();
            }
        }

        // appending last left centered line
        appendLastLine(words, maxWidth, rowBegin, line, result);

        return result;
    }
}


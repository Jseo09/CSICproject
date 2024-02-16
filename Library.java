import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] args) {
		String directory = "C:/Users/seoji/Downloads/data (1)/data/tiny1.txt";
		System.out.println("Question 1");
		getMostWordCounts(directory);
		System.out.println("Question 2");
		getMostWordsForSpecificWord(directory,"the" );
		getMostWordsForSpecificWord(directory, "of");
		getMostWordsForSpecificWord(directory, "was");
		forThePhase(directory, "but the");
		forThePhase(directory,"it was");
		forThePhase(directory,"in my");
		highestFrequencyInSentence(directory);
	}
	/**
	 ---------------------------------------------------------------
	 AUTHOR:         JIN SEO
	 CREATED DATE:   2024/2/15
	 PURPOSE:        To find the most frequent words from the file
	 PRECONDITIONS:  N/A
	 POSTCONDITIONS: N/A
	 ARGUMENTS:      Requires the file Directory(String) from the user
	 DEPENDENCIES:   N/A
	 ---------------------------------------------------------------
	 */
	private static void getMostWordCounts(String fileDirectory) {
		int wordCount = 0;
		ArrayStack<String> stack = new ArrayStack<>(10000);
		ArrayStack<Integer> counts = new ArrayStack<>(10000);
		try (BufferedReader reader = new BufferedReader(new FileReader(fileDirectory))) {
			String line;
			while ((line = reader.readLine()) != null) {
				Pattern pattern = Pattern.compile("\\b\\w+\\b");
				Matcher matcher = pattern.matcher(line);

				while (matcher.find()) {
					String word = matcher.group().toLowerCase();
					int index = indexOfWord(stack, word, wordCount);
					if (index == -1) {
						stack.push(word);
						counts.push(1);
						wordCount++;
					} else {
						counts.setIndex(index, counts.getIndex(index) + 1); // Update count for existing word
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		int maxIndex = 0;
		ArrayStack<Integer> indexKeeper = new ArrayStack<>(100);
		for (int i = 1; i < wordCount; i++) {
			if (counts.getIndex(i) > counts.getIndex(maxIndex)) {
				maxIndex = i;
				indexKeeper.empty();
			}
			if (counts.getIndex(i).equals(counts.getIndex(maxIndex))){
				indexKeeper.push(i);
			}

		}

		ArrayStack<Integer> indexForThird = new ArrayStack<>(100);
		//For the Second question
		int firstHighestIndex = 0;
		int secondHighestIndex = 0;
		int thirdHighestIndex = 0;
		for(int i = 1; i < wordCount; i++){
			if(counts.getIndex(i) > counts.getIndex(firstHighestIndex)){
				thirdHighestIndex = secondHighestIndex;
				secondHighestIndex = firstHighestIndex;
				firstHighestIndex = i;
			}
			else if(counts.getIndex(i) > counts.getIndex(secondHighestIndex)){
				thirdHighestIndex = secondHighestIndex;
				secondHighestIndex = i;
			}
			else if (counts.getIndex(i) > counts.getIndex(i)){
				thirdHighestIndex = i;
			}
		}
		//For getting same amount
		for (int i = 0; i < wordCount; i++) {
			if (counts.getIndex(i) == counts.getIndex(thirdHighestIndex)) {
				indexForThird.push(i);
			}
		}




		//Printing here
		if(indexKeeper.getSizeOfWords() >= 1){
			for(int i = 0; i < indexKeeper.getSizeOfWords(); i ++) {
				System.out.println("The most frequent word(s) in the file is(are) : " + stack.getIndex(indexKeeper.getIndex(i)));
				System.out.println("Frequency: " + counts.getIndex(maxIndex));
			}
		}
		System.out.println("*************************************************");
		System.out.println("*************************************************");



		System.out.println("The 3rd most frequent words in the file are:");
		while (!indexForThird.isEmpty()) {
			int index = indexForThird.pop();
			System.out.println("The most frequent word(s) in the file is(are) : " + stack.getIndex(index) + " \nFrequency: " + counts.getIndex(index));
			System.out.println("*************************************************");
		}



	}
	/**
	 ---------------------------------------------------------------
	 AUTHOR:         JIN SEO
	 CREATED DATE:   2024/2/15
	 PURPOSE:        To find the sentence that uses user's input mostly.
	 PRECONDITIONS:  N/A
	 POSTCONDITIONS: N/A
	 ARGUMENTS:      Requires the file Directory(String) from the user and target word
	 DEPENDENCIES:   N/A
	 ---------------------------------------------------------------
	 */
	public static void getMostWordsForSpecificWord(String fileDirectory, String word) {
		ArrayStack<String> sentenceWithMaxOccurrences = new ArrayStack<>(10000);
		int maxOccurrences = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(fileDirectory))) {
			StringBuilder textBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				textBuilder.append(line).append("\n"); // Ensure newline characters are retained
			}

			// Split text into paragraphs based on consecutive newline characters
			String[] paragraphs = textBuilder.toString().split("(?m)(?=\\n\\s*\\n)");

			for (String paragraph : paragraphs) {
				// Split paragraph into sentences
				String[] sentences = paragraph.trim().split("(?<=[.!?\\n])\\s+");

				Pattern pattern = Pattern.compile("\\b" + word + "\\b", Pattern.CASE_INSENSITIVE);

				for (String sentence : sentences) {
					Matcher matcher = pattern.matcher(sentence);
					int occurrences = 0;
					while (matcher.find()) {
						occurrences++;
					}
					if (occurrences > maxOccurrences) {
						maxOccurrences = occurrences;
						sentenceWithMaxOccurrences.empty(); // Clear the stack
						sentenceWithMaxOccurrences.push(sentence.trim()); // Trim to remove leading/trailing whitespace
					} else if (occurrences == maxOccurrences) {
						sentenceWithMaxOccurrences.push(sentence.trim());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!sentenceWithMaxOccurrences.isEmpty()) {
			int count = 1;
			System.out.println("The most sentence(s) with the most word '" + word + "' is/are:");
			while (!sentenceWithMaxOccurrences.isEmpty()) {
				System.out.println("############ Sentence " + count + " ############ ");
				System.out.println(sentenceWithMaxOccurrences.pop().replace(".", ""));
				count++;
			}
			System.out.println("With the frequency of " + maxOccurrences);
			System.out.println("*************************************************");
		} else {
			System.out.println("Was unable to find the sentence with the word: " + word);
		}
	}
	/**
	 ---------------------------------------------------------------
	 AUTHOR:         JIN SEO
	 CREATED DATE:   2024/2/16
	 PURPOSE:        To find the sentence that uses user's input mostly.
	 PRECONDITIONS:  N/A
	 POSTCONDITIONS: N/A
	 ARGUMENTS:      Requires the file Directory(String) from the user and target phase
	 DEPENDENCIES:   N/A
	 ---------------------------------------------------------------
	 */
	public static void forThePhase(String fileDirectory, String word) {
		ArrayStack<String> sentenceWithMaxOccurrences = new ArrayStack<>(10000);
		int maxOccurrences = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(fileDirectory))) {
			StringBuilder textBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				textBuilder.append(line).append("\n"); // Ensure newline characters are retained
			}

			// Split text into sentences based on periods, exclamation marks, question marks, and newlines occurring together
			String[] sentences = textBuilder.toString().split("(?<=[.!?\\n]|^)(?!\\s*$)");


			Pattern pattern = Pattern.compile("\\b" + word.trim() + "\\b", Pattern.CASE_INSENSITIVE);

			for (String sentence : sentences) {
				Matcher matcher = pattern.matcher(sentence);
				int occurrences = 0;
				while (matcher.find()) {
					occurrences++;
				}
				if (occurrences > maxOccurrences) {
					maxOccurrences = occurrences;
					sentenceWithMaxOccurrences.empty();
					sentenceWithMaxOccurrences.push(sentence.trim()); // Trim to remove leading/trailing whitespace
				} else if (occurrences == maxOccurrences) {
					sentenceWithMaxOccurrences.push(sentence.trim());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!sentenceWithMaxOccurrences.isEmpty()) {
			int count = 1;
			System.out.println("The most sentence(s) with the most phrase '" + word + "' is/are:");
			while (!sentenceWithMaxOccurrences.isEmpty()) {
				String sentence = sentenceWithMaxOccurrences.pop();
				// Check if the sentence contains the specified word before printing
				if (sentence.toLowerCase().contains(word.toLowerCase())) {
					System.out.println("############ Sentence " + count + " ############ ");
					System.out.println(sentence.replaceAll("[.!?]$", "")); // Remove end punctuation
					count++;
				}
			}
			System.out.println("With the frequency of " + maxOccurrences);
			System.out.println("*************************************************");
		} else {
			System.out.println("The phrase '" + word + "' is not found in the file.");
			System.out.println("*************************************************");
		}
	}
	public static void highestFrequencyInSentence(String fileDirectory){
		ArrayStack<String> mostFrequentWords = new ArrayStack<>(100);
		int maxFrequency = 0;
		ArrayStack<String> sentencesWithFrequent = new ArrayStack<>(100);
		try (BufferedReader reader = new BufferedReader(new FileReader(fileDirectory))) {
			StringBuilder textBuilder = new StringBuilder();
			String line;
			//When there is line to read
			while ((line = reader.readLine()) != null) {
				textBuilder.append(line).append("\n"); // Ensure newline characters are retained
			}
			//FROM CHATGPT
			String[] sentences = textBuilder.toString().split("(?<=[.!?\\n])\\s+(?!$)");
			for (String sentence : sentences) {
				//FROM CHATGPT
				String[] words = sentence.split("\\s+");
				for (String word : words) {
					// Count occurrences of the word in the sentence
					int count = 0;
					Pattern pattern = Pattern.compile("\\b" + word + "\\b", Pattern.CASE_INSENSITIVE);
					Matcher matcher = pattern.matcher(sentence);
					while (matcher.find()) {
						count++;
					}
					// Update most frequent words and their frequency if necessary
					if (count > maxFrequency) {
						maxFrequency = count;
						mostFrequentWords.empty();
						mostFrequentWords.push(word);
						sentencesWithFrequent.empty();
						sentencesWithFrequent.push(sentence);
					} else if (count == maxFrequency) {
						mostFrequentWords.push(word);
						sentencesWithFrequent.push(sentence);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(!mostFrequentWords.isEmpty()){
			System.out.println("words: " + mostFrequentWords.pop());
			System.out.println("Frequency: " + maxFrequency);
			System.out.println("Sentence: " + sentencesWithFrequent.pop());
		}
	}



	private static int indexOfWord(ArrayStack<String> words, String word, int wordCount) {
		for (int i = 0; i < wordCount; i++) {
			if (words.getIndex(i).equals(word)) {
				return i;
			}
		}
		return -1;
	}
}

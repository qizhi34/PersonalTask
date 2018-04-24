package edu.uiowa.cs.similarity;

import opennlp.tools.stemmer.PorterStemmer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	// you have any problem,you can contact QQ:707573223
	private static void printMenu() {
		System.out.println("Supported commands:");
		System.out.println("help - Print the supported commands");
		System.out.println("quit - Quit this program");
		System.out.println("index FILE - Read in and index the file given by FILE");
		System.out.println("sentences - Print sentences and Num sentences value");
	}

	public static void main(String[] args) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		List<List<String>> sentensList=new ArrayList<List<String>>();
		int numSentences=0;
		while (true) {
			System.out.print("> ");
			String command = input.readLine();
			String[] splits=command.split("\\s");
			if (command.equals("help") || command.equals("h")) {
				printMenu();
			} else if (command.equals("quit")) {
				System.exit(0);
			} else if (command.equals("sentences")){
				System.out.println(sentensList);
				System.out.println("Num sentences");
				System.out.println(numSentences);
			} else if (splits.length==2&&splits[0].equals("index")){
				// /Users/qizhi/IntelliJ/github/LeetCode/src/main/resources/cleanup_test.txt
				Scanner sc = new Scanner(new File(splits[1]));
				System.out.println("Indexing "+splits[1]);
				StringBuilder stringBuilder=new StringBuilder();

				while(sc.hasNextLine()){
					stringBuilder.append(sc.nextLine()+" ");
				}
				String content=stringBuilder.toString();

				// 读取stopwords.txt
//				String relativelyPath=System.getProperty("resources/stopwords.txt");
				//注意stopwords的路径
				String relativelyPath=Main.class.getClassLoader().getResource("stopwords.txt").getPath();
				sc= new Scanner(new File(relativelyPath));
				List<String> stopWords=new ArrayList<String>();
				while(sc.hasNextLine()){
					stopWords.add(sc.nextLine());
				}

				// 处理句子为单词
				String[] sentens=content.split("!|\\.|\\?");
				sentensList=new ArrayList<List<String>>();
				numSentences=0;
				PorterStemmer porterStemmer=new PorterStemmer();

				for (int i = 0; i < sentens.length; i++) {
					String sentens_tmp=sentens[i].replaceAll(",","").replaceAll("--","").replaceAll(":","").replaceAll(";","").replaceAll("\"","").replaceAll("’","").toLowerCase();
					String[] words_tmp=sentens_tmp.split("\\s+");
					List<String> words=new ArrayList<String>();
					for (int j = 0; j < words_tmp.length; j++) {
						if(!stopWords.contains(words_tmp[j])&&!words_tmp[j].equals("")){
							words.add(porterStemmer.stem(words_tmp[j]));
						}
					}
					if(words.size()>0){
						sentensList.add(words);
					}
				}
				numSentences=sentensList.size();

				System.out.println("Sentences");
				System.out.println(sentensList);
				System.out.println("Num sentences");
				System.out.println(numSentences);
			} else{
				System.err.println("Unrecognized command");
			}
		}
	}
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Artur
 */
public class DocumentTextExtractor {
    
    public static String extractText(MultipartFile multipartFile){
        try(InputStream inputStream=multipartFile.getInputStream()){
            String contentType=multipartFile.getContentType();
            switch(contentType){
                case "text/plain":
                    return new String(inputStream.readAllBytes(),Charset.forName("UTF-8"));
                case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                    XWPFDocument document=new XWPFDocument(inputStream);
                    XWPFWordExtractor extractor=new XWPFWordExtractor(document);
                    return extractor.getText();
                case "application/msword":
                    WordExtractor wd = new WordExtractor(inputStream);
                    return wd.getText();
                default:
                    return null;
            }
        }catch(IOException ex){
            return null;
        }
    }
    
    public static String[] extractTextFragmentatedByMarker(MultipartFile multipartFile,String marker){
        String text=extractText(multipartFile);
        return textFragmentationByMarker(text, marker);
    }
    
    public static String[] extractTextFragmentatedByExpectingSize(MultipartFile multipartFile,int expectingSize){
        String text=extractText(multipartFile);
        return textFragmentationByPartSize(text, expectingSize);
    }
    
    private static String[] textFragmentationByMarker(String text,String marker){
        String[] parts=text.split(Pattern.quote(marker));
        Stream<String> stream=Arrays.stream(parts).filter(e->!e.equals(""));
        return stream.toArray(size->new String[size]);
    }
    
    private static String[] textFragmentationByPartSize(String text,int expectingSize){
        int nextIndex=0;
        int prevIndex=0;
        List<String> parts=new ArrayList<String>();
        while (nextIndex<text.length()-1){
            prevIndex=nextIndex;
            if (prevIndex>0 && prevIndex<text.length()-1)
                prevIndex++;
            nextIndex+=expectingSize;
            if (nextIndex>text.length()-1)
                nextIndex=text.length()-1;
            nextIndex=findFirstClosestPunctuationchar(text, nextIndex);
            parts.add(text.substring(prevIndex, nextIndex+1));
        }
        return parts.toArray(size->new String[size]);
    }
    
    private static boolean isEndOfSentence(char c){
       return (c=='.' || c==',' || c=='!' || c=='?');
    }
    
    private static int checkMultiCharPunctuation(String text,int startPosition){
        while (text.length()-1>startPosition && isEndOfSentence(text.charAt(startPosition)))
            startPosition++;
        return startPosition-1;
    }
    
    private static int findFirstClosestPunctuationchar(String text,int startPosition){
        while (text.length()-1>startPosition && !isEndOfSentence(text.charAt(startPosition)))
            startPosition++;
        if (text.length()-1==startPosition)
            return startPosition;
        else
            return checkMultiCharPunctuation(text, startPosition);
    }
    
}

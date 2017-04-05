/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        TrieNode temp = this;
        for(int i=1; i<=s.length(); i++){
            String x = s.substring(0,i);
            TrieNode temp2 = temp.children.get(x);
            if(temp2 == null){
                temp2 = new TrieNode();
                temp.children.put(x,temp2);
            }
            if(i==s.length()){
                temp2.isWord = true;
            }
            temp = temp2;
        }
    }

    public boolean isWord(String s) {
        int i=0;
        if(s==""){
            return false;
        }
        TrieNode temp=this;
        boolean res = false;
        for(i=1; i<=s.length(); i++){
            TrieNode temp2 = temp.children.get(s.substring(0,i));
            if(temp2 ==null){
                res = false;
                break;
            }
            else if(temp2.isWord && i==s.length()){
                res = true;
            }
            temp=temp2;
        }
        return res;
    }

    public String getAnyWordStartingWith(String s) {
        Random r = new Random();
        if(s==""){
            int y= r.nextInt(10) + (int)'a';
            return ""+(char)y;
        }
        TrieNode temp = this;
        for(int i=1; i<=s.length(); i++){
            TrieNode temp2 = temp.children.get(s.substring(0,i));
            if(temp2 !=null){
                temp= temp2;
            }
            else{
                return null;
            }
        }
        String ans = ""+s;
        while(true){
            TrieNode temp2 = new TrieNode();
            if(temp.children.size()==0){
                return null;
            }
            int z = r.nextInt(temp.children.size());
            int iterationNum=0;
            for (String str : temp.children.keySet()) {
                if(iterationNum == z){
                    ans = str;
                    break;
                }
                iterationNum ++;
            }
            if(temp.children.get(ans).isWord){
                return ans;
            }
            else{
                temp=temp.children.get(ans);
            }
        }
    }

    public String getGoodWordStartingWith(String s) {
        TrieNode temp=this;
        for(int i=1; i<=s.length(); i++){
            TrieNode temp2 = temp.children.get(s.substring(0,i));
            if(temp2 ==null){
                return null;
            }
            temp=temp2;
        }
        Random r = new Random();
        ArrayList<String> good = new ArrayList<>();
        ArrayList<String> bad = new ArrayList<>();
        int k = temp.children.size();
        if(k==0){
            return null;
        }
        String y = null;
        for (String t:temp.children.keySet()){
            if(!temp.children.get(t).isWord){
                good.add(t);
            }
            else{
                bad.add(t);
            }
        }
        if(good.size() == 0){
            int q = r.nextInt(bad.size());
            y = bad.get(q);
        }
        else{
            int q = r.nextInt(good.size());
            y = good.get(q);
        }
        return y;
    }
}


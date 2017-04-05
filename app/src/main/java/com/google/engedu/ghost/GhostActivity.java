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

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity  {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private FastDictionary dict;
    private int score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        try {
            dict = new FastDictionary(getAssets().open("words.txt"));
        } catch (IOException e) {
            dict=null;
        }
        if (savedInstanceState != null) {
            TextView a = (TextView)findViewById(R.id.ghostText);
            TextView b = (TextView)findViewById(R.id.gameStatus);
            TextView c = (TextView)findViewById(R.id.button2);
            a.setText(savedInstanceState.getCharSequence("WordStatus"));
            b.setText(savedInstanceState.getCharSequence("GameStatus"));
            c.setText(savedInstanceState.getCharSequence("Score"));
        }
        else{
            score = 0;
            onStart(null);
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        TextView t = (TextView)findViewById(R.id.ghostText);
        TextView g = (TextView)findViewById(R.id.gameStatus);
        TextView s = (TextView)findViewById(R.id.button2);
        outstate.putCharSequence("WordStatus",t.getText());
        outstate.putCharSequence("GameStatus",g.getText());
        outstate.putCharSequence("Score",g.getText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        userTurn = false;
        label.setText(COMPUTER_TURN);

        String s = ((TextView)findViewById(R.id.ghostText)).getText().toString();
        if(dict.isWord(s) && s.length()>=4){
            label.setText("Computer Wins. You made a valid Word !!!");
        }
        else{
            String p = dict.getAnyWordStartingWith(s);
            if(p!=null) {
                addToTextView(p.substring(s.length(), s.length() + 1));
                label.setText(USER_TURN);
                userTurn = true;
            }
            else{
                label.setText("You Lose. No more Valid Words possible !!!");
            }
        }
        updateScore();
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        int c = event.getUnicodeChar();
        if((c >= (int)('a') && c <= (int)('z')) && userTurn == true){
            String s = ((TextView)findViewById(R.id.ghostText)).getText().toString();
            s += ""+(char)c;
            ((TextView) findViewById(R.id.ghostText)).setText(s);
            computerTurn();
        }
        return super.onKeyUp(keyCode, event);
    }
    public void challenge(View view){
        if(!userTurn){
            return;
        }
        TextView tv = ((TextView)findViewById(R.id.ghostText));
        TextView label = (TextView) findViewById(R.id.gameStatus);
        String s = tv.getText().toString();
        String y = dict.getAnyWordStartingWith(s);
        if(dict.isWord(s) && s.length()>=4){
            label.setText("You Win !!!!\nIt is indeed a valid word.");
            score++;
        }
        else if(y!=null){
            label.setText("You Lose.\nOne possible word could be "+y);
        }
        else{
            label.setText("You Win !!!!\nNo valid words can be made now.");
            score++;
        }
        updateScore();
        userTurn = false;
    }

    private void addToTextView(String p){
        String s = ((TextView)findViewById(R.id.ghostText)).getText().toString();
        s += p;
        ((TextView)findViewById(R.id.ghostText)).setText(s);
    }

    private void updateScore(){
        TextView s = (TextView)findViewById(R.id.button2);
        s.setText("Score : "+score);
    }
}
